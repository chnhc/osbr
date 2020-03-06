#!/bin/bash

# 预先环境
# zookeeper 集群搭建
# Java 8+ (Apache Storm 2.x is tested through travis ci against a java 8 JDK)
# Python 2.6.6 (Python 3.x should work too, but is not tested as part of our CI enviornment)

# 下载 apache-storm-2.1.0.tar.gz
# tar -zxvf /usr/local/software/apache-storm-2.1.0.tar.gz -C /usr/local
# ln -s /usr/local/apache-storm-2.1.0 /usr/local/storm

# 配置环境变量
# vi /etc/profile
# 追加：
#   # storm
#   export STORM_HOME=/usr/local/storm
#   export PATH=$PATH:$STORM_HOME/bin
# 生效配置
# source /etc/profile

# 修改配置 conf/storm.yml
#  storm.zookeeper.servers:
#        - "s205"
#        - "s206"
#        - "s207"
#
#  nimbus.seeds: ["s205"]
#
#  storm.local.dir: "/usr/data/storm"
#
#  ui.port: 18080
#  supervisor.slots.ports:
#        - 6700
#        - 6701
#        - 6072
#        - 6073

# 创建 /usr/data/storm
# mkdir /usr/data/storm

# 端口 6700
# firewall-cmd --permanent --add-port=18080/tcp
# firewall-cmd --permanent --add-port=3771/tcp
# firewall-cmd --permanent --add-port=3772/tcp
# firewall-cmd --permanent --add-port=3773/tcp
# firewall-cmd --permanent --add-port=3774/tcp
# firewall-cmd --permanent --add-port=6700/tcp
# firewall-cmd --permanent --add-port=6701/tcp
# firewall-cmd --permanent --add-port=6072/tcp
# firewall-cmd --permanent --add-port=6073/tcp
# firewall-cmd --permanent --add-port=8000/tcp
# 重启防火墙
# firewall-cmd --reload

# storm 启动
# 主节点启动
# storm nimbus &

# 处理节点
# storm supervisor &

# UI启动
# storm ui &















