from __future__ import annotations

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from app.struct.graph import Graph

from abc import ABCMeta, abstractmethod


class GraphReader(metaclass=ABCMeta):

    @abstractmethod
    def read(self, *args, **kwargs) -> Graph:
        raise NotImplementedError("Not implemented by subclasses.")

    @abstractmethod
    @property
    def type(self):
        pass
