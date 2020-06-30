from abc import ABCMeta, abstractmethod

from app.model import Graph


class GraphReader(metaclass=ABCMeta):

    @abstractmethod
    def read(self, *args, **kwargs) -> Graph:
        raise NotImplementedError("Not implemented by subclasses.")

    @abstractmethod
    @property
    def type(self):
        pass
