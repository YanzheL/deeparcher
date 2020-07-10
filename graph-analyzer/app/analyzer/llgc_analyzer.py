from typing import *

import cupy
import cupyx
from cupyx.scipy.sparse import spmatrix, diags, csr_matrix
from numpy.linalg import LinAlgError

from app.analyzer.interface import GraphAnalyzer
from app.struct import Graph


class LLGCAnalyzer(GraphAnalyzer):
    """GraphAnalyzer for computing local and global consistency algorithm by Zhou et al.

    This implementation is an optimized version of
    networkx.algorithms.node_classification.lgc.local_and_global_consistency.
    The dependency of networkx library is removed here to avoid graph conversion overhead.

    Author:
        Yanzhe Lee <lee.yanzhe@yanzhe.org>, Linxin Chen <chen_linxin@outlook.com>

    Original Author (networkx):
        Yuto Yamaguchi <yuto.ymgc@gmail.com>

    References:
        Zhou, D., Bousquet, O., Lal, T. N., Weston, J., & Schölkopf, B. (2004).
        Learning with local and global consistency.
        Advances in neural information processing systems, 16(16), 321-328.

    .. seealso:: :func:`networkx.algorithms.node_classification.lgc.local_and_global_consistency`

    """

    def analyze(
            self,
            graph: Graph,
            ctx: dict,
            alpha: float = 0.99,
            max_iter: int = 30,
            attr_name: str = 'black_or_white'
    ) -> Optional[Graph]:
        """Analyzer entrypoint.

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            alpha: Clamping factor.
            max_iter: Max iterations.
            attr_name: Name of a boolean node attribute which indicates whether the node is a known black or white node.

        Returns:
            Graph: An analyzed graph with 'llgc_prob' node attribute.

        """

        labels = self._extract_boolean_attributes(graph, attr_name)
        if labels.shape[0] == 0:
            raise ValueError('No node on the input graph is labeled by {}'.format(attr_name))

        X = csr_matrix(graph.adj)
        n_samples = X.shape[0]
        n_classes = cupy.unique(labels[:, 1]).size
        F = cupy.zeros((n_samples, n_classes))
        P = self._build_propagation_matrix(X, alpha)
        B = self._build_base_matrix(X, labels, alpha, n_classes)
        F, converged = self._propagate_converged(P, F, B, max_iter)
        predicted = self._predict(F)
        # 将list形式的预测结果转变为_scores={数字编号：信誉度,}
        scores: Dict[int, float] = {node: value for node, value in enumerate(predicted)}
        graph.node_attrs['llgc_prob'] = scores
        return graph

    def accept(self, graph: Graph) -> bool:
        return graph.connected

    @staticmethod
    def _extract_boolean_attributes(graph: Graph, attr_name: str) -> cupy.ndarray:
        """Get and return information of boolean node attribute from the input graph

        Args:
            graph: The graph struct
            attr_name: Name of the target label

        Returns:
            numpy.ndarray: Array of pairs of labeled node ID and label ID, shape = [n_labeled_samples, 2]

        """

        attr = graph.node_attrs.get(attr_name, {})
        return cupy.array([[node_id, int(value)] for node_id, value in attr.items()], dtype=cupy.int32)

    @staticmethod
    def _build_propagation_matrix(X: spmatrix, alpha: float) -> spmatrix:
        """Build propagation matrix of Local and global consistency

        Args:
            X: Adjacency matrix, shape = [n_samples, n_samples]
            alpha: Clamping factor

        Returns:
            cupy.sparse.spmatrix: Propagation matrix, shape = [n_samples, n_samples].

        """

        degrees = X.sum(axis=0).A[0]
        degrees[degrees == 0] = 1  # Avoid division by 0
        D2 = cupy.sqrt(diags((1.0 / degrees), offsets=0))
        S = alpha * D2.dot(X).dot(D2)
        return S

    @staticmethod
    def _build_base_matrix(X: spmatrix, labels: cupy.ndarray, alpha: float, n_classes: int):
        """Build base matrix of Local and global consistency

        Args:
            X: Adjacency matrix, shape = [n_samples, n_samples]
            labels: Array of pairs of node id and label id, shape = [n_samples, 2]
            alpha: Clamping factor
            n_classes: The number of classes (distinct labels) on the input graph

        Returns:
            numpy.ndarray: Base matrix, shape = [n_samples, n_classes].

        """

        n_samples = X.shape[0]
        B = cupy.zeros((n_samples, n_classes))
        B[labels[:, 0], labels[:, 1]] = 1 - alpha
        return B

    @staticmethod
    def _propagate_converged(P: spmatrix, F: cupy.ndarray, B: cupy.ndarray, max_iters: int, force_iter=False) -> Tuple[
        cupy.ndarray, bool]:
        """Try to propagate F = P * F + B to its converged value.

        .. math::

            F = \lim_{n \\to + \\infty} (P^{n} * F_{0} + ( \sum_{k=0}^{n-1} P_{k} ) * B)  = (I - P)^{-1} * B

        Args:
            P: Propagation matrix, shape = [n_samples, n_samples].
            F: Label matrix, shape = [n_samples, n_classes].
            B: Base matrix, shape = [n_samples, n_classes].
            max_iters: The max iteration steps to operate if there is no optimal converged value.
            force_iter: Use iterations instead of trying to find optimal value.

        Returns:
            cupy.ndarray: Label matrix, shape = [n_samples, n_classes].

        """
        converged = False
        if not force_iter:
            cupyx.seterr(linalg='raise')
            n_samples = P.shape[0]
            X = cupy.eye(n_samples, dtype=cupy.float32) - P
            try:
                F = cupy.linalg.inv(X).dot(B)
                converged = True
            except LinAlgError:
                converged = False
        if not converged:
            remaining_iter = max_iters
            while remaining_iter > 0:
                F = P.dot(F) + B
                remaining_iter -= 1
        return F, converged

    @staticmethod
    def _predict(F: cupy.ndarray):
        """Predict labels by learnt label matrix

        Args:
            F : numpy array, shape = [n_samples, n_classes]
                Learnt (resulting) label matrix

        Returns:
            numpy.ndarray: Array of predicted label ids, shape = [n_samples].

        """

        predicted_label_ids = cupy.argmax(F, axis=1)
        return predicted_label_ids
