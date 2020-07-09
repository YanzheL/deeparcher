from typing import *

from app.analyzer.interface import GraphAnalyzer
from app.struct import Graph
from app.util.misc import extract_bool_attr_ids


class BeliefPropagationAnalyzer(GraphAnalyzer):

    def analyze(self, graph: Graph, ctx: dict, **runtime_configs) -> Union[Graph, List[Graph], None]:
        black_node_ids, white_node_ids = extract_bool_attr_ids('black_or_white', graph.node_attrs)
        scores: Dict[int, float] = {}
        # TODO: Implement it here.
        graph.node_attrs['bp_prob'] = scores
