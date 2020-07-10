from __future__ import annotations

from typing import TYPE_CHECKING, Dict

if TYPE_CHECKING:
    from app.struct import Graph

import numpy as np

from app.analyzer.interface import GraphAnalyzer


class NodeAttrMatcherAnalyzer(GraphAnalyzer):
    """Match the content of a node attribute with pre-configured true lists and false lists.

    Author:
        Yanzhe Lee <lee.yanzhe@yanzhe.org>

    """

    def __init__(self, true_list, false_list, exclude_common=True):
        """

        Args:
            true_list: array_like
                The attribute values which should be considered as True.
            false_list: array_like
                The attribute values which should be considered as False.
            exclude_common: bool, optional
                If True, common items will be excluded from both lists.

        """
        super().__init__()
        self._true_list = np.unique(true_list)
        self._false_list = np.unique(false_list)
        if exclude_common:
            common = np.intersect1d(self._true_list, self._false_list, assume_unique=True)
            if common.size > 0:
                self._true_list = np.setdiff1d(self._true_list, common, assume_unique=True)
                self._false_list = np.setdiff1d(self._false_list, common, assume_unique=True)

    def analyze(self, graph: Graph, ctx: dict, src_attr: str, dst_attr: str) -> Graph:
        """Match the content of a node attribute named `src_attr` with pre-configured true lists and false lists.

        Analyzed result will be written to graph.node_attrs[dst_attr] with type Dict[int, bool].
        For each (id,value) pair in graph.node_attrs[src_attr],
        if value in true_lists, graph.node_attrs[dst_attr][id] = True;
        if value in false_lists, graph.node_attrs[dst_attr][id] = False.

        Args:
            graph: The graph struct.
            ctx: Shared analyzer context.
            src_attr: Name of an existing string node attribute in the graph.
            dst_attr: Name of the output node attribute.

        Returns:
            Graph: An analyzed graph which contains an output node attribute.

        """
        src = graph.node_attrs[src_attr]
        dst_data: Dict[int, bool] = {}
        if len(src) == graph.nodes:
            values = np.array(
                [v for k, v in sorted(src.items())]
            )
            true_ids = np.intersect1d(values, self._true_list, return_indices=True)[1]
            false_ids = np.intersect1d(values, self._false_list, return_indices=True)[1]
            dst_data.update(map(lambda i: (i, True), true_ids))
            dst_data.update(map(lambda i: (i, False), false_ids))
        else:
            for node_id, value in src:
                if value in self._true_list:
                    dst_data[node_id] = True
                elif value in self._false_list:
                    dst_data[node_id] = False
        graph.node_attrs[dst_attr] = dst_data
        return graph
