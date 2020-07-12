from __future__ import annotations

from typing import TYPE_CHECKING, Dict

if TYPE_CHECKING:
    from app.struct import Graph

import cupy
from cugraph.traversal.sssp import sssp

from app.analyzer.interface import GraphAnalyzer
from app.io.graph_converter import build_cugraph
from app.util.misc import extract_bool_attr_ids
import math


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
    ) -> Graph:
        """Analyzer entrypoint.

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            bw_attr: Name of a boolean node attribute which indicates whether the node is a known black or white node.
            dst_attr: Name of the output node attribute.

        Returns:
            Graph: An analyzed graph which contains an output node attribute.

        """

        cu_graph = build_cugraph(graph.adj, weight_transform=lambda x: math.log(x + 1 / x))
        bw_meta_key = 'bw_node_ids_{}'.format(bw_attr)
        if bw_meta_key not in graph.meta:
            graph.meta[bw_meta_key] = extract_bool_attr_ids(bw_attr, graph.node_attrs)
        seeds, _ = graph.meta[bw_meta_key]
        if seeds.size < 1:
            return graph
        self.logger.info("Found {} black nodes in {} nodes".format(seeds.size, graph.nodes))
        dists = [
            cupy.asarray(sssp(cu_graph, seed)['distance'])
            for seed in seeds
        ]
        dists = cupy.asarray(dists).T  # shape = (nodes, seeds)
        dists[dists > 1e14] = cupy.inf
        # self.logger.info('dists = \n{}'.format(dists))
        possibilities = self._compute_mal_scores(dists)
        # if not (possibilities[seeds] == 1.0).all():
        #     self.logger.warn("PBI result maybe invalid.")
        #     self.logger.warn('mal = \n{}'.format(possibilities))
        possibilities[possibilities <= cupy.finfo(cupy.float32).eps] = 0.0
        self.logger.info(
            "The computed {} attribute of Graph<parent_id={},id={}> has {} positive values.".format(
                dst_attr, graph.parent_id, graph.id, cupy.count_nonzero(possibilities))
        )
        # Write the result to graph.node_attrs
        scores: Dict[int, float] = dict(enumerate(possibilities))
        graph.node_attrs[dst_attr] = scores
        return graph

    def accept(self, graph: Graph) -> bool:
        return graph.connected and not graph.directed

    @staticmethod
    def _compute_mal_scores(dists: cupy.ndarray) -> cupy.ndarray:
        n_seeds = dists.shape[1]
        assocs = dists
        assocs = cupy.multiply(assocs, -1, out=assocs)
        cupy.exp(assocs, assocs)
        assocs = cupy.sort(assocs, axis=1)
        largest_assoc = assocs[:, -1]
        mal = largest_assoc
        if n_seeds > 2:
            coef = cupy.logspace(n_seeds - 1, 2 - 1, num=n_seeds - 1, base=0.5, dtype=cupy.float32)
            mal += (1 - largest_assoc) * cupy.sum(coef * assocs[:, 1:], axis=1)
        return mal
