#!/bin/bash


# 环境安装
# yum install make cmake gcc gcc-c++ pcre-devel openssl openssl-devel perl readline-devel zlib zlib-devel

# ./configure --user=root --group=root --prefix=/usr/local/openresty-1.13.6.1 --with-luajit --without-http_redis2_module --with-http_iconv_module --with-http_ssl_module --with-http_stub_status_module --with-pcre --with-http_realip_module


########################### 安装 Openresty-nginx ###########################

# 安装
# tar -zxvf openresty-1.15.8.2.tar.gz
# cd openresty-1.15.8.2
# ./configure --user=root --group=root --prefix=/usr/local/openresty --with-luajit --without-http_redis2_module --with-http_iconv_module --with-http_ssl_module --with-http_stub_status_module --with-pcre --with-http_realip_module

# 编译安装
# gmake
# gmake install

#打开端口 80

# 6、启动Nginx
# /usr/local/openresty/nginx/sbin/nginx
# /usr/local/openresty/nginx/sbin/nginx -s reload
# /usr/local/openresty/nginx/sbin/nginx -s quit
# ps -ef | grep nginx

# 7、浏览器访问
# http://192.168.232.207
