#!/bin/bash

# __readINI [配置文件路径+名称] [节点名] [键值]
function __readINI() {
 INIFILE=$1; SECTION=$2; ITEM=$3
 _readIni=`awk -F '=' '/\['$SECTION'\]/{a=1}a==1&&$1~/'$ITEM'/{print $2;exit}' $INIFILE`
echo ${_readIni}
}

# __ROLE [规则] [用户名 / ips]
function __ROLE() {
  CURRENTROLE=$1; USERNAME=$2;
  _role=( $( __readINI /etc/doubleVali.ini $CURRENTROLE $USERNAME ) )
  if [ "$_role" = "nil" ]
  then
    echo 0
  fi
  aips=(${_role//,/ })
  isok=0
  for aip in ${aips[@]}
  do
    #echo "$aip"
    noai="${aip// /}"
    ai="${noai//\*/0}"
    acips=(${ai//./ })
    if [[ "${acips[0]}" = "${zore[0]}" || "${acips[0]}" == "${cips[0]}" ]]
    then
      if [[ "${acips[1]}" = "${zore[0]}" || "${acips[1]}" == "${cips[1]}" ]]
      then
        if [[ "${acips[2]}" = "${zore[0]}" || "${acips[2]}" == "${cips[2]}" ]]
        then
          if [[ "${acips[3]}" = "${zore[0]}" || "${acips[3]}" == "${cips[3]}" ]]
          then
            isok=1
            break
          fi
        fi
      fi
    fi
  done
  if [ $isok -eq 1 ]
  then
    echo 1
  else
    echo 0
  fi
}

# 允许、不允许
# ALLOW
# NOTALLOW
# 全局允许、不允许
# GLOBAL_ALLOW
# GLOBAL_NOTALLOW
# 固定 ips
function __getRole() {
  CURRENTROLE=$1; USERNAME=$2;
  _result=( $( __ROLE $CURRENTROLE $USERNAME ) )
  echo $_result
}


# 当前用户信息
wm=`who -m`
whos=(${wm//:/ })
# 用户名
username=${whos[0]}
username=${username// /}
eip=${whos[${#whos[@]} - 1]}
ipr=${eip//(/ }
# ip信息
ip=${ipr//)/ }
zore=0
cips=(${ip//./ })

isAllow=0

_sna=( $(__getRole NOTALLOW $username) )
# 是否是单点不允许
if [ $_sna -eq 0 ]
then
  # 是否单点允许
  _sa=( $(__getRole ALLOW $username) )
  if [ $_sa -eq 1 ]
  then
    isAllow=1
  else
    _gna=( $(__getRole GLOBAL_NOTALLOW ips) )
    # 是否是全局不允许
    if [ $_gna -eq 0 ]
    then
      _ga=( $(__getRole GLOBAL_ALLOW ips) )
      # 是否单点允许
      if [ $_ga -eq 1 ]
      then
        isAllow=1
      fi
    fi
  fi
fi

# 不允许ip登录
if [ $isAllow -eq 0 ]
then
  echo "$ip 禁止连接，请联系管理员 "
  exit 0
fi

# 验证信息
login=0
for i in `seq 3`; do
  if ! read -t 15 -p 'Username: ' username ;then
    exit 0
  fi

  if ! read -t 15 -s -p 'Password: ' password ;then
    exit 0
  fi
  echo

  diff <(echo $username:$password) /etc/janbar >/dev/null 2>&1
  if [ $? -eq 0 ];then
    login=1; break
  fi
  echo -e 'Login incorrect\n'
done

if [ $login -eq 1 ];then
  /bin/bash
fi
