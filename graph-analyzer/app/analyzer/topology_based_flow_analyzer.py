from typing import *

import numpy as np

from app.model import NodeAttrMap, Graph
from . import GraphAnalyzer, GraphAttrExtractor


class TopologyBasedFlowAnalyzer(GraphAnalyzer, GraphAttrExtractor):

    def __init__(self, graph: Graph, **configs):
        super(GraphAnalyzer).__init__(graph, **configs)
        self.whitelist: np.ndarray = self.configs['whitelist']
        self.blacklist: np.ndarray = self.configs['blacklist']
        self._scores: Dict[int, float] = {}
        self._finished = False

    def _run(self, **runtime_configs) -> NoReturn:
        # TODO: Implement the algorithm here
        self._finished = True

    def is_finished(self) -> bool:
        return self._finished

    def get_node_attrs(self, **kwargs) -> List[NodeAttrMap]:
        return [NodeAttrMap('tbf_prob', self._scores)]
