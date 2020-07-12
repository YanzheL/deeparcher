from __future__ import annotations

from typing import TYPE_CHECKING, Dict

if TYPE_CHECKING:
    from app.struct import Graph

import cupy
from cupyx.scipy.sparse import csr_matrix
from scipy.sparse import triu

from app.analyzer.interface import GraphAnalyzer
from app.util.misc import extract_bool_attr_ids, normalize, normalize_sparse


class TopologyBasedFlowAnalyzer(GraphAnalyzer):
    """GraphAnalyzer for computing topology-based-flow algorithm.

    Author:
        Yanzhe Lee <lee.yanzhe@yanzhe.org>, Linxin Chen <chen_linxin@outlook.com>

    """

    def analyze(
            self,
            graph: Graph,
            ctx: dict,
            max_iters: int = 5,
            legal_weight: float = -1.0,
            bw_attr: str = 'black_or_white',
            dst_attr: str = 'tbf_prob'
    ) -> Graph:
        """Analyzer entrypoint.

        This algorithm does the following steps.

        1. Initialize two reputation vectors named v_good and v_bad.
        2. Propagate reputations by multiplying reputation vectors with graph's adjacent matrix.
           THe labeled values will be maintained across propagation iterations.
        3. Calculate the final reputation vector based on the pre-configured weight of v_good.
        4. Normalize reputation vectors (Scale to [0,1]).
        5. Write the final reputation score of each node to graph.node_attrs[dst_attr]

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            max_iters: Max iterations.
            bw_attr: Name of a boolean node attribute which indicates whether the node is a known black or white node.
            dst_attr: dst_attr: Name of the output node attribute.

        Returns:
            Graph: An analyzed graph which contains an output node attribute.

        """

        black_node_ids, white_node_ids = extract_bool_attr_ids(bw_attr, graph.node_attrs)
        if not graph.directed:
            # Symmetrize the adjacent matrix of undirected graph.
            adj_triu = csr_matrix(triu(graph.adj, k=1))
            adj = adj_triu + adj_triu.T
        else:
            adj = csr_matrix(graph.adj, dtype=cupy.float32)
        # Normalize the adjacent matrix.
        adj = normalize_sparse(adj)
        # Initialize two reputation vectors as a propagation matrix with shape = (nodes, 2).
        P = cupy.zeros((graph.nodes, 2), dtype=cupy.float32)
        v_bad = P[:, 0]
        v_good = P[:, 1]
        # Initialize reputation of known nodes to 1.0.
        v_bad[black_node_ids] = 1.0
        v_good[white_node_ids] = 1.0
        # Begin the propagation.
        remaining_iter = max_iters
        while remaining_iter > 0:
            # Propagate.
            P[:, :] = P + adj.dot(P)
            # Normalize.
            v_bad[:] = normalize(v_bad)
            v_good[:] = normalize(v_good)
            remaining_iter -= 1
        # Calculate the final reputation vector with shape = (nodes,).
        v_result = v_bad + legal_weight * v_good
        # Restore scores of known nodes.
        v_result[white_node_ids] = legal_weight
        v_result[black_node_ids] = 1.0
        # Normalize the final reputation vector.
        possibilities = normalize(v_result)
        possibilities[possibilities <= cupy.finfo(cupy.float32).eps] = 0.0
        # self.logger.debug(reputations[reputations.nonzero()])
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

    # `   @staticmethod
    #     def _normalize(s1: np.ndarray, v1: np.ndarray, v2: np.ndarray) -> NoReturn:
    #         avg_2 = np.sum(v2[s1]) / np.count_nonzero(v2[s1])
    #         v1[v1 > 1] = 1.0
    #         v2[s1] = 1.0
    #         v1[v1 > 1 - avg_2] = 1.0
