#!/bin/bash

# 参考博客
#   https://blog.csdn.net/weixin_42266606/article/details/80879571
#   https://blog.csdn.net/qq_38591756/article/details/82958333

# 自动 安装mysql 5.7.27 或 8.0.17
#
# 注意：设置的用户必须是 sudo 免密的
#       事后可自行 删除用户免密
#
# 机器间设置 ssh 免密登录
#
# 需要：
#   1、ssh 免密
#   2、mysql 组和 mysql 用户 有sudo 免密
#
# ssh免密操作：
#   1)检查是否安装了ssh相关软件包(openssh-server + openssh-clients + openssh)
#       $yum list installed | grep ssh
#
#   2)检查是否启动了sshd进程
#       $>ps -Af | grep sshd
#
#   3)在client侧生成公私秘钥对。
#       $>ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
#
#   4)生成~/.ssh文件夹，里面有id_rsa(私钥) + id_rsa.pub(公钥)
#
#   5)追加公钥到~/.ssh/authorized_keys文件中(文件名、位置固定)
#       $>cd ~/.ssh
#       $>cat id_rsa.pub >> authorized_keys
#
# 注：
#   6)修改authorized_keys的权限为644.
#     权限设置,文件和文件夹权限除了自己之外，别人不可写
#       $>chmod 644 authorized_keys
#
#   7)测试
#       $>ssh localhost
#
#######################
#
# 用户操作
#   1)  检查mysql组和用户是否存在
#       $>cat /etc/group | grep mysql
#       $>cat /etc/passwd | grep mysql
#
#   2) 不存在，创建用户
#       创建mysql用户组
#       $> groupadd mysql

#       创建一个用户名为mysql的用户，并加入mysql用户组
#       $> useradd -g mysql mysql

#       制定password 为123456
#       $> passwd mysql
#
#   3) 设置 sudo 免密
#       授权sudo权限，需要修改sudoers文件
#       a)首先找到文件位置，示例中文件在/etc/sudoers位置
#         $> whereis sudoers

#       b)修改文件权限为可编辑
#         $>chmod -v u+w /etc/sudoers

#       c)修改文件，在如下位置增加一行，保存退出
#       vi /etc/sudoers
#       root ALL=(ALL) ALL           (已有行)
#       mysql ALL=(ALL) ALL          (新增行 - 需要密码)
#       mysql ALL=(ALL) NOPASSWD:ALL (新增行 - 免密)
#
#       d) 修改文件权限为只读
#       chmod -v u-w /etc/sudoers
#
# 检查完毕才能执行自动安装代码

##### 安装mysql #####

# 1、上传、或者分发 mysql-xxxx.x86_64.rpm-bundle.tar
#    安装mysql版本  8 ： /home/mysql/mysql8 ；
#                  5 ： /home/mysql/mysql5
#    ssh mysql@localhost mkdir mysql8 创建文件夹
#    scp /.../mysql-xxxx.x86_64.rpm-bundle.tar mysql@localhost:/home/mysql/mysql8 分发压缩包

# 1.1、 注： ssh mysql@localhost ( ssh 登录到目标机器，进行安装 )

# 2、删除 centos7 自带的 mariadb 并 检查是否删除
#     sudo rpm -qa | grep mariadb (存在)
#     sudo rpm -e --nodeps mariadb-libs-5.5.44-2.el7.centos.x86_64  (卸载 自带的 mariadb)
#     sudo rm -rf /etc/my.cnf
#     sudo rpm -qa | grep mariadb (检查不存在)
#     sudo rpm -qa | grep mysql   (检查 mysql 不存在)

# 3、解压  sudo tar -xvf mysql-xxxx.x86_64.rpm-bundle.tar
#     cd /home/mysql/mysql8
#     sudo tar -xvf mysql-xxxx.x86_64.rpm-bundle.tar

# 4、 sudo rpm -ivh xxx.rpm --nodeps --force  安装 common、 libs 、 client、 server （ 注：lib-compat 不用）
#     ls ( 以 mysql-community 开头， ‘-’ 切分 第3个单词 是 common、 libs 、 client、 server 的 需要安装, 注：lib-compat 不用)
#     sudo rpm -ivh xxx.rpm --nodeps --force (安装 4个模块包 )
#     进度最后[100%] , 安装完成

# 5、初始化数据库
#    sudo mysqld --initialize
#    sudo chown mysql:mysql /var/lib/mysql -R 赋予权限
#    sudo vi /etc/my.cnf 配置文件
#    [mysqld] 添加
#       log-bin=mysql-bin
#       server-id=194 [必须]服务器唯一ID，默认是1，一般取IP最后一段
#    sudo service mysqld start 开启mysql服务
#    service mysqld status 查看mysql服务状态

