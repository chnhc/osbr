#!/bin/bash


#自行安装Xtrabackup


# 注意全量一天一个，增量一小时一个
## chmod u+x Xtrabackup_all.sh
# chmod u+x Xtrabackup_increment.sh
#

# 请手动安装 zip
# yum install -y zip
#
# yum install -y unzip
#

# 5 0 * * * /bin/bash /backups/Xtrabackup_all.sh #每天零点5分全备
# 55 * * * * /bin/bash /backups/Xtrabackup_increment.sh #每个小时的55分进行一次增备

# crontab -e
#每天零点5分全备
# 5 0 * * * /backups/Xtrabackup_all.sh
#每个小时的55分进行一次增备
# 55 * * * * /backups/Xtrabackup_increment.sh

#zip -rq a.zip ./*
#-q不显示打包过程

#unzip -od "解压path" a.zip
#-o不显示解压过程



# 备份流程:
#   每天 全量备份
#     全量备份 - 判断昨天是否有全量备份和增量备份 - 有: 将增量 和 全量 都 压缩、保存
#             - 删除昨天的 全量备份和增量备份
#
#   每小时 增量备份

# 恢复数据 -- 请手动恢复数据
#   先prepare全备：
#   innobackupex --defaults-file=/etc/my.cnf --databases=mytest --user=root --password=Chu163.com --apply-log --redo-only /backups/all/2019-12-25_13-14-16/
#
#   增量一次恢复：
#   innobackupex --defaults-file=/etc/my.cnf --apply-log --redo-only  --user-memory=1G /backups/all/2019-12-25_13-14-16/ --incremental-dir=/backups/increment/2019-12-25_13-20-31/
#
#   增量二次恢复：
#   innobackupex --defaults-file=/etc/my.cnf  --apply-log --redo-only  --user-memory=1G /backups/all/2019-12-25_13-14-16/ --incremental-dir=/backups/increment/2019-12-25_13-22-33/
#
#   增量三次恢复：最后不需要 --redo-only
#   innobackupex --defaults-file=/etc/my.cnf  --apply-log  --user-memory=1G /backups/all/2019-12-25_13-14-16/ --incremental-dir=/backups/increment/2019-12-25_13-27-27/
#
#   停止数据库服务
#
#   1、全部库恢复数据
#       innobackupex --defaults-file=/etc/my.cnf  --copy-back /backups/all/2019-12-25_13-14-16/
#
#   2、部分库恢复：
#       在全备份目录中：
#       cd /backups/all/2019-12-25_13-14-16/
#       cp ibdata1 ib_logfile0 ib_logfile1 /usr/data/mysql/
#       cp -av mytest/ /usr/data/mysql/
#       如果不是本用户操作，还需要为cp 的数据文件夹设置权限
#       chown -R ziheng:senior /usr/data/mysql/*
#
#   启动数据库服务




# 备份数据用户名
USER=root
# 密码
PASSWORD=Chu163.com
# 主机
HOST=localhost
# socket 位置
SOCKET_FILE=/usr/data/mysql/mysql.sock
# mysql 配置文件
DEFAULTS_FILE=/etc/my.cnf
# 全备地址
BACKUP_ALL=/backups/all
# 增量地址
BACKUP_INCREMENT=/backups/increment
# LOG地址
BACKUP_BASE_LOGS=/backups/logs
BACKUP_LOGS="${BACKUP_BASE_LOGS}/`date "+%Y%m%d"`"
# 今天log
LOG_PATH="${BACKUP_LOGS}/backup_`date "+%Y%m%d"`" ;
# XTRABACKUP_LOG 日志
XTRABACKUP_LOG_PATH="${BACKUP_LOGS}/xtrabackup_`date "+%Y%m%d%H%M"`" ;
# 今天的全量备份路径
TODAY_ALL_PATH="${BACKUP_ALL}/`date "+%Y%m%d"`" ;
# 今天的增量备份路径
TODAY_INCREMENT_PATH="${BACKUP_INCREMENT}/`date "+%Y%m%d"`" ;
# 备份数据库名 ： 设置一个 ， 没有则备份所有
BACKUP_DATABASE="mytest"
# log 保存的时间 默认 保存10天
LOG_DATE=10
# 昨天文件名
YESTERDAY_NAME=`date -d "-1 day" "+%Y%m%d"`
# 昨天的全量备份路径
YESTERDAY_ALL_PATH="${BACKUP_ALL}/${YESTERDAY_NAME}" ;
# 昨天的增量备份路径
YESTERDAY_INCREMENT_PATH="${BACKUP_INCREMENT}/${YESTERDAY_NAME}" ;
# tmp 缓存目录
TMP_PATH=/backups/tmp
# 今天备份昨天的数据，删除前天的缓存
TMP_BACKUP_BEFORE_YESTERDAY="${TMP_PATH}/backup_`date -d "-2 day" "+%Y%m%d"`/" ;
# tmp 压缩地址
TMP_BACKUP="${TMP_PATH}/backup_${YESTERDAY_NAME}/" ;
TMP_ALL_BACKUP="${TMP_PATH}/backup_${YESTERDAY_NAME}/all" ;
TMP_INCREMENT_BACKUP="${TMP_PATH}/backup_${YESTERDAY_NAME}/increment" ;
# 压缩后保存目录 bak
BAK_PATH=/backups/bak
# 压缩文件名路径
BAK_BACKUP="${BAK_PATH}/backup_${YESTERDAY_NAME}.zip" ;
#BAK_BACKUP="${BAK_PATH}" ;



