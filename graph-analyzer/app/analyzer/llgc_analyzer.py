from __future__ import annotations

from typing import TYPE_CHECKING, Dict, Tuple

if TYPE_CHECKING:
    from app.struct import Graph
    from cupyx.scipy.sparse import spmatrix

import cupy
import cupyx
from cupyx.scipy.sparse import diags, csr_matrix
from numpy.linalg import LinAlgError

from app.analyzer.interface import GraphAnalyzer


class LLGCAnalyzer(GraphAnalyzer):
    """GraphAnalyzer for computing local and global consistency algorithm by Zhou et al.

    This is a GPU optimized implementation of
    networkx.algorithms.node_classification.lgc.local_and_global_consistency.
    The dependency of networkx library is removed here to avoid graph conversion overhead.

    Labeled samples should contains only 2 classes. In other words, the label value should be boolean.

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
            max_iters: int = 30,
            bw_attr: str = 'black_or_white',
            dst_attr: str = 'llgc_prob'
    ) -> Graph:
        """Analyzer entrypoint.

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            alpha: Clamping factor.
            max_iters: Max iterations.
            bw_attr: Name of a boolean node attribute which indicates whether the node is a known black or white node.
            dst_attr: dst_attr: Name of the output node attribute.

        Returns:
            Graph: An analyzed graph which contains an output node attribute.

        """

        labels = self._extract_boolean_attributes(graph, bw_attr)
        if labels.shape[0] == 0:
            self.logger.error('No node on {} is labeled by {}'.format(
                graph, bw_attr))
            return graph

        X = csr_matrix(graph.adj)
        n_samples = X.shape[0]
        n_labeled_classes = cupy.unique(labels[:, 1]).size
        n_expected_classes = 2
        if n_labeled_classes != n_expected_classes:
            self.logger.warn(
                "{} has insufficient label classes, expected {}, got {}, skipped.".format(
                    graph, n_expected_classes, n_labeled_classes))
            return graph
        F = cupy.zeros((n_samples, n_expected_classes), dtype=cupy.float32)
        P = self._build_propagation_matrix(X, alpha)
        B = self._build_base_matrix(X, labels, alpha, n_expected_classes)
        F, converged = self._propagate_converged(P, F, B, max_iters)
        if not converged:
            self.logger.info(
                "The computed {} attribute of {} is not converged. This is ok but not optimal.".format(
                    dst_attr, graph, n_labeled_classes, n_expected_classes))
        possibilities = self._compute_positive_prob(F)
        possibilities[possibilities <= cupy.finfo(cupy.float32).eps] = 0.0
        self.logger.info(
            "The computed {} attribute of {} has {} positive values.".format(
                dst_attr, graph, cupy.count_nonzero(possibilities))
        )
        # Write the result to graph.node_attrs
        scores: Dict[int, float] = dict(enumerate(possibilities))
        graph.node_attrs[dst_attr] = scores
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
        degrees = X.sum(axis=0)[0]
        degrees[degrees == 0] = 1  # Avoid division by 0
        D2 = diags((1.0 / degrees), offsets=0).sqrt()
        S = alpha * D2.dot(X).dot(D2)
        return S

    @staticmethod
    def _build_base_matrix(X: spmatrix, labels: cupy.ndarray, alpha: float, n_classes: int) -> cupy.ndarray:
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
    def _propagate_converged(
            P: spmatrix,
            F: cupy.ndarray,
            B: cupy.ndarray,
            max_iters: int,
            force_iter=False) -> Tuple[cupy.ndarray, bool]:
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
            bool: If true, the result is convergent.

        """
        convergent = False
        if not force_iter:
            cupyx.seterr(linalg='raise')
            n_samples = P.shape[0]
            X = cupy.eye(n_samples, dtype=cupy.float32) - P
            try:
                # If matrix (I - P) is invertible, then the propagation series is convergent.
                F = cupy.linalg.inv(X).dot(B)
                convergent = True
            except LinAlgError:
                convergent = False
        if not convergent:
            remaining_iter = max_iters
            while remaining_iter > 0:
                F = P.dot(F) + B
                remaining_iter -= 1
        return F, convergent

    @staticmethod
    def _compute_positive_prob(F: cupy.ndarray) -> cupy.ndarray:
        """Predict labels by learnt label matrix

        Args:
            F : cupy array, shape = [n_samples, 2]
                Learnt (resulting) label matrix

        Returns:
            cupy.ndarray: The probability of label 'True', shape = [n_samples].

        """

        tan = F[:, 1] / F[:, 0]
        arctan = cupy.arctan(tan, out=tan)
        prob = arctan / (cupy.pi * 0.5)
        prob[prob <= cupy.finfo(cupy.float32).eps] = 0
        # predicted_label_ids = cupy.argmax(F, axis=1)
        return prob
