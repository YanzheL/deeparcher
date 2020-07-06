from abc import ABCMeta, abstractmethod
from typing import *

from app.struct import Graph, NodeAttrMap, EdgeAttrMap
from app.util.logger_router import LoggerRouter


class GraphAnalyzer(metaclass=ABCMeta):

    def __init__(self, graph: Graph):
        self.graph = graph
        self.logger = LoggerRouter().get_logger(__name__)

    def run(self, **runtime_configs) -> NoReturn:
        self.logger.info('Begin running <{}>'.format(self.__class__))
        if self.is_finished():
            if not self.is_restartable():
                raise RuntimeError("This analyzer is finished and not restartable.")
            else:
                self.reset()
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
