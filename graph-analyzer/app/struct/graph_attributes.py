from __future__ import annotations

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    import numpy as np


class ComponentAttr:
    def __init__(
            self,
            id: int,
            components: np.ndarray
    ):
        self._id = id
        self._components = components

    @property
    def id(self):
        return self._id

    @property
    def components(self):
        return self._components
