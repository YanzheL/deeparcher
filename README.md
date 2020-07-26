# 分布式恶意域名挖掘与评级系统

## 基础设施部署

- Kubernetes 集群

- [Kafka 集群](deploy/kubernetes/infra/kafka/)

- [MongoDB 集群](deploy/kubernetes/infra/db/mongodb/)

- [MariaDB 单节点数据库 (可选)](deploy/kubernetes/infra/db/mariadb/)

## [模块间接口数据结构](common/src/main/kotlin/com/hitnslab/dnssecurity/deeparcher/api/proto/)

## 各模块文档

- [被动DNS日志导入模块](pdns-data-loader/)

- [Kafka 流处理](stream/)

  - 域名属性聚合模块

  - 白名单过滤模块

  - 图构建模块

- [图计算模块](graph-analyzer/)