# 日志 -- 当前执行的时间
function Empty_Head() {

   # 判断 log文件夹 是否存在
  if [ ! -d ${BACKUP_BASE_LOGS} ];then
    mkdir -p ${BACKUP_BASE_LOGS}
  fi

  # 判断 log文件夹 是否存在
  if [ ! -d ${BACKUP_LOGS} ];then
    mkdir -p ${BACKUP_LOGS}
  fi

   # 删除LOG_DATE 前的日志数据
  find ${BACKUP_BASE_LOGS} -mtime +$LOG_DATE -name "*" -exec rm -rf {} \;


  echo "                       " >> ${LOG_PATH}
  echo " `date +%Y/%m/%d-%H:%M`" >> ${LOG_PATH}
}

# 全量备份
function Backup_All(){
  # 判断socket 是否存在
  if [ ! -e "${SOCKET_FILE}" ];then
    echo "${SOCKET_FILE} 文件不存在！" >> ${LOG_PATH}
    exit 0
  fi

  # 判断 全量备份 地址是否存在
  if [ ! -d ${BACKUP_ALL} ];then
    mkdir -p ${BACKUP_ALL}
  fi

   # 判断 今天全量备份 地址是否存在
  if [ ! -d ${TODAY_ALL_PATH} ];then
    mkdir -p ${TODAY_ALL_PATH}
  fi

  # 进行全量备份
  echo " 正在执行全新的完全备份 ========>> "  >>${LOG_PATH}

  # 判断是否需要指定 - 数据库
  # -z 	检测字符串长度是否为0，为0返回 true。
  # -n 	检测字符串长度是否为0，不为0返回 true。
  if [ -z ${BACKUP_DATABASE} ];then
    # 没有设置 指定数据量
    echo " 全数据库备份 ========>> "  >>${LOG_PATH}
    `innobackupex --defaults-file=${DEFAULTS_FILE} --user=${USER} --password=${PASSWORD} --host=${HOST} --socket=${SOCKET_FILE} ${TODAY_ALL_PATH} 2> ${XTRABACKUP_LOG_PATH}` ;sleep 2
  else
    echo " 指定数据库备份 - ${BACKUP_DATABASE} ========>> "  >>${LOG_PATH}
    `innobackupex --defaults-file=${DEFAULTS_FILE} --databases=${BACKUP_DATABASE} --user=${USER} --password=${PASSWORD} --host=${HOST} --socket=${SOCKET_FILE} ${TODAY_ALL_PATH} 2> ${XTRABACKUP_LOG_PATH}` ;sleep 2
  fi

  #查看备份日志，判断备份是否成功
  if [ `/usr/bin/tail -1 ${XTRABACKUP_LOG_PATH} | grep 'completed OK!' | wc -l` -eq 1 ];then
     #`grep 'full-backuped' ${LATEST_FULL_BACKUP}/xtrabackup_checkpoints | wc -l` -eq 1
     echo " 数据成功全量备份到：${TODAY_ALL_PATH} " >>${LOG_PATH}
     echo " Xtrabackup日志：${XTRABACKUP_LOG_PATH} " >>${LOG_PATH}
  else
     echo " innobackupex 全备命令执行失败! "       >>${LOG_PATH}
     echo " -------------错误日志------------- "  >>${LOG_PATH}
     tail -10 ${XTRABACKUP_LOG_PATH}             >>${LOG_PATH}
     echo " ---------------------------------- " >>${LOG_PATH}
     echo " Xtrabackup日志：${XTRABACKUP_LOG_PATH} " >>${LOG_PATH}
     exit 1
  fi

}


