# 实验室GPU服务器预配置环境

## 连接服务器

假设你在实验室GPU服务器上现有的账号为`myuser`，密码为`mypassword`。若没有账号，联系李彦哲。

以下命令在你自己的本地电脑上的执行。Windows下使用[MobaXterm](https://mobaxterm.mobatek.net/)等ssh终端，Linux和macOS下用系统自带的终端。

1. 校园网环境

   ```shell
   ssh myuser@10.245.146.40
   ```

2. 校外环境

   ```shell
   ssh -p 65521 myuser@hitnslab.com
   ```

## 运行

以下命令在实验室GPU服务器上执行。

1. 进入本模块根目录

   ```shell
   cd /home/nist/deeparcher/graph-analyzer
   ```

2. 激活虚拟环境

   ```shell
   conda activate -p ./envs
   ```

3. 根据需要修改配置文件

   备份默认配置文件

   ```shell
   cp application.yml default.application.yml
   ```

   编辑配置文件

   ```shell
   vi application.yml # 或者通过图形界面编辑该文件
   ```

4. 运行

   ```shell
   python main.py
   ```
