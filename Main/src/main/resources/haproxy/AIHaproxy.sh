#!/bin/bash

# Haproxy使用详解.html
# HAProxy原理和基本概.html
# Haproxy安装与配置.html
# HAProxy用法详解 全网最详细中文文档.html

# yum install gcc make -y
# 下载软件包 https://src.fedoraproject.org/repo/pkgs/haproxy/
# tar -xvf haproxy-2.1.2.tar.gz -C /usr/local/

# 查看内核
# uname -r
# 可查阅 INSTALL 详细安装

# 并将其分配给TARGET变量： 我的内核是3.10.0，选择linux2628
# make TARGET=linux-glibc ARCH=x86_64 prefix=/usr/local/haproxy
# make install PREFIX=/usr/local/haproxy
# cp -a examples /usr/local/haproxy/

# 后续可直接参考 Haproxy安装与配置.html



