# 增量备份
function Backup_Increment(){
  # 判断socket 是否存在
  if [ ! -e "${SOCKET_FILE}" ];then
    echo "${SOCKET_FILE} 文件不存在！" >> ${LOG_PATH}
    exit 0
  fi

  # 如果没有全量地址，则不进行增量备份
  if [ ! -e "${TODAY_ALL_PATH}" ];then
    echo "未先进行全量备份！" >> ${LOG_PATH}
    echo "${TODAY_ALL_PATH} 文件不存在！" >> ${LOG_PATH}
    exit 0
  fi

  # 判断 增量备份 地址是否存在
  if [ ! -d ${BACKUP_INCREMENT} ];then
    mkdir -p ${BACKUP_INCREMENT}
  fi

   # 判断 今天增量备份 地址是否存在
  if [ ! -d ${TODAY_INCREMENT_PATH} ];then
    mkdir -p ${TODAY_INCREMENT_PATH}
  fi

  # 获取全量备份路径 ls | sort -r |head -n 1
  BACKUP_ALL_LASTER="$TODAY_ALL_PATH/`ls $TODAY_ALL_PATH| sort -r |head -n 1`";

  # 获取最后一次的增量备份路径
  INCREMENT_LASTER="`ls $TODAY_INCREMENT_PATH| sort -r |head -n 1`";
  BACKUP_INCREMENT_LASTER="$TODAY_INCREMENT_PATH/`ls $TODAY_INCREMENT_PATH| sort -r |head -n 1`";

  # 进行增量备份
  echo " 正在执行增量备份 ========>> "  >>${LOG_PATH}

  # 判断是否需要指定 - 数据库
  # -z 	检测字符串长度是否为0，为0返回 true。
  # -n 	检测字符串长度是否为0，不为0返回 true。
  if [ -z ${BACKUP_DATABASE} ];then
    # 没有设置 指定数据量
    echo " 全数据库增量备份 ========>> "  >>${LOG_PATH}
    # 判断是否进行过了增量备份
    if [ -z ${INCREMENT_LASTER} ];then
      # 之前没有进行过增量备份
      echo " 之前没有进行过增量备份 ========>> "  >>${LOG_PATH}
      `innobackupex --defaults-file=${DEFAULTS_FILE} --user=${USER} --password=${PASSWORD} --host=${HOST} --socket=${SOCKET_FILE} --incremental ${TODAY_INCREMENT_PATH} --incremental-basedir=${BACKUP_ALL_LASTER} 2> ${XTRABACKUP_LOG_PATH}` ;sleep 2
    else
      # 进行过增量备份
      echo " 上一次进行过增量备份 ${BACKUP_INCREMENT_LASTER} ========>> "  >>${LOG_PATH}
      `innobackupex --defaults-file=${DEFAULTS_FILE} --user=${USER} --password=${PASSWORD} --host=${HOST} --socket=${SOCKET_FILE} --incremental ${TODAY_INCREMENT_PATH} --incremental-basedir=${BACKUP_INCREMENT_LASTER} 2> ${XTRABACKUP_LOG_PATH}` ;sleep 2
    fi

  else
    echo " 指定数据库增量备份 - ${BACKUP_DATABASE} ========>> "  >>${LOG_PATH}
    # 判断是否进行过了增量备份
    if [ -z ${INCREMENT_LASTER} ];then
      # 之前没有进行过增量备份
      echo " 之前没有进行过增量备份 ========>> "  >>${LOG_PATH}
      `innobackupex --defaults-file=${DEFAULTS_FILE} --databases=${BACKUP_DATABASE} --user=${USER} --password=${PASSWORD} --host=${HOST} --socket=${SOCKET_FILE} --incremental ${TODAY_INCREMENT_PATH} --incremental-basedir=${BACKUP_ALL_LASTER} 2> ${XTRABACKUP_LOG_PATH}` ;sleep 2
    else
      # 进行过增量备份
      echo " 上一次进行过增量备份 ${BACKUP_INCREMENT_LASTER} ========>> "  >>${LOG_PATH}
      `innobackupex --defaults-file=${DEFAULTS_FILE} --databases=${BACKUP_DATABASE} --user=${USER} --password=${PASSWORD} --host=${HOST} --socket=${SOCKET_FILE} --incremental ${TODAY_INCREMENT_PATH} --incremental-basedir=${BACKUP_INCREMENT_LASTER} 2> ${XTRABACKUP_LOG_PATH}` ;sleep 2
    fi
  fi

  #查看备份日志，判断备份是否成功
  if [ `/usr/bin/tail -1 ${XTRABACKUP_LOG_PATH} | grep 'completed OK!' | wc -l` -eq 1 ];then
     #`grep 'full-backuped' ${LATEST_FULL_BACKUP}/xtrabackup_checkpoints | wc -l` -eq 1
     echo " 数据成功增量备份到：${TODAY_INCREMENT_PATH} " >>${LOG_PATH}
     echo " Xtrabackup日志：${XTRABACKUP_LOG_PATH} " >>${LOG_PATH}
  else
     echo " innobackupex 增量命令执行失败! "       >>${LOG_PATH}
     echo " -------------错误日志------------- "  >>${LOG_PATH}
     tail -10 ${XTRABACKUP_LOG_PATH}             >>${LOG_PATH}
     echo " ---------------------------------- " >>${LOG_PATH}
     echo " Xtrabackup日志：${XTRABACKUP_LOG_PATH} " >>${LOG_PATH}
     exit 1
  fi
}



