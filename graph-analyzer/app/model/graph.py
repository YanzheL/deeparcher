import numpy as np
from scipy.sparse import spmatrix

from . import ComponentAttr, GraphElementsAttrMap


class Graph:
    def __init__(
            self,
            id: int,
            nodes: int,
            edges: int,
            adj: spmatrix,
            directed: bool,
            connected: bool,
            unweighted: bool,
            node_id_remap: np.ndarray,
            component_attrs: ComponentAttr = None,
            node_attrs: GraphElementsAttrMap = None,
            edge_attrs: GraphElementsAttrMap = None

    ):
        self._id = id
        self._nodes = nodes
        self._edges = edges
        self._adj = adj,
        self._directed = directed
        self._connected = connected
        self._unweighted = unweighted
        self._node_id_remap = node_id_remap
        self._component_attrs = component_attrs
        self._node_attrs = node_attrs
        self._edge_attrs = edge_attrs

    @property
    def id(self):
        return self._id

    @property
    def nodes(self):
        return self._nodes

    @property
    def edges(self):
        return self._edges

    @property
    def adj(self):
        return self._adj

    @property
    def directed(self):
        return self._directed

    @property
    def connected(self):
        return self._connected

    @property
    def unweighted(self):
        return self._unweighted

    @property
    def node_id_remap(self):
        return self._node_id_remap

    @property
    def component_attrs(self):
        return self._component_attrs

    @property
    def node_attrs(self):
        return self._node_attrs

    @property
    def edge_attrs(self):
        return self._edge_attrs
