from typing import *

import numpy as np


class ComponentAttr:
    def __init__(
            self,
            parent_id: int,
            component_id: int,
            components: np.ndarray
    ):
        self._parent_id = parent_id
        self._component_id = component_id
        self._components = components

    @property
    def parent_id(self):
        return self._parent_id

    @property
    def component_id(self):
        return self._component_id

    @property
    def components(self):
        return self._components


class GraphElementsAttrMap:
    def __init__(
            self,
            name: str,
            target: str,
            data: Dict[int, object]
    ):
        self._name = name
        self._target = target
        self._data = data

    @property
    def name(self):
        return self._name

    @property
    def target(self):
        return self._target

    @property
    def data(self):
        return self._data


class NodeAttrMap(GraphElementsAttrMap):
    def __init__(
            self,
            name: str,
            data: Dict[int, object]
    ):
        super().__init__(name, 'NODE', data)


class EdgeAttrMap(GraphElementsAttrMap):
    def __init__(
            self,
            name: str,
            data: Dict[int, object]
    ):
        super().__init__(name, 'EDGE', data)
