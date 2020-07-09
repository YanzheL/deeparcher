import math
from typing import *

import cudf
import cugraph
import cupy
import numpy as np
from cugraph.traversal.sssp import sssp

from app.analyzer.interface import GraphAnalyzer
from app.io.graph_converter import build_cugraph
from app.struct import Graph
from app.util.misc import timing, extract_bool_attr_ids


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

    def analyze(self, graph: Graph, ctx: dict, attr_name: str = 'black_or_white') -> Optional[Graph]:
        """Analyzer entrypoint.

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            attr_name: Name of a boolean node attribute which indicates whether the node is a known black or white node.

        Returns:
            Graph: An analyzed graph with 'pbi_prob' node attribute.

        """
        if not graph.connected:
            self.logger.warn('Current graph is not connected, now skipped.')
            return
        if 'cugraph' not in graph.meta:
            graph.meta['cugraph'] = build_cugraph(graph.adj)
        cug = graph.meta['cugraph']
        seeds, _ = extract_bool_attr_ids(attr_name, graph.node_attrs)
        scores: Dict[int, float] = {s: 1.0 for s in seeds}
        if seeds.size <= 1:
            self.logger.info('Skipped a trivial graph. It has only {} seeds in {} nodes.'.format(
                seeds.size, graph.nodes))
            return
        self._initialize_weights(cug)
        dists = []
        # np.vectorize(lambda s: sssp(graph,s)['distance'].to_array(),signature='()->(n)')
        for seed in seeds:
            dist = cupy.asarray(sssp(cug, seed)['distance'].data)
            dists.append(dist)
        dists = cupy.asarray(dists).T  # shape = (nodes, seeds)
        scores.update(zip(graph.node_id_remap, self._compute_mal_scores(dists)))
        self.logger.info('Updated {} scores'.format(len(scores)))
        graph.node_attrs['pbi_prob'] = scores
        return graph

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

    @staticmethod
    def _initialize_weights(graph: cugraph.Graph):
        weights: cudf.Series = graph.view_edge_list()['weight']
        graph.view_edge_list()['weight'] = weights.applymap(lambda x: math.log(x + 1 / x))
