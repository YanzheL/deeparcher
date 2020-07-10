from __future__ import annotations

import math
from typing import TYPE_CHECKING, Optional, Dict, NoReturn

if TYPE_CHECKING:
    from app.struct import Graph

import cudf
import cugraph
import cupy
from cugraph.traversal.sssp import sssp

from app.analyzer.interface import GraphAnalyzer
from app.io.graph_converter import build_cugraph
from app.util.misc import extract_bool_attr_ids


class PathBasedInferenceAnalyzer(GraphAnalyzer):
    """GraphAnalyzer for computing path-based inference algorithm by Khalil et al.

    Author:
        Yanzhe Lee <lee.yanzhe@yanzhe.org>

    References:
        Issa K., Ting Y., and Bei G. (2016).
        Discovering Malicious Domains through Passive DNS Data Graph Analysis.
        In Proceedings of the 11th ACM on Asia Conference on Computer and Communications Security (ASIA CCS ’16).
        Association for Computing Machinery, New York, NY, USA, 663–674.

    """

    def analyze(
            self,
            graph: Graph,
            ctx: dict,
            bw_attr: str = 'black_or_white',
            dst_attr: str = 'pbi_prob'
    ) -> Optional[Graph]:
        """Analyzer entrypoint.

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            bw_attr: Name of a boolean node attribute which indicates whether the node is a known black or white node.
            dst_attr: Name of the output node attribute.

        Returns:
            Graph: An analyzed graph which contains an output node attribute.

        """

        if 'cu_graph' not in graph.meta:
            graph.meta['cu_graph'] = build_cugraph(graph.adj)
        cu_graph = graph.meta['cu_graph']
        seeds, _ = extract_bool_attr_ids(bw_attr, graph.node_attrs)
        if seeds.size <= 1:
            self.logger.info('Skipped a trivial graph. It has only {} seeds in {} nodes.'.format(
                seeds.size, graph.nodes))
            return
        self._transform_weights(cu_graph)
        # np.vectorize(lambda s: sssp(graph,s)['distance'].to_array(),signature='()->(n)')
        dists = [
            cupy.asarray(sssp(cu_graph, seed)['distance'].data)
            for seed in seeds
        ]
        dists = cupy.asarray(dists).T  # shape = (nodes, seeds)
        scores: Dict[int, float] = dict(enumerate(self._compute_mal_scores(dists)))
        self.logger.info('Updated {} scores'.format(len(scores)))
        graph.node_attrs[dst_attr] = scores
        return graph

    def accept(self, graph: Graph) -> bool:
        return graph.connected and not graph.directed

    @staticmethod
    def _compute_mal_scores(dists: cupy.ndarray) -> cupy.ndarray:
        n_seeds = dists.shape[1]
        assocs = dists
        assocs *= -1
        cupy.exp(assocs, assocs)
        assocs.sort(int_axis=1)
        largest_assoc = assocs[:, -1]
        mal = largest_assoc
        if n_seeds > 2:
            coef = cupy.logspace(n_seeds - 1, 2 - 1, num=n_seeds - 1, base=0.5, dtype=cupy.float32)
            mal += (1 - largest_assoc) * cupy.sum(coef * assocs[:, 1:], axis=1)
        return mal

    @staticmethod
    def _transform_weights(graph: cugraph.Graph) -> NoReturn:
        weights: cudf.Series = graph.view_edge_list()['weight']
        graph.view_edge_list()['weight'] = weights.applymap(lambda x: math.log(x + 1 / x))
