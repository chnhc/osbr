#!/bin/bash

# MongoDB操作.html
# MongoDB重要配置.html
# MongoDB高可用的集群.html

# 下载软件包
# https://www.mongodb.com/download-center/community
# centos7 OS 选择 RHEL 7.0 Linux 64-bit x64 ; Package 选择 TGZ

# mongodb-linux-x86_64-rhel70-4.2.3.tgz
# 1、tar 开软件包
# tar -zxvf mongodb-linux-x86_64-rhel70-4.2.3.tgz -C /usr/local
# 2、创建软连接
# ln -s /usr/local/mongodb-linux-x86_64-rhel70-4.2.3 /usr/local/mongodb
# 3、配置环境变量
# vi /etc/profile
#   # mongodb
#   export MONGODB_HOME=/usr/local/mongodb
#   export PATH=$PATH:$MONGODB_HOME/bin
# 4、重载环境变量
# source /etc/profile
# 5、配置说明
# https://docs.mongodb.com/manual/reference/configuration-options/
:<<!
# 配置格式使用 yaml 配置格式
systemLog:
   # 默认0;组件日志级别,组件也可以单独设置级别
   # 如果级别设置为2 , 日志输出 D2 ...
   verbosity: <int>
   # 安静模式：减少输出量
   # 不推荐生产使用, 这可能造成跟踪问题困难
   quiet: <boolean>
   # 日志中输出相关解决方法
   traceAllExceptions: <boolean>
   # 默认user; 输出到syslog, 系统syslog必须支持你设置的值
   # 如果使用此项, systemLog.destination 必须设置为 syslog
   syslogFacility: <string>
   # 日志输出地址
   # 此地址不是标准输出或系统syslog 输出地址
   path: <string>
   # 默认false
   # true: 实例重新启动时, 日志将追加到现有的文件中
   # false: 实例重新启动时, 备份旧的日志,创建新的文件
   logAppend: <boolean>
   # 默认rename; logRotate执行行为, 支持 rename 和 reopen
   # rename: 重命名日志
   # reopen: 典型 Linux/Unix 日志 rotate 行为
   logRotate: <string>
   # 日志输出地址, 支持 file 和 syslog
   # file: 必须设置systemLog.path
   # 注意: 设置syslog时, 日志为守护线程的记录时的时间戳, 当系统过载时, 时间戳将不准确
   # 生产系统推荐设置为 file , 保证准确的的时间戳
   destination: <string>
   # 默认iso8601-local; 支持 ctime 、 iso8601-utc 和 iso8601-local
   # ctime: 格式例如: Wed Dec 31 18:17:54.811
   # iso8601-utc: 格式例如: 1970-01-01T00:00:00.000Z
   # iso8601-local: 格式例如: 1969-12-31T19:00:00.000-0500
   timeStampFormat: <string>
   # 组件日志设置
   component:
      # 访问控制
      accessControl:
         verbosity: <int>
      # 命令相关
      command:
         verbosity: <int>
      # 控制操作
      control:
         verbosity: <int>
      # 诊断数据收集
      # 3.2之后
      ftdc:
         verbosity: <int>
      # 地理空间分析
      geo:
         verbosity: <int>
      # 索引操作
      index:
         verbosity: <int>
      # 网络操作
      network:
         verbosity: <int>
      # 查询操作
      query:
         verbosity: <int>
      # 分片操作
      sharding:
         verbosity: <int>
      # 事务操作 4.0.2 之后
      transaction:
         verbosity: <int>
      # 复制相关
      replication:
         verbosity: <int>
         # 选举相关 4.2 之后
         election:
            verbosity: <int>
         # 心跳相关 3.6 之后
         heartbeats:
            verbosity: <int>
         # initialSync相关 4.2 之后
         initialSync:
            verbosity: <int>
         # 回滚相关 3.6 之后
         rollback:
            verbosity: <int>
      # 存储相关
      storage:
         verbosity: <int>
         # journaling 相关
         journal:
            verbosity: <int>
         # 恢复相关 4.0 之后
         recovery:
            verbosity: <int>
      # 写入操作
      write:
         verbosity: <int>
processManagement:
   # 默认false; 是否是守护进程, 默认不是
   # window 不支持此项
   fork: <boolean>
   # pid 存储地址
   pidFilePath: <string>
   # 加载时区数据库的完整路径。如果未提供此选项，则MongoDB将使用其内置时区数据库 /usr/share/zoneinfo
   timeZoneInfo: <string>
# 4.0 之后
cloud:
   monitoring:
      free:
         # 默认runtime; 可用于社区版, 支持 runtime 和 on 、off
         # runtime: 可以在运行时启用或禁用免费监视
         # on/off: 启动时设置, 运行时无法更改
         state: <string>
         # 可用于社区版, 描述环境上下文的可选标记
         tags: <string>
