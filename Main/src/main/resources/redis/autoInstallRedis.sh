#!/bin/bash

# 参考 Redis集群详解.html

# 下载最新安装包 http://download.redis.io/releases
# 上传到 /usr/local/ 下

# 安装基础环境
# yum -y install gcc gcc-c++ kernel-devel（主要）
# yum -y install setup
# yum -y install perl
# yum -y install wget

# 1、tar 开包
# tar -zxvf redis-x.x.x.tar.gz
# 2、创建软连接
# ln -s redis-x.x.x/ redis
# 3、cd 到Redis解压包中
# cd redis
# 4、编译、安装Redis
# make
# make install

# 错误问题： make 出错
#     redis编译报致命错误：jemalloc/jemalloc.h：没有那个文件或目录
#     分配器allocator， 如果有MALLOC  这个 环境变量， 会有用这个环境变量的 去建立Redis。
#     而且libc 并不是默认的 分配器， 默认的是 jemalloc, 因为 jemalloc 被证明 有更少的 fragmentation problems 比libc。
#     但是如果你又没有jemalloc 而只有 libc 当然 make 出错。
#     所以加这么一个参数,运行如下命令：
#     make MALLOC=libc
#     然后执行 make install

# 启动单机redis
# 1、备份配置：
# cp redis.conf redis.conf.bak
# 2、配置redis.conf
# vi redis.conf
#   2.1、修改配置守护进程
#   daemonize yes
# 3、启动redis
# redis-server redis.conf
# 4、检查是否启动服务
# ps aux|grep redis
# 5、客户端本机连接
# redis-cli （本机即可）
# （远程其他）redis-cli -h 192.168.25.153（ip地址） -p 6379（宽口号）
# 6、关闭redis
# 先登录到redis
# xxx.xxx.xxx.xxx:6379> SHUTDOWN
# 或者直接执行 redis-cli shutdown
# 也可以直接杀死进程
# 7、如果有密码登录
# 首先redis-cli到对应的主机上
# xxx.xxx.xxx.xxx:6379> auth 密码 ( 即可密码登录 )
# 或者 （远程其他）redis-cli -h 192.168.25.153（ip地址） -p 6379（宽口号） -a 1235 (密码)


# 修改内存配置
# vi /etc/sysctl.conf
# vm.overcommit_memory=1
# 配置生效
# sysctl -p


# 删除注释行和空行
# $ grep -v '^#' xxx.conf | grep -v '^$' > xxx.conf


########################### 主从模式搭建 ###########################
# 使用主从配置.conf
# 根据配置修改内容即可：主要配置以下
#   bind 192.168.232.205                  # 监听ip，多个ip用空格分隔
#   protected-mode yes                    #保护模式：no 外网可以直接连接；yes 需要bind ip 或者设置密码
#   daemonize yes                         #允许后台启动
#   logfile "/usr/local/redis/redis.log"  #日志路径
#   dir /usr/data/redis                   #数据库备份文件存放目录
#   appendonly yes                        #在/data/redis/目录生成appendonly.aof文件，将每一次写操作请求都追加到appendonly.aof 文件中
#   masterauth 123456                     #slave连接master密码，master可省略
#   requirepass 123456                    #设置master连接密码，slave可省略
#   replicaof 192.168.232.205 6379        #主机节点,slave设置

# 主从节点先后启动即可
# 查看是否主从连接成功
# redis-cli -h xxx.xxx.xxx.xxx -a 123456 (masterauth设置的密码)
# 节点信息查询
# xxx.xxx.xxx.xxx:6379> info replication


########################### 哨兵模式搭建 ###########################

# 说明注意：
# 主从模式的弊端就是不具备高可用性，当master挂掉以后，
# Redis将不能再对外提供写入操作，因此sentinel应运而生。
# sentinel中文含义为哨兵，顾名思义，它的作用就是监控redis集群的运行状况，特点如下：
#   1、sentinel模式是建立在主从模式的基础上，如果只有一个Redis节点，sentinel就没有任何意义
#   2、当master挂了以后，sentinel会在slave中选择一个做为master，并修改它们的配置文件，
#      其他slave的配置文件也会被修改，比如slaveof属性会指向新的master
#   3、当master重新启动后，它将不再是master而是做为slave接收新的master的同步数据
#   4、sentinel因为也是一个进程有挂掉的可能，所以sentinel也会启动多个形成一个sentinel集群
#   5、多sentinel配置的时候，sentinel之间也会自动监控
#   6、当主从模式配置密码时，sentinel也会同步将配置信息修改到配置文件中，不需要担心
#   7、一个sentinel或sentinel集群可以管理多个主从Redis，多个sentinel也可以监控同一个redis
#   8、sentinel最好不要和Redis部署在同一台机器，不然Redis的服务器挂了以后，sentinel也挂了

# 工作机制：
#   1、每个sentinel以每秒钟一次的频率向它所知的master，slave以及其他sentinel实例发送一个 PING 命令
#   2、如果一个实例距离最后一次有效回复 PING 命令的时间超过 down-after-milliseconds 选项所指定的值，
#      则这个实例会被sentinel标记为主观下线。
#   3、如果一个master被标记为主观下线，则正在监视这个master的所有sentinel要以每秒一次的频率确认master的确进入了主观下线状态
#   4、当有足够数量的sentinel（大于等于配置文件指定的值）在指定的时间范围内确认master的确进入了主观下线状态，
#      则master会被标记为客观下线
#   5、在一般情况下， 每个sentinel会以每 10 秒一次的频率向它已知的所有master，slave发送 INFO 命令
#   6、当master被sentinel标记为客观下线时，sentinel向下线的master的所有slave发送 INFO 命令的频率会从 10 秒一次改为 1 秒一次
#   7、若没有足够数量的sentinel同意master已经下线，master的客观下线状态就会被移除；
#      若master重新向sentinel的 PING 命令返回有效回复，master的主观下线状态就会被移除

