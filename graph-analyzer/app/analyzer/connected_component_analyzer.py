from __future__ import annotations

from typing import TYPE_CHECKING, List, Dict, Union

if TYPE_CHECKING:
    from scipy.sparse import csr_matrix

import numpy as np
import cupy
from cugraph.components.connectivity import weakly_connected_components, strongly_connected_components
from app.analyzer.interface import GraphAnalyzer
from app.io import to_cugraph
from app.struct.graph_attributes import ComponentAttr
from app.struct import Graph


class ConnectedComponentsAnalyzer(GraphAnalyzer):
    """GraphAnalyzer for splitting a sparse graph into connected components.

    Components will be output as a sub-graph list.

    Author:
        Yanzhe Lee <lee.yanzhe@yanzhe.org>

    """

    def __init__(self, weak=True):
        super().__init__()
        self._cc_func = weakly_connected_components if weak else strongly_connected_components

    def analyze(self, graph: Graph, ctx: dict, cc_size_thres=50) -> Union[List[Graph], Graph]:
        if graph.connected or len(graph.component_attrs) == 1:
            self.logger.debug('Current graph is connected, now skipped.')
            graph.connected = True
            return graph
        if len(graph.component_attrs) == 0:
            self.logger.info('Computing weakly connected components...')
            if 'cugraph' not in graph.meta:
                graph.meta['cugraph'] = to_cugraph(graph.adj)
            cug = graph.meta['cugraph']
            cc_res = self._cc_func(cug)
            # self.logger.debug(cc_res)
            cc_mat: cupy.ndarray = cupy.fromDlpack(cc_res.to_dlpack())
            # self.logger.debug(cc_mat)
            grouped = cc_res.groupby('labels')
            label_count = grouped.count()
            # self.logger.debug('\n{}'.format(label_count))
            n_components = len(label_count)
            self.logger.info('Got {} weakly connected components'.format(n_components))
            if n_components == 1:
                graph.connected = True
                return graph
            else:
                interested_labels = label_count.query('vertices > {}'.format(cc_size_thres)) \
                    .index.to_series()
                interested_labels = cupy.asarray(interested_labels)
                self.logger.info(
                    'Got {} components larger than threshold.'.format(interested_labels.size, cc_size_thres))
                for i in interested_labels:
                    # component: cupy.ndarray = cupy.asarray(cc_res.query(f'labels == {i}')['vertices'])
                    component: cupy.ndarray = cc_mat[cc_mat[:, 0] == i][:, 1]
                    component = cupy.sort(component)
                    component = cupy.asnumpy(component)
                    # self.logger.debug(component)
                    component_attr = ComponentAttr(i - 1, component)
                    graph.component_attrs.append(component_attr)
        else:
            self.logger.info('Found {} precomputed weakly connected components in graph attributes.'.format(
                len(graph.component_attrs)))
        self.logger.info('Constructing {} sub-graphs'.format(len(graph.component_attrs)))
        return [self.extract_subgraph(graph, component_attr) for component_attr in graph.component_attrs]

    @staticmethod
    def extract_subgraph(graph: Graph, component_attr: ComponentAttr) -> Graph:
        component = component_attr.components
        adj: csr_matrix = graph.adj.tocsr()[component, :][:, component]
        return Graph(
            id=component_attr.id,
            nodes=component.size,
            edges=adj.count_nonzero(),
            adj=adj,
            directed=graph.directed,
            connected=True,
            unweighted=graph.unweighted,
            parent_id=graph.id,
            node_id_remap=component,
            node_attrs=ConnectedComponentsAnalyzer.extract_sub_attr_map(graph.node_attrs, component),
            edge_attrs=ConnectedComponentsAnalyzer.extract_sub_attr_map(graph.edge_attrs, component)
        )

    @staticmethod
    def extract_sub_attr_map(attrs: Dict[str, Dict[int, object]], component: np.ndarray) \
            -> Dict[str, Dict[int, object]]:
        ret = {}
        for name, data in attrs.items():
            ids = list(data.keys())
            parent_attr_ids, component_attr_ids, _ = np.intersect1d(component, ids, assume_unique=True,
                                                                    return_indices=True)
            values = map(lambda i: data[i], parent_attr_ids)
            sub_attr_map = dict(zip(component_attr_ids, values))
            if len(sub_attr_map) > 0:
                ret[name] = sub_attr_map
        return ret
