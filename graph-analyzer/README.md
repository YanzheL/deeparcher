# 域名关联图分析模块

## 简介

### 功能

本模块负责在现有的域名关联图文件上运行各类算法，实现对图节点特征的提取。

已实现的算法有

- Local and Global Consistency

  算法原理基于`networkx.algorithms.node_classification.lgc.local_and_global_consistency`。本模块重新实现了该算法，实现了GPU加速和传播优化。

  实现类: [LLGCAnalyzer](app/analyzer/llgc_analyzer.py)

- Topology Based Flow

  实现类: [TopologyBasedFlowAnalyzer](app/analyzer/topology_based_flow_analyzer.py)

- Path Based Inference

  实现类: [PathBasedInferenceAnalyzer](app/analyzer/path_based_inference_analyzer.py)

- Belief Propagation

  基于`factorgraph`第三方库，暂未做任何优化，效率很低，不适合大型图。

  实现类: [BeliefPropagationAnalyzer](app/analyzer/belief_propagation_analyzer.py)

其他图操作:

- 连接子图划分

  GPU加速的弱连接子图(Weakly Connected Components)划分。

  实现类: [ConnectedComponentsAnalyzer](app/analyzer/connected_component_analyzer.py)

- 节点预标记

  实现类: [NodeAttrMatcherAnalyzer](app/analyzer/node_attr_matcher_analyzer.py)

### 内部数据流

1. 读取图文件，将其转换为一种特殊的稀疏图数据结构。
2. 根据用户配置，将原始图依次传给[GraphAnalyzer](app/analyzer/interface.py)的实现类做相应的分析操作。
3. 每一个算法实现类的输出都将写入到该图的节点或边的稀疏属性映射中。
4. 经过所有算法实现类分析过后的图的属性值将会被输出到指定的CSV文件中。

## 配置文件

所有配置参数在[application.yml](application.yml)中设置。各配置参数的含义可参考[application.yml](application.yml)中的注释。编辑配置文件时建议先备份默认配置文件[application.yml](application.yml)。

## 运行教程

- [Conda环境](docs/deploy/conda.md)
- [Docker环境](docs/deploy/docker.md)
