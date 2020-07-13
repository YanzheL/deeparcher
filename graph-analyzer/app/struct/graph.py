from __future__ import annotations

from typing import TYPE_CHECKING, List, Optional, Dict

if TYPE_CHECKING:
    from app.struct.graph_attributes import ComponentAttr

import numpy as np
from scipy.sparse import spmatrix, tril, triu

from app.struct.base import MutableBean


class Graph(MutableBean):
    def __init__(
            self,
            id: int,
            nodes: int,
            edges: int,
            adj: spmatrix,
            directed: bool,
            connected: bool,
            unweighted: bool,
            parent_id: int = 0,
            node_id_remap: Optional[np.ndarray] = None,
            component_attrs: List[ComponentAttr] = None,
            node_attrs: Dict[str, Dict[int, object]] = None,
            edge_attrs: Dict[str, Dict[int, object]] = None,
            attributes: Dict[str, object] = None
    ):
        super().__init__()
        self._id = id
        self._nodes = nodes
        self._edges = edges
        self._adj = adj
        self._directed = directed
        self._connected = connected
        self._unweighted = unweighted
        self._parent_id = parent_id
        self._node_id_remap = node_id_remap
        self._component_attrs = component_attrs if component_attrs is not None else []
        self._node_attrs = node_attrs if node_attrs is not None else {}
        self._edge_attrs = edge_attrs if edge_attrs is not None else {}
        self._attributes = attributes if attributes is not None else {}
        self._modified = False
        self.meta = {}
        self._finalize()

    @property
    def id(self):
        return self._id

    @property
    def nodes(self):
        return self._nodes

    @nodes.setter
    def nodes(self, value):
        self._set_property('nodes', value)

    @property
    def edges(self):
        return self._edges

    @edges.setter
    def edges(self, value):
        self._set_property('edges', value)

    @property
    def adj(self):
        return self._adj

    @property
    def directed(self):
        return self._directed

    @directed.setter
    def directed(self, value):
        self._set_property('directed', value)

    @property
    def connected(self):
        return self._connected

    @connected.setter
    def connected(self, value):
        self._set_property('connected', value)

    @property
    def unweighted(self):
        return self._unweighted

    @unweighted.setter
    def unweighted(self, value):
        self._set_property('unweighted', value)

    @property
    def parent_id(self):
        return self._parent_id

    @parent_id.setter
    def parent_id(self, value):
        self._set_property('parent_id', value)

    @property
    def node_id_remap(self):
        return self._node_id_remap

    @node_id_remap.setter
    def node_id_remap(self, value):
        self._set_property('node_id_remap', value)

    @property
    def component_attrs(self):
        return self._component_attrs

    @property
    def node_attrs(self):
        return self._node_attrs

    @property
    def edge_attrs(self):
        return self._edge_attrs

    def __repr__(self):
        return f"Graph[parent_id={self._parent_id},id={self.id},nodes={self._nodes},edges={self._edges}]"

    def __eq__(self, other: Graph):
        if self._id != other._id:
            return False
        if self._nodes != other.nodes:
            return False
        if self._edges != other._edges:
            return False
        if (self._adj != other._adj).nnz == 0:
            return False
        if self._directed != other._directed:
            return False
        if self._connected != other._connected:
            return False
        if self._unweighted != other._unweighted:
            return False
        if self._parent_id != other._parent_id:
            return False
        if not np.array_equal(self._node_id_remap, other._node_id_remap):
            return False

        # TODO (Yanzhe Lee): Check equality of attributes.

    def _finalize(self):
        if not (self._nodes == self._adj.shape[0] == self._adj.shape[1]):
            raise ValueError("Graph nodes do not match adjacent matrix's shape.")
        if self._adj.count_nonzero() == 0:
            raise ValueError("Graph adjacent list has no non-zero value.")
        if not self._directed:
            tl = tril(self._adj, k=-1)
            if tl.count_nonzero() > 0:
                self._adj = triu(self._adj) + tl.transpose()
        if self._edges != self._adj.count_nonzero():
            raise ValueError("Number of non-zero values in adjacent list does not match the number of edges.")
        if self._unweighted:
            nz_rows, nz_cols = self._adj.nonzero()
            self._adj = self._adj.tocsr()
            self._adj[nz_rows, nz_cols] = 1.0
