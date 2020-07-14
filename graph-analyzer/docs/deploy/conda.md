# Conda环境

## 前置条件

下述环境均经过测试。对于其他相似环境，也许能够运行。

#### 1. 硬件环境

- 内存: 8GB
- GPU: NVIDIA GTX 1080Ti, NVIDIA GTX 2080

#### 2. 软件环境

- 操作系统: Ubuntu 18.04
- Conda: Anaconda 或 Miniconda, 版本 4.8 及以上
- NVIDIA GPU 驱动: 版本 430 及以上

## 运行

1. 下载本项目代码 (如果已下载，可跳过。需要校园网)

   ```shell
   git clone https://git.hitnslab.com/dns-security/malicious-domain-rating/deeparcher.git

   cd deeparcher
   ```

2. 进入本模块根目录

   ```shell
   cd graph-analyzer
   ```

3. 初始化虚拟conda环境到`./envs`子目录 (只需要运行一次，以后不用)

   ```shell
   conda env create -f environment.yml -p ./envs
   ```

4. 激活虚拟环境

   ```shell
   conda activate -p ./envs
   ```

5. 根据需要修改配置文件

   备份默认配置文件

   ```shell
   cp application.yml default.application.yml
   ```

   编辑配置文件

   ```shell
   vi application.yml # 或者通过图形界面编辑该文件
   ```

6. 运行

   ```shell
   python main.py
   ```
