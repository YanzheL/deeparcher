# Docker环境运行

## 前置条件

下述环境均经过测试。对于其他相似环境，也许能够运行。

#### 1. 硬件环境:

- 内存: 8GB
- GPU: NVIDIA GTX 1080Ti, NVIDIA GTX 2080

#### 2. 软件环境

- 操作系统: Ubuntu 18.04
- Docker: 版本 18.09 及以上
- [NVIDIA Docker Runtime](https://github.com/NVIDIA/nvidia-docker)

## 配置

所有配置参数在[application.yml](application.yml)中设置。各配置参数的含义可参考[application.yml](application.yml)中的注释。

## 运行

1. 拉取容器镜像 (校园网)

   ```shell
   sudo docker pull registry.hitnslab.com/dns-security/malicious-domain-rating/deeparcher/graph-analyzer:dev
   ```

2. 根据需要修改配置文件

   备份默认配置文件

   ```shell
   cp application.yml default.application.yml
   ```

   编辑配置文件

   ```shell
   vi application.yml # 或者通过图形界面编辑该文件
   ```

   注意，配置文件内部设置的文件路径是容器内部的路径，而不是主机上的路径。通常来说，主机上的数据目录挂在到容器内部的`/app/data`目录即可

3. 运行

   假设你的主机数据目录在`path/to/your/host/data`，配置文件在`/path/to/your/application.yml`

   ```shell
   sudo docker run \
     -it --rm --gpus all \
     -v /path/to/your/host/data:/app/data \
     -v /path/to/your/application.yml:/app/application.yml \
     registry.hitnslab.com/dns-security/malicious-domain-rating/deeparcher/graph-analyzer:dev
   ```
