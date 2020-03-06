#!/bin/bash

# yum install psmisc -- killall
# 详细学习参考 Keepalived_sery-lvs-cluster.pdf
# 先安装好nginx 便于测试

########################### 安装 Keepalived ###########################
# yum install make cmake gcc gcc-c++ pcre-devel openssl openssl-devel perl readline-devel zlib zlib-devel
# 下载软件包 keepalived-2.0.20.tar.gz
# 上传解压
# tar -zxvf keepalived-2.0.20.tar.gz
# cd keepalived-2.0.20
# ./configure --prefix=/usr/local/keepalived

# WARNING - this build will not support IPVS with IPv6. Please install libnl/libnl-3 dev libraries to support IPv6 with IPVS.
# yum -y install libnl libnl-devel
# configure: error: libnfnetlink headers missing
# yum install -y libnfnetlink-devel

#

# 将keepalived安装成Linux系统服务，因为没有使用keepalived的默认安装路径（默认路径：/usr/local）,安装完成之后，需要做一些修改工作：
# 首先创建文件夹，将keepalived配置文件进行复制：
# mkdir /etc/keepalived
# cp /usr/local/keepalived/etc/keepalived/keepalived.conf /etc/keepalived/
# 然后复制keepalived脚本文件：
# cp /usr/local/keepalived/etc/rc.d/init.d/keepalived /etc/init.d/
# cp /usr/local/keepalived/etc/sysconfig/keepalived /etc/sysconfig/
# ln -s /usr/local/sbin/keepalived /usr/sbin/
# ln -s /usr/local/keepalived/sbin/keepalived /sbin/
# 可以设置开机启动：chkconfig keepalived on，到此我们安装完毕!

# 对配置文件进行修改：
# vim /etc/keepalived/keepalived.conf
:<<!

参考 keepalived.conf
参考 nginx_check.sh


keepalived.conf配置文件说明：
（一）Master

#Configuration File for keepalived

global_defs {
   router_id bhz005 ##标识节点的字符串，通常为hostname
}
## keepalived 会定时执行脚本并且对脚本的执行结果进行分析，动态调整vrrp_instance的优先级。这里的权重weight 是与下面的优先级priority有关，如果执行了一次检查脚本成功，则权重会-20，也就是由100 - 20 变成了80，Master 的优先级为80 就低于了Backup的优先级90，那么会进行自动的主备切换。
如果脚本执行结果为0并且weight配置的值大于0，则优先级会相应增加。
如果脚本执行结果不为0 并且weight配置的值小于0，则优先级会相应减少。
vrrp_script chk_nginx {
    script "/etc/keepalived/nginx_check.sh" ##执行脚本位置
    interval 2 ##检测时间间隔
    weight -20 ## 如果条件成立则权重减20（-20）
}

vrrp_sync_group VG_1 {
    group {
        VI_1
    }
    notify_master /etc/keepalived/scripts/sersync_master.sh
    notify_backup /etc/keepalived/scripts/sersync_backup.sh
    notify_stop /etc/keepalived/scripts/sersync_backup.sh
}


## 定义虚拟路由 VI_1为自定义标识。
vrrp_instance VI_1 {
    state MASTER   ## 主节点为MASTER，备份节点为BACKUP
    ## 绑定虚拟IP的网络接口（网卡），与本机IP地址所在的网络接口相同（我这里是eth6）
    interface eth6
    virtual_router_id 172  ## 虚拟路由ID号 1-255
    mcast_src_ip 192.168.1.172  ## 本机ip地址
    priority 100  ##优先级配置（0-254的值）
    Nopreempt  ##
    advert_int 1 ## 组播信息发送间隔，俩个节点必须配置一致，默认1s
    authentication {
        auth_type PASS
        auth_pass bhz ## 真实生产环境下对密码进行匹配
    }

    track_script {
        chk_nginx
    }

    virtual_ipaddress {
        192.168.1.170 ## 虚拟ip(vip)，可以指定多个
    }
}

（二）Backup
#Configuration File for keepalived

global_defs {
   router_id bhz006
}

vrrp_script chk_nginx {
    script "/etc/keepalived/nginx_check.sh"
    interval 2
    weight -20
}

vrrp_instance VI_1 {
    state BACKUP
    interface eth7
    virtual_router_id 173
    mcast_src_ip 192.168.1.173
    priority 90 ##优先级配置
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass bhz
    }

    track_script {
        chk_nginx
    }

    virtual_ipaddress {
        192.168.1.170
    }



}




（三）nginx_check.sh 脚本：

A=`ps -C nginx --no-header |wc -l`
if [ $A -eq 0 ];then
    /usr/local/nginx/sbin/nginx
    sleep 2
    if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
        killall keepalived
    fi
fi

  或者
A=`ps -C nginx --no-header |wc -l`
if [ $A -eq 0 ];then
   killall keepalived
fi

!

# 启动
# service keepalived start
# ps -ef | grep keepalived

# 查日志
# /var/log/messages

# killall keepalived
# vi /lib/systemd/system/keepalived.service


















