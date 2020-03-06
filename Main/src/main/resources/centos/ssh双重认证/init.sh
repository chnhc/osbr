#!/bin/bash

# 初始化操作
#
# 1、创建自定义验证
# vi /bin/doubleVali
# chmod 777 /bin/doubleVali
# 可以使用doubleVali.sh
# 添加二次验证用户名密码/etc/janbar
# 添加 admin:passwd
# 2、确保 /etc/shells 里面有一行 /bin/doubleVali 即可
# vi /etc/shells
# 追加 /bin/doubleVali
# 3、自定义ip拦截
# vi /etc/doubleVali.ini
:<<!
# 格式
# 如果 allow 或 notallow 设置ips, 将忽略 global
# 如果 allow 和 notallow 同时设置了, 那么是不允许登录的
# 如果 global_allow 和 global_notallow 同时设置了, 也是不允许登录的


[GLOBAL_ALLOW]
ips = *.*.*.*

[GLOBAL_NOTALLOW]
ips = nil

[ALLOW]
# 用户登录名 = 允许的ip
mytest = 192.168.232.206

[NOTALLOW]
# 用户登录名 = 允许的ip
mytest = 192.168.232.205


!















