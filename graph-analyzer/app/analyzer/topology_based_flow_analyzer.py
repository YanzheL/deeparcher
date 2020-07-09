from typing import *

import numpy as np
from scipy.sparse import *

from app.analyzer.interface import GraphAnalyzer
from app.struct import Graph
from app.util.misc import extract_bool_attr_ids


class TopologyBasedFlowAnalyzer(GraphAnalyzer):
    """
    功能：算法执行过程
    （1）分别初始化好流和坏流的声誉列向量
    （2）分别用声誉列向量乘以图的邻接矩阵，传播有边相连结点之间的声誉信息
    （3）根据好流相对于坏流的重要程度系数W计算最终声誉评分（此时评分在w到1之间）,并保持原有标签的信息
    （4）归一化操作（将评分投影到0到1之间）
    （5）根据判断标签类别的阈值，得到最后的标签结果
    """

    def analyze(self, graph: Graph, ctx: dict, n_iters=5, legal_weight: float = -1,
                attr_name: str = 'black_or_white') -> Graph:
        black_node_ids, white_node_ids = extract_bool_attr_ids('black_or_white', graph.node_attrs)
        adj_triu = triu(graph.adj, k=1)  # 获取除对角线以外的上三角
        M = adj_triu + adj_triu.T  # 写成对称邻接矩阵
        M = (M - M.min()) / (M.max() - M.min())  # 矩阵归一化
        # 初始化两个结点数量维零列向量，用来存储好坏流信誉度传播时的评分
        P = np.zeros((graph.nodes, 2), dtype=np.float32)  # shape = (nodes, 2)
        v_bad = P[:, 0]
        v_good = P[:, 1]
        # 各自赋予初始标签情况，只要是已知标签都赋为1，其余都赋为0（此时0，1不代表恶意、良性，仅代表有标签）
        v_bad[black_node_ids] = 1.0
        v_good[white_node_ids] = 1.0
        # 用矩阵乘法传播有边相连结点之间的声誉信息
        for i in range(n_iters):
            # 用矩阵乘法传播有边相连结点之间的声誉信息
            P[:, :] = P + M.dot(P)
            # 进行声誉归一化操作
            v_good = (v_good - v_good.min()) / (v_good.max() - v_good.min())
            v_bad = (v_bad - v_bad.min()) / (v_bad.max() - v_bad.min())

        # 好坏流根据各自重要性得出的最终声誉评分（此时评分在w到1之间）
        v_result = v_bad + legal_weight * v_good  # shape = (nodes,)

        # 保持原有标签的信息
        v_result[white_node_ids] = legal_weight  # 如果是良性结点，则V_bad对应声誉为0，因此最终声誉为legal_weight
        v_result[black_node_ids] = 1  # 如果是恶意结点，则V_good对应声誉为0，因此最终声誉为1
        # 归一化操作（将评分投影到0到1之间）,并从np.array转化为字典类型
        rep = (v_result - v_result.min()) / (v_result.max() - v_result.min())
        scores: Dict[int, float] = {k: v for k, v in enumerate(rep)}
        graph.node_attrs['tbf_prob'] = scores
        return graph

# `    @staticmethod
#     def _normalize(s1: np.ndarray, v1: np.ndarray, v2: np.ndarray) -> NoReturn:
#         avg_2 = np.sum(v2[s1]) / np.count_nonzero(v2[s1])
#         v1[v1 > 1] = 1.0
#         v2[s1] = 1.0
#         v1[v1 > 1 - avg_2] = 1.0
