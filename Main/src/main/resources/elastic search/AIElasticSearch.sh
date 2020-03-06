#!/bin/bash


# jdk 1.8 环境

# 下载 elasticsearch-7.5.2-linux-x86_64.tar.gz

# 解压
# tar -zxvf elasticsearch-7.5.2-linux-x86_64.tar.gz -C /usr/local
# ln -s /usr/local/elasticsearch-7.5.2/ /usr/local/elasticsearch
# 配置文件
# vi /usr/local/elasticsearch/config/elasticsearch.yml
# 虚拟机设置
# vi /config/jvm.options


# 创建目录
# mkdir -p /usr/data/elastic/search/data
# mkdir -p /usr/data/elastic/search/logs

# 创建用户
# groupadd elastic
# useradd esearch -p elastic
# useradd ekibana -p elastic
# chown -R ekibana:elastic /usr/local/kibana
# chown -R ekibana:elastic /usr/local/kibana-7.5.2-linux-x86_64
# chown -R esearch:elastic /usr/local/elasticsearch
# chown -R esearch:elastic /usr/local/elasticsearch-7.5.2
# chown -R esearch:elastic /usr/data/elastic/search

# max file descriptors [4096] for elasticsearch process is too low, increase to at least [65536]
# 意思是elasticsearch用户拥有的客串建文件描述的权限太低，知道需要65536个
# ulimit -Sn
# ulimit -Hn
# 修改/etc/security/limits.conf文件： 追加
# *               hard    nofile          65536
# *               soft    nofile          65536

# max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
# 意思是：elasticsearch用户拥有的内存权限太小了，至少需要262114。这个比较简单，也不需要重启，直接执行
# 追加以下内容：vm.max_map_count=655360保存后，执行：sysctl -p
# vi /etc/sysctl.conf
# 追加 vm.max_map_count=262144
# sysctl -p


# 启动
# su esearch
# ./elasticsearch -d

# 浏览器集群查看
# http://192.168.232.205:9200/_cat/nodes?v
# http://192.168.232.205:9200/_cluster/state?pretty

# 启动完毕，注释配置
# cluster.initial_master_nodes

















