from abc import ABCMeta, abstractmethod
from typing import *

from app.model import Graph, NodeAttrMap, EdgeAttrMap


class GraphAnalyzer(metaclass=ABCMeta):
    def __init__(self, graph: Graph, **configs):
        self.graph = graph
        self.configs = configs

    def run(self, **runtime_configs) -> NoReturn:
        if self.is_finished() and not self.is_restartable():
            raise RuntimeError("This analyzer is finished and not restartable.")
        self._run(**runtime_configs)

    def reset(self) -> NoReturn:
        pass

    def is_finished(self) -> bool:
        return False

    def is_restartable(self) -> bool:
        return False

    @abstractmethod
    def _run(self, **runtime_configs) -> NoReturn:
        raise NotImplementedError("Not implemented by subclasses.")


class GraphAttrExtractor(metaclass=ABCMeta):

    @abstractmethod
    def get_node_attrs(self, **kwargs) -> List[NodeAttrMap]:
        pass

    @abstractmethod
    def get_edge_attrs(self, **kwargs) -> List[EdgeAttrMap]:
        pass
