# Kafka 流处理

本模块共包含如下几个子模块。

- 域名属性聚合模块
- 白名单过滤模块
- 图构建模块

各子模块集成到一个JAR格式的Java包中，通过配置文件切换模块的功能。

开发语言：Kotlin/Java

## 域名属性聚合模块

#### 功能

将存有被动DNS时间序列的Kafka消息队列按照完整域名（FQDN）为键，提取IP集合、CNAME集合，做聚合操作（GroupBy），得到以完整域名为唯一键的域名聚合属性表的更新事件流，输出到Kafka消息队列中。

该模块执行的是有状态的流操作。

#### 数据输入

**Kafka Topic:** raw.pdns

#### 数据输出

**Kafka Topic:** domain.aggregate.full

#### [部署文档](/deploy/kubernetes/modules/aggregator)

## 白名单过滤模块

#### 功能

根据预先配置的白名单文件，对输入的Kafka数据流按照完整域名做过滤操作。

该模块执行的是无状态的流操作。

#### 白名单匹配流程

1. 首先提取白名单中所有域名的顶级私有域，得到顶级私有域白名单。
2. 对于输入的Kafka消息队列中的每一个数据，提取完整域名对应的顶级私有域，判断该顶级私有域是否在顶级私有域白名单中，如果不在，则将该条数据推送到输出到消息队列中。

#### 数据输入

**Kafka Topic:** raw.pdns

#### 数据输出

**Kafka Topic:** raw.pdns.in-whitelist，raw.pdns.not-in-whitelist

#### [部署文档](/deploy/kubernetes/modules/whitelist-filter)

## 图构建模块

#### 功能

根据域名属性聚合模块得到的域名属性聚合表，将两个域名的IP集合、CNAME集合的公共元素个数作为初始的关联边权重，增量生成域名关联图的关联边，作为图的增量更新事件推送到输出的Kafka消息队列。

该模块会主动查询MongoDB数据库中存储的域名属性表中已有的图节点ID数据，为未分配图节点ID的完整域名分配图节点ID，并定期将新分配的图节点ID存入MongoDB数据库中，从而图中每一个节点的域名等其它属性值都能在MongoDB数据库中查询得到。

#### 数据输入

**Kafka Topic:** domain.aggregate.full

**MongoDB Collection:**  analysis.domain_attributes

#### 数据输出

**Kafka Topic:** graph.event.full

**MongoDB Collection:**  analysis.domain_attributes

#### [部署文档](/deploy/kubernetes/modules/graph-builder)