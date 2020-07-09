from abc import ABCMeta, abstractmethod
from typing import *

from app.struct import Graph
from app.util.logger_router import LoggerRouter


class GraphAnalyzer(metaclass=ABCMeta):

    def __init__(self):
        self.logger = LoggerRouter().get_logger(__name__)

    @abstractmethod
    def analyze(self, graph: Graph, ctx: dict, **runtime_configs) -> Union[Graph, List[Graph], None]:
        pass

    def reset(self) -> NoReturn:
        pass

    def exception_caught(self, e: Exception):
        raise e
