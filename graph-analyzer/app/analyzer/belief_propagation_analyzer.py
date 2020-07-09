from typing import *

import numpy as np

import app.third_party.factorgraph as fg
from app.analyzer.interface import GraphAnalyzer
from app.struct import Graph
from app.util.misc import extract_bool_attr_ids


class BeliefPropagationAnalyzer(GraphAnalyzer):

    def analyze(self, graph: Graph, ctx: dict, max_iter: int, prob_diff: float) -> Optional[Graph]:
        """Analyzer entrypoint.

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            max_iter: Max iterations.
            prob_diff: Difference of inference possibility.

        Returns:
            Graph: An analyzed graph with 'bp_prob' node attribute.

        """
        if not graph.connected:
            self.logger.warn('Current graph is not connected, now skipped.')
            return
        black_node_ids, white_node_ids = extract_bool_attr_ids('black_or_white', graph.node_attrs)
        g = fg.Graph()
        # Construct the fg.Graph
        for rv_marginal in range(graph.nodes):
            g.rv(str(rv_marginal), 2)
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
        graph.node_attrs['bp_prob'] = scores
        return graph