# 注意: 4.2 之后; 不支持ssl, 使用相同功能的tls
net:
   # 端口设置, 默认如下:
   # 27017: 不是  shard member 或者  config server member
   # 27018:  shard member
   # 27019:  config server member
   port: <int>
   # 默认: localhost
   # 查看详细设置: https://docs.mongodb.com/manual/reference/configuration-options/
   # 在绑定到非本地主机（例如公共访问）IP地址之前，请确保已保护群集不受未经授权的访问
   # 注意 bindIp 和 bindIpAll 只能设置一个
   bindIp: <string>
   # 默认false; 3.6 之后
   # 如果为true，实例将绑定到所有IPv4地址（即0.0.0.0）。如果net.ipv6:true开头，net.bindIpAll也将绑定到所有ipv6地址（即：：）
   # 注意 bindIp 和 bindIpAll 只能设置一个
   bindIpAll: <boolean>
   # 默认: 65536; 最大连接数
   maxIncomingConnections: <int>
   # 默认: true; 验证所有请求的数据格式
   wireObjectCheck: <boolean>
   # 默认: fasle,
   ipv6: <boolean>
   unixDomainSocket:
      # 默认: true, 在UNIX域套接字上启用或禁用侦听
      enabled: <boolean>
      # 默认: /tmp, UNIX套接字的路径
      pathPrefix: <string>
      # 默认: 0700, 设置UNIX域套接字文件的权限
      filePermissions: <int>
   # 4.2 之后
   tls:
      mode: <string>
      certificateKeyFile: <string>
      certificateKeyFilePassword: <string>
      certificateSelector: <string>
      clusterCertificateSelector: <string>
      clusterFile: <string>
      clusterPassword: <string>
      CAFile: <string>
      clusterCAFile: <string>
      CRLFile: <string>
      allowConnectionsWithoutCertificates: <boolean>
      allowInvalidCertificates: <boolean>
      allowInvalidHostnames: <boolean>
      disabledProtocols: <string>
      FIPSMode: <boolean>
   compression:
      compressors: <string>
   serviceExecutor: <string>
security:
   keyFile: <string>
   clusterAuthMode: <string>
   authorization: <string>
   transitionToAuth: <boolean>
   javascriptEnabled:  <boolean>
   redactClientLogData: <boolean>
   clusterIpSourceWhitelist:
     - <string>
   sasl:
      hostName: <string>
      serviceName: <string>
      saslauthdSocketPath: <string>
   enableEncryption: <boolean>
   encryptionCipherMode: <string>
   encryptionKeyFile: <string>
   kmip:
      keyIdentifier: <string>
      rotateMasterKey: <boolean>
      serverName: <string>
      port: <string>
      clientCertificateFile: <string>
      clientCertificatePassword: <string>
      clientCertificateSelector: <string>
      serverCAFile: <string>
   ldap:
      servers: <string>
      bind:
         method: <string>
         saslMechanisms: <string>
         queryUser: <string>
         queryPassword: <string>
         useOSDefaults: <boolean>
      transportSecurity: <string>
      timeoutMS: <int>
      userToDNMapping: <string>
      authz:
         queryTemplate: <string>
      validateLDAPServerConfig: <boolean>
setParameter:
   enableLocalhostAuthBypass: false
storage:
   # 默认: /data/db , 数据存储
   dbPath: <string>
   # 默认: true, 是否在下次启动时重建不完整的索引
   # 注意： 在版本4.0中更改：设置storage.indexBuildRetry不能与replication.replSetName一起使用。
   # 设置仅适用于mongod。
   # 不适用于使用内存存储引擎的mongod实例。
   indexBuildRetry: <boolean>

   journal:
      # 默认: true on 64-bit systems, false on 32-bit systems
      # 启用或禁用耐久性日志以确保数据文件保持有效和可恢复。仅当指定storage.dbPath设置时，此选项才适用。mongod默认启用日志记录。
      # 设置仅适用于mongod。
      # 不适用于使用内存存储引擎的mongod实例。
      enabled: <boolean>
      # 默认: 100, 在日记操作之间的最大毫秒时间。值的范围可以是1到500毫秒。较低的值会增加日志的持久性，但会牺牲磁盘性能
      # 设置仅适用于mongod。
      # 不适用于使用内存存储引擎的mongod实例。
      commitIntervalMs: <num>
   # 默认: false, 如果为true，MongoDB将使用单独的目录存储每个数据库的数据。这些目录位于storage.dbPath目录下，每个子目录名对应于数据库名
   # 设置仅适用于mongod。
   # 不适用于使用内存存储引擎的mongod实例。
   directoryPerDB: <boolean>
   # 默认: 60, fsync操作将数据刷新到数据文件
   # 不要在生产系统上设置此值。在几乎所有情况下，都应该使用默认设置
   # 如果将storage.syncPeriodSecs设置为0，MongoDB将不会将内存映射文件同步到磁盘
   # 设置仅适用于mongod。
   # 不适用于使用内存存储引擎的mongod实例。
   syncPeriodSecs: <int>
   # 默认: wiredTiger, 支持 wiredTiger 和 inMemory
   # 4.2开始，MongoDB删除了不推荐使用的MMAPv1存储引擎
   engine: <string>
   wiredTiger:
      engineConfig:
         # 用于所有数据的内部高速缓存的最大大小
         # 索引生成所消耗的内存（请参阅maxIndexBuildMemoryUsageMB）与WiredTiger缓存内存分开
         # 3.4开始，这些值可以从0.25 GB到10000 GB不等，并且可以是浮点值。
         # 默认: 50% of (RAM - 1 GB) 与 256 MB , 较大者
         cacheSizeGB: <number>
         # 默认: snappy, 用于压缩WiredTiger日志数据的压缩类型
         # none, snappy, zlib, zstd(4.2 之后)
         journalCompressor: <string>
         # 默认: false;
         # true时，mongod将索引和集合存储在data（即storage.dbPath）目录下的单独子目录中。
         # 具体来说，mongod将索引存储在名为index的子目录中，将集合数据存储在名为collection的子目录中
         directoryForIndexes: <boolean>
         # 默认0; 即无限
         # “查找表（或缓存溢出）表”文件WiReTigGelas.Wt的最大大小（以GB为单位）
         # 4.2.1 之后
         maxCacheOverflowFileSizeGB: <number>
      collectionConfig:
         # 默认:snappy
         # none, snappy, zlib, zstd(4.2 之后)
         # 指定集合数据的默认压缩。创建集合时，可以在每个集合的基础上覆盖此项
         blockCompressor: <string>
      indexConfig:
         # 默认: true, 启用或禁用索引数据的前缀压缩
         # 如果在现有的索引更改配置, 所有新索引将使用前缀压缩。现有索引不受影响
         prefixCompression: <boolean>
   inMemory:
      engineConfig:
         # 默认: 50%的物理RAM - 1GB
         # 3.4中更改：值的范围从256MB到10TB，可以是浮点值
         # 内存存储引擎数据中分配的最大内存量，包括索引、OMOPLG如果是副本集的一部分、副本集或共享的簇元数据等
         inMemorySizeGB: <number>
