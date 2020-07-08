from typing import *

import numpy as np

from scipy.sparse import *
from app.analyzer.interface import GraphAnalyzer
from app.struct import Graph
from app.util.misc import extract_bool_attr_ids


class TopologyBasedFlowAnalyzer(GraphAnalyzer):

    def analyze(self, graph: Graph, ctx: dict, n_iters,legal_weight) -> Union[Graph, List[Graph], None]:
        set_bad, set_good = extract_bool_attr_ids('black_or_white', graph.node_attrs)
        scores: Dict[int, float] = {}
        incoming_graph_data = graph.adj.copy()  # 获取邻接矩阵(上三角形式)
        up_triangle_matrix = triu(incoming_graph_data, k=1)  # 获取除对角线以外的上三角
        real_matrix = up_triangle_matrix + up_triangle_matrix.T  # 写成对称邻接矩阵
        real_matrix = (real_matrix - real_matrix.min()) / (
                real_matrix.max() - real_matrix.min())  # 矩阵归一化
        graph.node_attrs['tbf_prob'] = scores

    """
    功能：算法执行过程
    （1）分别初始化好流和坏流的声誉列向量
    （2）分别用声誉列向量乘以图的邻接矩阵，传播有边相连结点之间的声誉信息
    （3）根据好流相对于坏流的重要程度系数W计算最终声誉评分（此时评分在w到1之间）,并保持原有标签的信息
    （4）归一化操作（将评分投影到0到1之间）
    （5）根据判断标签类别的阈值，得到最后的标签结果
    """

    def tbf_apply(self) -> NoReturn:
        # 初始化两个结点数量维零列向量，用来存储好坏流信誉度传播时的评分
        v_bad = np.zeros((self.graph.nodes,), dtype=np.float32)
        v_good = np.zeros((self.graph.nodes,), dtype=np.float32)
        # 各自赋予初始标签情况，只要是已知标签都赋为1，其余都赋为0（此时0，1不代表恶意、良性，仅代表有标签）
        v_good[self.set_good] = 1
        v_bad[self.set_bad] = 1
        # 用矩阵乘法传播有边相连结点之间的声誉信息
        for i in range(0, self.n):
            # 用矩阵乘法传播有边相连结点之间的声誉信息
            v_bad = v_bad + self.real_matrix.dot(v_bad)
            v_good = v_good + self.real_matrix.dot(v_good)
            # 进行声誉归一化操作
            v_good = (v_good - v_good.min()) / (v_good.max() - v_good.min())
            v_bad = (v_bad - v_bad.min()) / (v_bad.max() - v_bad.min())
            # 传播过程中保持原有标签信息
            v_good[self.set_good] = 1
            v_bad[self.set_bad] = 1

        # 好坏流根据各自重要性得出的最终声誉评分（此时评分在w到1之间）
        v_result = v_bad + self.w * v_good

        # 保持原有标签的信息
        v_result[self.set_good] = self.w  # 如果是良性结点，则V_bad对应声誉为0，因此最终声誉为self.w
        v_result[self.set_bad] = 1  # 如果是良恶意结点，则V_good对应声誉为0，因此最终声誉为1
        # 归一化操作（将评分投影到0到1之间）,并从np.array转化为字典类型
        rep = (v_result - v_result.min()) / (v_result.max() - v_result.min())
        self._scores = {k: v for k, v in enumerate(rep)}