# 6、查询 mysql 初始化密码
#   sudo grep 'temporary password' /var/log/mysqld.log
#   A temporary password is generated for root@localhost: 5g)b1?lVj0G1 (获取最后一条信息)

# 7、重新设置 密码
#   ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '新密码';
#
# 7.1、主从复制 -----------------------------------------------------------------------------------------------

#   创建用户用于主从复制
#   CREATE USER 'mysync'@'172.21.171.195' IDENTIFIED WITH mysql_native_password BY '123456';
#   GRANT REPLICATION SLAVE,RELOAD,SUPER ON *.* TO 'mysync'@'172.21.171.195';
#   刷新权限
#   flush privileges;

# 8、 登录主服务器的mysql，查询master的状态
#   show master status;

# 9、从服务器设置普通用户只读模式
#   use mysql;
#   create user 'test'@'%' IDENTIFIED WITH mysql_native_password BY '123456';  // 创建普通用户，可以远程连接
#   grant select  on *.* to 'test'@'%' ; //授权所有库,只能查询操作
#   # grant all privileges on *.* to test@'%' IDENTIFIED WITH mysql_native_password BY '123456';  //这是授予所有权限
#   刷新权限
#   flush privileges;

# 10、 配置从服务器Slave
#   change master to master_host='172.21.171.194',master_user='mysync',master_password='123456',master_log_file='mysql-bin.000004',master_log_pos=564;

# 11、 启动从服务器复制功能
#   start slave;

# 12、 检查从服务器复制功能状态
#   show slave status\G
#     Slave_IO_Running: Yes    //此状态必须YES
#     Slave_SQL_Running: Yes     //此状态必须YES

# 13、 主服务器 root 所有ip访问权限
#   use mysql
#   update user set host='%' where user ='root';
#   GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'root'
#   flush privileges;

# 14、 主主配置  -----------------------------------------------------------------------------------------------
#   一主机 194：
#   sudo vi /etc/my.cnf 配置文件
#   binlog_format = mixed
#   auto-increment-increment = 2    # 自增长为2
#   auto-increment-offset = 1       # 从1开始，也就是全是奇数ID
#   slave-skip-errors = all
#   用户权限
#   use mysql
#   CREATE USER 'sync'@'172.21.171.196' IDENTIFIED WITH mysql_native_password BY '123456';
#   GRANT REPLICATION SLAVE,RELOAD,SUPER,REPLICATION CLIENT  ON *.* TO 'sync'@'172.21.171.196';
#   flush privileges;
#   show master status;
#   change master to master_host='172.21.171.196',master_user='sync',master_password='123456',master_log_file='mysql-bin.000001',master_log_pos=1734;
#   start slave;
#   show slave status \G;

#   二主机 196：
#   sudo vi /etc/my.cnf 配置文件
#   binlog_format = mixed
#   auto-increment-increment = 2    # 自增长为2
#   auto-increment-offset = 2       # 从2开始，也就是全是偶数ID
#   slave-skip-errors = all
#   用户权限
#   use mysql
#   CREATE USER 'sync'@'172.21.171.194' IDENTIFIED WITH mysql_native_password BY '123456';
#   GRANT REPLICATION SLAVE,RELOAD,SUPER,REPLICATION CLIENT  ON *.* TO 'sync'@'172.21.171.194';
#   flush privileges;
#   show master status;
#   change master to master_host='172.21.171.194',master_user='sync',master_password='123456',master_log_file='mysql-bin.000006',master_log_pos=1497;
#   start slave;
#   show slave status \G;
#   创建用户用于主从复制
#   CREATE USER 'mysync7'@'172.21.171.197' IDENTIFIED WITH mysql_native_password BY '123456';
#   GRANT REPLICATION SLAVE,RELOAD,SUPER ON *.* TO 'mysync7'@'172.21.171.197';
#   刷新权限
#   flush privileges;


# drop database if exists `tpm_business`;



##### 卸载mysql #####

# 1.1、 注： ssh mysql@localhost ( ssh 登录到目标机器，进行安装 )

# 1、停止mysql服务
#     sudo service mysqld stop

# 2、rpm -qa|grep mysql 卸载 common (会自动将依赖删除)
#   sudo yum remove  mysql-xxx-common-xxx.x86_64 (会自动将依赖删除)

# 3、防止未卸载完全 rpm -qa | grep -i mysql 查询 ，如果存在，rpm -e 名称 ， 卸载
#   sudo rpm -e mysql-xxx.x86_64

# 4、删除 mysql 残余文件
#    sudo rm -rf /var/lib/mysql (默认)
#    sudo rm -rf /etc/my.cnf (默认)
#    sudo rm -rf /home/mysql/mysql8 (删除安装包)


#  防火墙
#  firewall-cmd  --add-port=8080/tcp  --permanent
#  firewall-cmd --reload
#  firewall-cmd  --list-port
#  firewall-cmd  --state

##### Haproxy #####

































