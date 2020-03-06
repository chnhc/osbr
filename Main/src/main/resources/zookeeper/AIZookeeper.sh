#!/bin/bash

# 参考 Zookeeper入门.html


########################### 安装java1.8环境 ###########################
# 上传jdk-8u65-linux-x64.tar.gz 到 /usr/local 下
# 1、tar 开包
# tar -zxvf jdk-8u65-linux-x64.tar.gz
# 2、删除压缩包、创建软连接
# rm -f jdk-8u65-linux-x64.tar.gz
# ln -s jdk1.8.0_65 jdk
# 3、配置环境变量
# vi /etc/profile
# 追加：
#   #jdk 1.8
#   export JAVA_HOME=/usr/local/jdk
#   export PATH=$PATH:$JAVA_HOME/bin
# 4、生效配置
# source /etc/profile
# 5、检查java版本
# java -version



########################### 安装ZK ###########################

# 上传ZK包 apache-zookeeper-3.5.6-bin.tar.gz
# 1、tar 开
# tar -zxvf apache-zookeeper-3.5.6-bin.tar.gz
# 2、删除压缩包、创建软连接
# rm -f apache-zookeeper-3.5.6-bin.tar.gz
# ln -s apache-zookeeper-3.5.6-bin zookeeper
# 3、复制配置
# cp /usr/local/zookeeper/conf/zoo_sample.cfg /usr/local/zookeeper/conf/zoo.cfg
# 4、编辑zoo.cfg配置
# vi zoo.cfg
#   修改
#   dataDir=/usr/data/zookeeper
# 5、创建目录和打开端口
# mkdir /usr/data/zookeeper
# 端口 2181
# firewall-cmd --permanent --add-port=2181/tcp
# 重启防火墙
# firewall-cmd --reload
# 6、启动zk服务
# cd /usr/local/zookeeper/bin
# ./zkServer.sh start
# 7、连接到服务器
#		$>zkCli.sh -server 192.168.232.201:2181	//进入zk命令行
#		$zk]help			//查看帮助
#		$zk]quit			//退出
#		$zk]create /a tom		//
#		$zk]get /a			//查看数据
#		$zk]ls /			//列出节点
#		$zk]set /a tom		//设置数据
#		$zk]delete /a			//删除一个节点
#		$zk]rmr /a			//递归删除所有节点。
# 8、退出zk
# ./zkServer.sh stop


########################### 集群配置 ###########################
# 1、在 zoo.cfg 中，添加集群配置
# server.1=192.168.232.205:2888:3888
# server.2=192.168.232.206:2888:3888
# server.3=192.168.232.207:2888:3888
# 说明： server.A=B：C：D：其中 A 是一个数字，表示这个是第几号服务器；
# B 是这个服务器的 ip 地址；C 表示的是这个服务器与集群中的 Leader
# 服务器交换信息的端口；D 表示的是万一集群中的 Leader 服务器挂了，
# 需要一个端口来重新进行选举，选出一个新的 Leader，而这个端口就是
# 用来执行选举时服务器相互通信的端口。如果是伪集群的配置方式，
# 由于 B 都是一样，所以不同的 Zookeeper 实例通信端口号不能一样，
# 所以要给它们分配不同的端口号。
# 2、在每台主机的 dataDir 中添加myid,内容分别是1,2,3
# $>echo 1 > /usr/data/zookeeper/myid
# $>echo 2 > /usr/data/zookeeper/myid
# $>echo 3 > /usr/data/zookeeper/myid
# 3、开放端口 2888、3888
# 4、分别启动zk
# zkServer.sh start
# 5、查看每台服务器的状态
# zkServer.sh status

# 注意： 需将127.0.0.1删除
# vi /etc/hosts (删除第一行的127.0.0.1)
# 原因是与输入的地址发生冲入，所以拒绝连接






