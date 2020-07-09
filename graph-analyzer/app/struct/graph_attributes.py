import numpy as np


class ComponentAttr:
    def __init__(
            self,
            component_id: int,
            components: np.ndarray
    ):
        self._component_id = component_id
        self._components = components

    @property
    def component_id(self):
        return self._component_id

    @property
    def components(self):
        return self._components
