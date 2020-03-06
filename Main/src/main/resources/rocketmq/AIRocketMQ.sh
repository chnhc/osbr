#!/bin/bash

# yum install net-tools

# 创建存储路径
# mkdir /usr/data/rocketmq
# mkdir /usr/data/rocketmq/store
# mkdir /usr/data/rocketmq/store/commitlog
# mkdir /usr/data/rocketmq/store/consumequeue
# mkdir /usr/data/rocketmq/store/index
# mkdir -p /usr/data/rocketmq/logs

########################### 三主节点 Master ###########################
# 修改配置
# cd /usr/local/rocketmq/conf && sed -i 's#${user.home}#/usr/data/rocketmq#g' *.xml
# vi /usr/local/rocketmq/conf/2m-noslave/broker-a.properties
# vi /usr/local/rocketmq/conf/2m-noslave/broker-b.properties
# 多Master-Rocketmq.properties

# 修改启动脚本参数
# vi /usr/local/rocketmq/bin/runbroker.sh
# JAVA_OPT="${JAVA_OPT} -server -Xms1g -Xmx1g -Xmn512m"


# 端口
# firewall-cmd --permanent --add-port=9876/tcp
# firewall-cmd --permanent --add-port=10911/tcp
# firewall-cmd --permanent --add-port=10912/tcp

# 重启防火墙
# firewall-cmd --reload


# 启动 所有的nameServer
# cd /usr/local/rocketmq/bin
# nohup sh mqnamesrv &

# 启动 broker
# nohup sh mqbroker -c /usr/local/rocketmq/conf/2m-noslave/broker-a.properties >/dev/null 2>&1 &
# nohup sh mqbroker -c /usr/local/rocketmq/conf/2m-noslave/broker-b.properties >/dev/null 2>&1 &
# nohup sh mqbroker -c /usr/local/rocketmq/conf/2m-noslave/broker-c.properties >/dev/null 2>&1 &

# 查看日志 broker.log namesrv.log

# 关闭
# cd /usr/local/rocketmq/bin
# sh mqshutdown broker
# sh mqshutdown namesrv

# /etc/init.d/network restart

# mqadmin clusterList -n localhost:9876

########################### 单主双从-自动切换 DLedger  ###########################

# 多Master多slavesDLedger-Rocketmq.properties
# 配置
# cd /usr/local/rocketmq/conf/dledger/
# vi broker-n0.conf
# vi broker-n1.conf
# vi broker-n2.conf
# brokerId
# brokerRole
# dLegerSelfId


# 端口
# firewall-cmd --permanent --add-port=9876/tcp
# firewall-cmd --permanent --add-port=10911/tcp
# firewall-cmd --permanent --add-port=10912/tcp
# firewall-cmd --permanent --add-port=40911/tcp

# 重启防火墙
# firewall-cmd --reload


# 启动 所有的nameServer
# cd /usr/local/rocketmq/bin
# nohup sh mqnamesrv &

# 启动 broker
# nohup sh mqbroker -c /usr/local/rocketmq/conf/dledger/broker-n0.conf >/dev/null 2>&1 &
# nohup sh mqbroker -c /usr/local/rocketmq/conf/dledger/broker-n1.conf >/dev/null 2>&1 &
# nohup sh mqbroker -c /usr/local/rocketmq/conf/dledger/broker-n2.conf >/dev/null 2>&1 &

# cd /usr/local/rocketmq/bin
# ./mqadmin clusterList -n s206:9876

# 查看日志 broker.log namesrv.log

# 查看端口
# netstat -lnp|grep 40911
# 关闭
# cd /usr/local/rocketmq/bin
# sh mqshutdown broker
# sh mqshutdown namesrv



