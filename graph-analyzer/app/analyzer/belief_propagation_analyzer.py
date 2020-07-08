from typing import *

import networkx as nx
import numpy as np
import app.analyzer.factorgraph as fg
from app.analyzer.interface import GraphAnalyzer
from app.struct import Graph
from app.util.misc import extract_bool_attr_ids


class BeliefPropagationAnalyzer(GraphAnalyzer):

    def analyze(self, graph: Graph, ctx: dict, **runtime_configs) -> Union[Graph, List[Graph], None]:
        set_bad, set_good = extract_bool_attr_ids('black_or_white', graph.node_attrs)
        scores: Dict[int, float] = {}
        nx_graph = nx.Graph(incoming_graph_data=graph.adj)
        # 黑白名单结点列表读取
        T = max_iter  # 迭代次数T
        D = d  # 推断概率差值D
        g = fg.Graph()
        # 写入结点
        for n in nx_graph.nodes():
            g.rv(str(n), 2)

        # 根据传入标签添加信息
        # 添加显示信息
        for good in set_good:
            g.factor([str(good)], potential=np.array([0.99, 0.01]))  # 良性
        for bad in set_bad:
            g.factor([str(bad)], potential=np.array([0.01, 0.99]))  # 恶意
        # 添加隐式信息
        for e in nx_graph.edges():
            g.factor([str(e[0]), str(e[1])], potential=np.array([
                [0.5 + self.D, 0.5 - self.D],
                [0.5 - self.D, 0.5 + self.D],
            ]))

        # 进行置信传播
        g.lbp(init=True, normalize=True, max_iters=self.T, progress=False)
        tuples = g.print_rv_marginals()
        result_belief = {}
        for n in tuples:
            belief = list(n[1])
            name = n[0].name
            result_belief[name] = belief[0] / (belief[0] + belief[1])
        scores = result_belief
        graph.node_attrs['bp_prob'] = scores
