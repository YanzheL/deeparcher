from typing import *

import networkx as nx
import numpy as np
from app.analyzer.interface import GraphAnalyzer
from app.struct import Graph
from app.util.misc import extract_bool_attr_ids
from app.analyzer.lgc import local_and_global_consistency


class LLGCAnalyzer(GraphAnalyzer):

    def analyze(self, graph: Graph, ctx: dict, alpha,max_iter) -> Union[Graph, List[Graph], None]:
        set_bad, set_good = extract_bool_attr_ids('black_or_white', graph.node_attrs)
        scores: Dict[int, float] = {}
        nx_graph = nx.Graph(incoming_graph_data=graph.adj)
        predicted = local_and_global_consistency(nx_graph, alpha=alpha, max_iter=max_iter)
        count = 0
        # 将list形式的预测结果转变为_scores={数字编号：信誉度,}
        for node in nx_graph.nodes:
            scores[node] = predicted[count]
            count += 1
        graph.node_attrs['llgc_prob'] = scores
