import math
from queue import Queue
from typing import *

import cudf
import cugraph
import cupy
import numpy as np
from cugraph.components.connectivity import weakly_connected_components
from cugraph.traversal.sssp import sssp

from app.analyzer.interface import GraphAnalyzer, GraphAttrExtractor
from app.model import NodeAttrMap, Graph
from app.util.misc import timing, load_blacklist


class MaliciousAssociationAnalyzer(GraphAnalyzer, GraphAttrExtractor):

    def __init__(
            self,
            graph: Graph,
            blacklist: Optional[str] = None,
            cc_size_thres=0
    ):
        super(GraphAnalyzer).__init__(graph)
        self._blacklist: Optional[np.ndarray] = None
        if blacklist is not None:
            self._blacklist = load_blacklist(blacklist, self.logger)
        node_labels = []
        for node_attr in self.graph.node_attrs:
            if node_attr.name == 'fqdn':
                items: List[Tuple[int, str]] = node_attr.data.items()
                items.sort(key=lambda x: x[0])
                for _, fqdn in items:
                    node_labels.append(fqdn)
                break
        if len(node_labels) != self.graph.nodes:
            raise ValueError(
                "The input graph has {} nodes, but only {} nodes have 'fqdn' label.".format(self.graph.nodes,
                                                                                            len(node_labels)))
        self._cc_size_thres = cc_size_thres
        self._node_labels: np.ndarray = np.array(node_labels, dtype=str)
        self._cugraph: cugraph.DiGraph = self._build_cugraph()
        self._black_node_ids: Optional[np.ndarray] = None
        self._components: List[cudf.Series] = []
        self._scores: Dict[int, float] = {}
        self._task_queue = Queue()
        self._finished = False

    @timing
    def _build_cugraph(self) -> cugraph.DiGraph:
        adj = self.graph.adj.tocoo()
        df = cudf.DataFrame()
        df['src'] = adj.row
        df['dst'] = adj.col
        df['weight'] = adj.data
        g = cugraph.DiGraph()
        g.from_cudf_edgelist(df, 'src', 'dst', 'weight')
        # adj = self.graph.adj.tocsr()
        # offsets = cudf.Series(adj.indptr)
        # indices = cudf.Series(adj.indices)
        # g.from_cudf_adjlist(offsets, indices, None)
        return g

    @timing
    def _run(self, blacklist: Optional[str] = None) -> NoReturn:
        if blacklist is not None:
            self._blacklist = load_blacklist(blacklist, self.logger)
            self._black_node_ids = None
        if self._black_node_ids is None:
            self.logger.info('Computing black node ids...')
            self._black_node_ids = np.intersect1d(
                self._node_labels, self._blacklist, assume_unique=True, return_indices=True
            )[1]
            self.logger.info('Found {} black nodes in the whole graph'.format(self._black_node_ids.size))
            for id in self._black_node_ids:
                self._scores[id] = 1.0
        if self.graph.connected or len(self._compute_connected_components()) == 1:
            self.logger.info('The input graph is connected.')
            self._run_for_connected_graph(self._cugraph, self.graph.node_id_remap)
        else:
            for idx, cc in enumerate(self._components):
                if len(cc) <= self._cc_size_thres:
                    continue
                adj_list = cugraph.subgraph(self._cugraph, cc).view_edge_list()
                edges = cudf.DataFrame()
                edges['src'], edges['dst'], node_id_remap = cugraph.renumber(adj_list['src'], adj_list['dst'])
                edges['weight'] = adj_list['weight']
                subgraph = cugraph.DiGraph()
                subgraph.from_cudf_edgelist(edges, 'src', 'dst', 'weight')
                self.logger.info(
                    'Begin analyzing current connected subgraph {}.'.format(self.__class__, idx))
                self._run_for_connected_graph(subgraph, node_id_remap)
        self._finished = True

    @timing
    def _run_for_connected_graph(self, graph: cugraph.Graph, node_id_remap: Optional[np.ndarray] = None) -> NoReturn:
        if node_id_remap is not None:
            seeds = np.intersect1d(node_id_remap, self._black_node_ids, assume_unique=True, return_indices=True)[1]
        else:
            seeds = self._black_node_ids
        if seeds.size <= 1:
            self.logger.info('Skipped a trivial subgraph. It has only {} seeds in {} nodes.'.format(
                seeds.size, graph.number_of_nodes()))
            return
        self._initialize_weights(graph)
        dists = []
        # np.vectorize(lambda s: sssp(graph,s)['distance'].to_array(),signature='()->(n)')
        for seed in seeds:
            dist = cupy.asarray(sssp(graph, seed)['distance'].data)
            dists.append(dist)
        dists = cupy.asarray(dists).T  # shape = (nodes, seeds)
        scores = self._compute_mal_scores(dists)
        self._scores.update(zip(node_id_remap, scores))
        self.logger.info('Updated {} scores'.format(scores.size))

    @staticmethod
    @timing
    def _compute_mal_scores(dists: cupy.ndarray) -> cupy.ndarray:
        n_seeds = dists.shape[1]
        assocs = dists
        assocs *= -1
        cupy.exp(assocs, assocs)
        assocs.sort(int_axis=1)
        largest_assoc = assocs[:, -1]
        mal = largest_assoc
        if n_seeds > 2:
            coef = cupy.logspace(n_seeds - 1, 2 - 1, num=n_seeds - 1, base=0.5, dtype=np.float64)
            mal += (1 - largest_assoc) * cupy.sum(coef * assocs[:, 1:], axis=1)
        return mal

    @timing
    def _compute_connected_components(self) -> List[cudf.Series]:
        if self.graph.component_attrs is None:
            self.logger.info('Computing weakly connected components...')
            cc_df = weakly_connected_components(self._cugraph)['labels']
            n_components = cc_df.max()
            self.logger.info('Got {} weakly connected components.'.format(n_components))
            if n_components == 1:
                self.graph.connected = True
                self._components.append(self._cugraph.nodes())
            else:
                for i in range(1, cc_df.max() + 1):
                    component = cc_df.query("labels == {}".format(i))['vertices']
                    self._components.add(component)
        else:
            self.logger.info('Found {} precomputed weakly connected components in graph attributes.'.format(
                len(self.graph.component_attrs)))
            for cc in self.graph.component_attrs:
                self._components.append(cudf.Series(cc.components, dtype=np.int32))
        return self._components

    def is_finished(self) -> bool:
        return self._finished

    def get_node_attrs(self, **kwargs) -> List[NodeAttrMap]:
        return [NodeAttrMap('mal_assoc_prob', self._scores)]

    def reset(self) -> NoReturn:
        self._scores.clear()
        self._finished = False

    def is_restartable(self) -> bool:
        return True

    @staticmethod
    def _initialize_weights(graph: cugraph.Graph):
        weights: cudf.Series = graph.view_edge_list()['weight']
        graph.view_edge_list()['weight'] = weights.applymap(lambda x: math.log(x + 1 / x))

# from timeit import timeit
#
# if __name__ == '__main__':
#     a1 = np.random.rand(10000)
#     a2 = np.random.random(20000)
#     s1 = cudf.Series(a1, dtype=np.float, name='data')
#     s2 = cudf.Series(a2, dtype=np.float, name='data')
#
#     print(timeit(lambda: np.intersect1d(a1, a2, assume_unique=True), number=1))
#     print(timeit(lambda: s1.merge(s2), number=1))
