from __future__ import annotations

from typing import TYPE_CHECKING, Optional, Dict

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

        cu_graph = build_cugraph(graph.adj, weight_transform=lambda x: math.log(x + 1 / x))
        seeds, _ = extract_bool_attr_ids(bw_attr, graph.node_attrs)
        if seeds.size < 1:
            return
        self.logger.info("Found {} black nodes in {} nodes".format(seeds.size, graph.nodes))
        dists = [
            cupy.asarray(sssp(cu_graph, seed)['distance'])
            for seed in seeds
        ]
        dists = cupy.asarray(dists).T  # shape = (nodes, seeds)
        dists[dists > 1e14] = cupy.inf
        # self.logger.info('dists = \n{}'.format(dists))
        mal = self._compute_mal_scores(dists)
        # if not (mal[seeds] == 1.0).all():
        #     self.logger.warn("PBI result maybe invalid.")
        #     self.logger.warn('mal = \n{}'.format(mal))
        scores: Dict[int, float] = dict(enumerate(mal))
        self.logger.info('Updated {} scores'.format(mal.size))
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
