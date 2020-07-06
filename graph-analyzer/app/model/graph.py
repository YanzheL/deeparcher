from typing import *

import numpy as np
from scipy.sparse import spmatrix

from app.model.base import MutableBean
from app.model.graph_attributes import ComponentAttr, GraphElementsAttrMap


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
            node_id_remap: Optional[np.ndarray] = None,
            component_attrs: List[ComponentAttr] = None,
            node_attrs: List[GraphElementsAttrMap] = None,
            edge_attrs: List[GraphElementsAttrMap] = None

    ):
        super().__init__()
        self._id = id
        self._nodes = nodes
        self._edges = edges
        self._adj = adj,
        self._directed = directed
        self._connected = connected
        self._unweighted = unweighted
        self._node_id_remap = node_id_remap
        self._component_attrs = component_attrs if component_attrs is not None else []
        self._node_attrs = node_attrs if node_attrs is not None else []
        self._edge_attrs = edge_attrs if edge_attrs is not None else []
        self._modified = False

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
