from typing import *

import numpy as np

from app.analyzer.interface import GraphAnalyzer, GraphAttrExtractor
from app.struct import NodeAttrMap, Graph
from app.util.misc import load_blacklist, load_whitelist


class TopologyBasedFlowAnalyzer(GraphAnalyzer, GraphAttrExtractor):

    def __init__(self, graph: Graph, whitelist: str, blacklist: str):
        super(GraphAnalyzer).__init__(graph)
        self.whitelist: np.ndarray = load_whitelist(whitelist, self.logger)
        self.blacklist: np.ndarray = load_blacklist(blacklist, self.logger)
        self._scores: Dict[int, float] = {}
        self._finished = False

    def _run(self, **runtime_configs) -> NoReturn:
        # TODO: Implement the algorithm here
        self._finished = True

    def is_finished(self) -> bool:
        return self._finished

    def get_node_attrs(self, **kwargs) -> List[NodeAttrMap]:
        return [NodeAttrMap('tbf_prob', self._scores)]
