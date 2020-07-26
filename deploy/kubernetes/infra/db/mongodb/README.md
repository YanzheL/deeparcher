# MongoDB集群——Kubernetes方式部署

## 前置条件

#### 部署环境依赖

- Kubernetes集群: 版本1.17.x

#### 本地软件依赖

- kubectl：Kubernetes集群管理命令行工具，版本1.17.x
- [helm](https://helm.sh/): 版本3.x

## 参数配置

[values.yaml](values.yaml)文件定义了集群所用到的配置参数。

## 部署

进入本模块的部署文件目录

```shell
cd deploy/kubernetes/infra/db/mongodb
```

部署

```shell
./deploy.sh
```

