# 白名单过滤模块——Kubernetes方式部署

## 前置条件

#### 部署环境依赖

- Kubernetes集群: 版本1.17.x

- Kafka集群: 版本2.5.x

#### 本地软件依赖

- kubectl：Kubernetes集群管理命令行工具，版本1.17.x

## 参数配置

[config.yaml](config.yaml)文件定义了本模块所用到的配置参数，旁边附有注释的参数是用户可以修改的配置。

各参数含义见配置文件[config.yaml](config.yaml)

## 部署

进入本模块的部署文件目录

```shell
cd deploy/kubernetes/modules/whitelist-filter
```

部署

```shell
kubectl apply -f config.yaml
kubectl apply -f pvc.yaml
kubectl apply -f deployment.yaml
kubectl apply -f svc.yaml
```