# 安装zip
function Install_Zip(){
  echo " zip 安装中  ========>> "       >>${LOG_PATH}
  yum install -y zip >>/dev/null 2>&1
  yum install -y unzip >>/dev/null 2>&1

  zip --help >>/dev/null 2>&1
  Check=$?
    if [ $Check -ne 0 ];then
       echo "zip install Failed ！" >>${LOG_PATH}
       exit 0
    fi
  echo "======== zip 成功 ======== "  >>${LOG_PATH}
}


# 压缩文件
function Zip_Data(){
  if [ -d ${YESTERDAY_ALL_PATH} ];then

    # 判断是否有 zip 工具
    #command -v zip >/dev/null 2>&1 || Install_Zip

    echo " zip压缩数据  ========>> "       >>${LOG_PATH}
    # 判断 缓存目录 地址是否存在
    if [ ! -d ${TMP_BACKUP} ];then
      mkdir -p ${TMP_BACKUP}
    fi

    # 判断 缓存目录 地址是否存在
    if [ ! -d ${TMP_PATH} ];then
      mkdir -p ${TMP_PATH}
    fi

    # 判断 全量压缩 地址是否存在
    if [ ! -d ${TMP_ALL_BACKUP} ];then
      mkdir -p ${TMP_ALL_BACKUP}
    fi

    # 判断 增量压缩 地址是否存在
    if [ ! -d ${TMP_INCREMENT_BACKUP} ];then
      mkdir -p ${TMP_INCREMENT_BACKUP}
    fi

    # 判断 备份压缩 地址是否存在
    #if [ ! -d ${BAK_BACKUP} ];then
    #  mkdir -p ${BAK_BACKUP}
    #fi

     # 判断 保证文件不空 地址是否存在
    if [ ! -d ${YESTERDAY_INCREMENT_PATH} ];then
      mkdir -p ${YESTERDAY_INCREMENT_PATH}
    fi

    echo " 拷贝 全量 和 增量备份的数据到 ${YESTERDAY_ALL_PATH} 和 ${YESTERDAY_INCREMENT_PATH} ========>> "       >>${LOG_PATH}
    # 先拷贝 全量 和 增量备份的数据到 /backups/tmp/backup_时间 目录
    `mv ${YESTERDAY_ALL_PATH} ${TMP_ALL_BACKUP}` ; sleep 2
    `mv ${YESTERDAY_INCREMENT_PATH} ${TMP_INCREMENT_BACKUP}`;sleep 2

    echo " 压缩数据，并保存到 ${BAK_BACKUP} ========>> "       >>${LOG_PATH}
    # 压缩完成，保存备份 /backups/bak/ 下面
    `zip -rq  ${BAK_BACKUP} ${TMP_BACKUP} >>/dev/null 2>&1 ` ;sleep 10
    # `cp -R -f ${TMP_BACKUP} ${BAK_BACKUP}`
    sleep 2
    echo " ======== 压缩、保存完成。删除缓存 ======== "       >>${LOG_PATH}
    # 清除缓存 tmp
    #rm -rf ${TMP_BACKUP}
    rm -rf ${TMP_BACKUP_BEFORE_YESTERDAY}
    rm -rf ${YESTERDAY_ALL_PATH}
    rm -rf ${YESTERDAY_INCREMENT_PATH}
  fi
}


# 主函数
function Main(){
  # 初始化日志头
  Empty_Head
  # 执行全量备份
  #Zip_Data
  #Backup_All
  # 执行增量备份
  #Backup_Increment
  #Zip_Data
}


Main






