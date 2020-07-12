from __future__ import annotations

from typing import TYPE_CHECKING, Dict

if TYPE_CHECKING:
    from app.struct import Graph

import numpy as np

import app.third_party.factorgraph as fg
from app.analyzer.interface import GraphAnalyzer

from app.util.misc import extract_bool_attr_ids


class BeliefPropagationAnalyzer(GraphAnalyzer):
    """GraphAnalyzer for computing belief propagation algorithm.

    Author:
        Yanzhe Lee <lee.yanzhe@yanzhe.org>, Linxin Chen <chen_linxin@outlook.com>

    """

    def analyze(
            self,
            graph: Graph,
            ctx: dict,
            max_iter: int = 30,
            prob_diff: float = 0.01,
            bw_attr: str = 'black_or_white',
            dst_attr: str = 'bp_prob'
    ) -> Graph:
        """Analyzer entrypoint.

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            max_iter: Max iterations.
            prob_diff: Difference of inference possibility.
            bw_attr: Name of a boolean node attribute which indicates whether the node is a known black or white node.
            dst_attr: Name of the output node attribute.

        Returns:
            Graph: An analyzed graph which contains an output node attribute.

        """
        bw_meta_key = 'bw_node_ids_{}'.format(bw_attr)
        if bw_meta_key not in graph.meta:
            graph.meta[bw_meta_key] = extract_bool_attr_ids(bw_attr, graph.node_attrs)
        black_node_ids, white_node_ids = graph.meta[bw_meta_key]
        g = fg.Graph()
        # Construct the fg.Graph
        for node_id in range(graph.nodes):
            g.rv(str(node_id), 2)
        # Add explicit node potentials.
        for good in white_node_ids:
            g.factor([str(good)], potential=np.array([0.99, 0.01]))  # 良性
        for bad in black_node_ids:
            g.factor([str(bad)], potential=np.array([0.01, 0.99]))  # 恶意
        # Add implicit node potentials.
        adj = graph.adj.tocoo()
        for i, j, v in zip(adj.row, adj.col, adj.data):
            g.factor(
                [str(i), str(j)],
                potential=np.array([
                    [0.5 + prob_diff, 0.5 - prob_diff],
                    [0.5 - prob_diff, 0.5 + prob_diff],
                ])
            )
        # Start propagation.
        g.lbp(init=True, normalize=True, max_iters=max_iter, progress=False)
        # Write results.
        scores: Dict[int, float] = {}
        rv_marginals = g.rv_marginals()
        for rv, marginal in rv_marginals:
            total, incoming = marginal
            name = rv.name
            scores[int(name)] = total / (total + incoming)
        self.logger.info(
            "The computed {} attribute of Graph<parent_id={},id={}> has {} values.".format(
                dst_attr, graph.parent_id, graph.id, len(scores))
        )
        # Write the result to graph.node_attrs
        graph.node_attrs[dst_attr] = scores
        return graph

    def accept(self, graph: Graph) -> bool:
        return graph.connected
