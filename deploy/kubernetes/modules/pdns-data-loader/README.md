# DNS日志导入模块——Kubernetes方式部署

## 前置条件

#### 部署环境依赖

- Kubernetes集群: 版本1.17.x
- Kafka集群: 版本2.5.x
- MariaDB数据库: 版本10.x

#### 数据准备

- 存放原始DNS日志文件的目录以NFSv4共享卷的方式对外提供文件共享

  本项目的日志文件存放位置为`10.245.146.24:/data/general/pdns_logs`

#### 本地软件依赖

- kubectl：Kubernetes集群管理命令行工具，版本1.17.x

## 参数配置

[config.yaml](config.yaml)文件定义了本模块所用到的配置参数，旁边附有注释的参数是用户可以修改的配置。

各参数含义见配置文件[config.yaml](config.yaml)

## 部署

进入本模块的部署文件目录

```shell
cd deploy/kubernetes/modules/pdns-data-loader
```

部署

```shell
kubectl apply -f config.yaml
kubectl apply -f nfs-datasource.yaml
kubectl apply -f job.yaml
```

