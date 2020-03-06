#!/bin/bash

# 准备软件包
# fastDfs 相关包 https://github.com/happyfish100/ 下载
# libfastcommon-1.0.43.zip
# fastdfs-6.06.tar.gz
# fastdfs-nginx-module-1.22.tar.gz
# 使用 openresty - nginx 下载 https://openresty.org/en/download.html#lastest-release
# openresty-1.15.8.2.tar.gz
# nginx 缓存模块
# fastdfs-nginx-module-1.22.tar.gz

# 安装必要环境
# yum install make cmake gcc gcc-c++
# yum install -y unzip zip
# yum -y install vim*
# yum install pcre-devel openssl openssl-devel perl readline-devel
# yum install zlib
# yum install zlib-devel

########################### 安装 libfastcommon ###########################
#
# unzip libfastcommon-1.0.43.zip -d /usr/local/fast/
# cd /usr/local/fast/libfastcommon-1.0.43/
# 进行编译和安装
# ./make.sh
# ./make.sh install
# 我们的libfastcommon默认安装到了/usr/lib64/这个位置
# FastDFS主程序设置的目录为/usr/local/lib/，所以我们需要创建/usr/lib64/下的一些核心执行程序的软连接文件
# mkdir /usr/local/lib/
# ln -s /usr/lib64/libfastcommon.so /usr/local/lib/libfastcommon.so
# ln -s /usr/lib64/libfastcommon.so /usr/lib/libfastcommon.so
# ln -s /usr/lib64/libfdfsclient.so /usr/local/lib/libfdfsclient.so
# ln -s /usr/lib64/libfdfsclient.so /usr/lib/libfdfsclient.so



########################### 安装 FastDFS ###########################
#
# tar -zxvf fastdfs-6.06.tar.gz -C /usr/local/fast/
# cd /usr/local/fast/fastdfs-6.06/
# 进行编译和安装
# ./make.sh
# ./make.sh install

# 采用默认安装方式脚本文件说明:
# 1、 服务脚本
#   /etc/init.d/fdfs_storaged
#   /etc/init.d/fdfs_trackerd
#   cd /etc/init.d/ && ls | grep fdfs
# 2、 配置文件
#   /etc/fdfs/client.conf.sample
#   /etc/fdfs/storage.conf.sample
#   /etc/fdfs/tracker.conf.sample
# 3、 执行脚本
#   cd /usr/bin/ && ls | grep fdfs

############ 配置跟踪器 ############
# 选择一个节点做跟踪器
# cd /etc/fdfs/
# 复制跟踪器的配置
# cp tracker.conf.sample tracker.conf
# 修改tracker.conf文件
# vim /etc/fdfs/tracker.conf
#   修改存储地址
#   base_path = /usr/data/fastdfs/tracker
# 创建目录
# mkdir -p /usr/data/fastdfs/tracker
# 端口：22122、8080

# 启动操作
# cd /usr/data/fastdfs/tracker && ll
# /etc/init.d/fdfs_trackerd start
# ps -el | grep fdfs
# ps -ef | grep fdfs
# 停止
# /etc/init.d/fdfs_trackerd stop

# 可以设置开机启动跟踪器：（一般生产环境需要开机启动一些服务，如keepalived、linux、tomcat等等）
#命令：vim /etc/rc.d/rc.local
#加入配置：/etc/init.d/fdfs_trackerd start


############ 配置存储器 ############
# 选择一个节点做存储器
# cd /etc/fdfs/
# 复制存储器的配置
# cp storage.conf.sample storage.conf
# 修改storage.conf文件
# vim /etc/fdfs/storage.conf
#   修改存储地址
#   base_path = /usr/data/fastdfs/storage
#   store_path0 = /usr/data/fastdfs/storage
#   tracker_server = 192.168.232.205:22122
#   http.server_port = 8888
# 创建目录
# mkdir -p /usr/data/fastdfs/storage
# 端口：23000、8888

# 启动操作
# cd /usr/data/fastdfs/storage && ll
# /etc/init.d/fdfs_storaged start
# ps -el | grep fdfs
# ps -ef | grep fdfs
# 停止
# /etc/init.d/fdfs_storaged stop

# 同理，也可以设置开机启动存储器：（一般生产环境需要开机启动一些服务，如keepalived、linux、tomcat等等）
#命令：vim /etc/rc.d/rc.local
#加入配置：/etc/init.d/fdfs_storaged start


############ 测试环境 ############
# 注意： 文件是在tracker（跟踪器）中上传
# cd /etc/fdfs/
# cp client.conf.sample client.conf
# vim /etc/fdfs/client.conf
#   base_path = /usr/data/fastdfs/tracker
#   tracker_server = 192.168.232.205:22122

# 测试上传
# cd /usr/bin/
# ls | grep fdfs
# /usr/bin/fdfs_upload_file /etc/fdfs/client.conf /usr/local/kuang.png



########################### 安装 Openresty-nginx ###########################
# 注意：在存储器机器上安装单节点测试
# 模块配置
# tar -zxvf /usr/local/fastdfs-nginx-module-1.22.tar.gz -C /usr/local/fast/
# cd fastdfs-nginx-module-1.22/src/
# 修改配置config
# vim config (删除 local/ )
#   ngx_module_incs="/usr/include"
#   CORE_INCS="$CORE_INCS /usr/include"

# 安装
# tar -zxvf openresty-1.15.8.2.tar.gz
# cd openresty-1.15.8.2
# 加入模块命令：
# ./configure --add-module=/usr/local/fast/fastdfs-nginx-module-1.22/src/
# 编译安装
# gmake
# gmake install

# 1、复制fastdfs-ngin-module中的配置文件，到/etc/fdfs目录中
# cp /usr/local/fast/fastdfs-nginx-module-1.22/src/mod_fastdfs.conf /etc/fdfs/
# 2、修改模块配置
# vim /etc/fdfs/mod_fastdfs.conf
#   connect_timeout=10
#   tracker_server=192.168.232.205:22122
#   url_have_group_name = true
#   store_path0=/usr/data/fastdfs/storage
# 3、复制FastDFS里的2个文件，到/etc/fdfs目录中
# cd /usr/local/fast/fastdfs-6.06/conf/
# cp http.conf mime.types /etc/fdfs/
# 4、创建一个软连接，在/fastdfs/storage文件存储目录下创建软连接，将其链接到实际存放数据的目录
# ln -s /usr/data/fastdfs/storage/data/ /usr/data/fastdfs/storage/data/M00

# 5、修改Nginx 配置
# cd /usr/local/openresty/nginx/conf/
# 修改内容为：
#   listen 8888;
#   server_name localhost;
#   location ~/group([0-9])/M00 {
#     #alias /fastdfs/storage/data;
#     ngx_fastdfs_module;
#   }
# nginx里的端口要和第五步配置FastDFS存储中的storage.conf文件配置一致，也就是（http.server_port=8888）

# 6、启动Nginx
# /usr/local/openresty/nginx/sbin/nginx
# /usr/local/openresty/nginx/sbin/nginx -s reload

# 7、浏览器访问
# http://192.168.232.206:8888/group1/M00/00/00/wKjozl41NdCAGbdKAA68FvAT8Vc945.png