# 当使用sentinel模式的时候，客户端就不要直接连接Redis，而是连接sentinel的ip和port，
# 由sentinel来提供具体的可提供服务的Redis实现，这样当master节点挂掉以后，sentinel就会感知并将新的master节点提供给使用者。
# sentinel 端口 ： 26379

# 日志中的事件
#      +reset-master ：主服务器已被重置。
#       +slave ：一个新的从服务器已经被 Sentinel 识别并关联。
#       +failover-state-reconf-slaves ：故障转移状态切换到了 reconf-slaves 状态。
#       +failover-detected ：另一个 Sentinel 开始了一次故障转移操作，或者一个从服务器转换成了主服务器。
#       +slave-reconf-sent ：领头（leader）的 Sentinel 向实例发送了 [SLAVEOF](/commands/slaveof.html) 命令，为实例设置新的主服务器。
#       +slave-reconf-inprog ：实例正在将自己设置为指定主服务器的从服务器，但相应的同步过程仍未完成。
#       +slave-reconf-done ：从服务器已经成功完成对新主服务器的同步。
#       -dup-sentinel ：对给定主服务器进行监视的一个或多个 Sentinel 已经因为重复出现而被移除 —— 当 Sentinel 实例重启的时候，就会出现这种情况。
#       +sentinel ：一个监视给定主服务器的新 Sentinel 已经被识别并添加。
#       +sdown ：给定的实例现在处于主观下线状态。
#       -sdown ：给定的实例已经不再处于主观下线状态。
#       +odown ：给定的实例现在处于客观下线状态。
#       -odown ：给定的实例已经不再处于客观下线状态。
#       +new-epoch ：当前的纪元（epoch）已经被更新。
#       +try-failover ：一个新的故障迁移操作正在执行中，等待被大多数 Sentinel 选中（waiting to be elected by the majority）。
#       +elected-leader ：赢得指定纪元的选举，可以进行故障迁移操作了。
#       +failover-state-select-slave ：故障转移操作现在处于 select-slave 状态 —— Sentinel 正在寻找可以升级为主服务器的从服务器。
#       no-good-slave ：Sentinel 操作未能找到适合进行升级的从服务器。Sentinel 会在一段时间之后再次尝试寻找合适的从服务器来进行升级，又或者直接放弃执行故障转移操作。
#       selected-slave ：Sentinel 顺利找到适合进行升级的从服务器。
#       failover-state-send-slaveof-noone ：Sentinel 正在将指定的从服务器升级为主服务器，等待升级功能完成。
#       failover-end-for-timeout ：故障转移因为超时而中止，不过最终所有从服务器都会开始复制新的主服务器（slaves will eventually be configured to replicate with the new master anyway）。
#       failover-end ：故障转移操作顺利完成。所有从服务器都开始复制新的主服务器了。
#       +switch-master ：配置变更，主服务器的 IP 和地址已经改变。 这是绝大多数外部用户都关心的信息。
#       +tilt ：进入 tilt 模式。
#       -tilt ：退出 tilt 模式。


# 配置注意：
# 哨兵模式即是主从模式升级版 -- redis.conf配置与主从一致
# 注意以下配置：
#     masterauth 123456                #slave连接master密码，哨兵模式需要
#     requirepass 123456               #设置master连接密码，哨兵模式需要
#     replicaof 192.168.232.205 6379   #主机节点,slave设置 , 默认主节点

# 先启动redis服务  redis-server redis.conf
# 再启动哨兵线程服务 /usr/local/bin/redis-sentinel /usr/local/redis/sentinel.conf



########################### Cluster模式搭建 ###########################



# 可更改配置
#   pidfile "/var/run/redis_7001.pid"
#   logfile "/usr/local/redis/cluster/redis_7001.log"
#   dir "/data/redis/cluster/redis_7001"
# 不需要配置
#   replicaof 192.168.30.129 6379 （不需要配置）
# 增加配置
#   cluster-enabled yes
#   cluster-config-file nodes_7001.conf
#   cluster-node-timeout 15000

# 先启动每个服务
# redis-server /usr/local/redis/clusterRedis.conf

# 先开启redis端口 port + 10000 的端口
# 例如：port = 6379 ; 那么需要打开 集群线路端口 = 16379

# redis低版本需要安装ruby
# 请自行查阅对应html
# 创建集群
# redis-cli -a 123456 --cluster create 192.168.232.205:6379 192.168.232.208:6379 192.168.232.206:6379 192.168.232.209:6379 192.168.232.207:6379 192.168.232.210:6379 --cluster-replicas 1


# 连接 redis-cli -c(集群模式) -h 192.168.232.205 -a 123456
# 集群状态
# xxx.xxx.xxx.xxx:6379> CLUSTER INFO
# 列出节点信息
# xxx.xxx.xxx.xxx:6379> CLUSTER NODES
