operationProfiling:
   # 默认: off
   # off: 探查器已关闭，不收集任何数据。这是默认的探查器级别
   # slowOp: 探查器为耗时超过slowms值的操作收集数据。
   # all: 探查器为所有操作收集数据
   # 分析可能会影响性能并与系统日志共享设置。在配置和启用生产部署上的探查器之前，请仔细考虑任何性能和安全影响
   mode: <string>
   slowOpThresholdMs: <int>
   slowOpSampleRate: <double>
replication:
   # 复制操作日志的最大兆字节大小
   # 4.0开始，oplog可以增长到超过其配置的大小限制，以避免删除大多数提交点
   # 默认: oplog通常是可用磁盘空间的5%
   oplogSizeMB: <int>
   replSetName: <string>
   secondaryIndexPrefetch: <string>
   enableMajorityReadConcern: <boolean>
sharding:
   clusterRole: <string>
   archiveMovedChunks: <boolean>
auditLog:
   destination: <string>
   format: <string>
   path: <string>
   filter: <string>
snmp:
   disabled: <boolean>
   subagent: <boolean>
   master: <boolean>

# 仅 mongos 设置
replication:
   localPingThresholdMs: <int>
sharding:
   configDB: <string>



!



# 指定配置启动
# mongod --config /etc/mongod.conf
# 简单测试启动
#
# 关闭服务
# mongo
# use admin;
# db.shutdownServer()

# 检查是否启动
# ss -ntal
# ps -ef | grep mongod

# 打开端口 27017
# firewall-cmd --permanent --add-port=27017/tcp
# 重启防火墙
# firewall-cmd --reload

# 1、连接本机服务
# mongo

# 2、创建数据库
# use DATABASE_NAME 如果数据库不存在，则创建数据库，否则切换到指定数据库
# 3、当前数据库
# db
# 4、查看所有的数据库
# show dbs
# 5、可以看到，我们刚创建的数据库 test 并不在数据库的列表中， 要显示它，我们需要向 test 数据库插入一些数据
# db.test.insert({"name":"测试name"})
# 6、再次查看所有数据库
# show dbs 出现 test 数据库

# 7、删除数据库, 必须切换到对应数据库
# use test
# db.dropDatabase()
# show dbs

# 8、创建集合, 必须切换到对应数据库
# use test
# db.createCollection("testC")
# 查看集合
# show collections / show tables
# 创建集合并设置配置
# db.createCollection("mycol", { capped : true, autoIndexId : true, size : 6142800, max : 10000 } )
#   capped: 如果为 true，则创建固定集合。固定集合是指有着固定大小的集合，当达到最大值时，它会自动覆盖最早的文档。当该值为 true 时，必须指定 size 参数
#   autoIndexId: （可选）如为 true，自动在 _id 字段创建索引。默认为 false。
#   size: （可选）为固定集合指定一个最大值，以千字节计（KB）。 如果 capped 为 true，也需要指定该字段。
#   max: （可选）指定固定集合中包含文档的最大数量。
# 如果插入不存在集合，将自动创建
# db.test.insert({"name":"测试name"})

# 9、删除集合, 必须切换到对应数据库
# db.test.drop()
# 查看集合
# show collections / show tables





