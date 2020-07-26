# Kafka集群——Kubernetes方式部署

以下教程仅仅作为概念性流程示范，实际操作时请仔细学习本目录下各配置文件的含义和依赖的第三方软件文档，并对配置文件做适当修改，不建议从零开始部署。直接应用下述教程将产生无法预料到的后果。

## 前置条件

#### 部署环境依赖

- Kubernetes集群: 版本1.17.x

#### 本地软件依赖

- kubectl：Kubernetes集群管理命令行工具，版本1.17.x
- helm: 版本3.x

## 部署

进入本模块的部署文件目录

```shell
cd deploy/kubernetes/infra/db/kafka
```

部署kafka-operator, 对应的配置文件为[kafka-operator/values.yaml](kafka-operator/values.yaml)

```shell
cd kafka-operator
./deploy.sh
cd ..
```

部署kafka集群, 对应的配置文件为[cluster/kafka.yaml](kafka-operator/values.yaml)

```shell
cd cluster
kubectl apply -f kafka.yaml
cd ..
```

创建Kafka消息队列(Topic)

```shell
kubectl apply -f topics.yaml
```

创建Kafka用户

```shell
kubectl apply -f users.yaml
```

了解更多配置相关的信息，请参考 [Kafka Operator官方文档](https://strimzi.io/docs/latest/)
