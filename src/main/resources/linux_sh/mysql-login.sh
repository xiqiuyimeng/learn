#!/bin/zsh
# mysql自动登录脚本
# 主要读取变量，选择相应的环境信息，调用expect脚本进行实际登录操作
# case语法：case var in     var可以取变量
#          case1）      每一个case情况
#             command1
#             command2
#             ...
#             ;;
#          *)        相当于 default
#             command  需要执行的命令
#             ;;  相当于 break
#          esac  case块结束


usage=`cat << EOF
usage：mysql-login.sh [OPTION]
OPTION:
	1. 本地localhost\n
EOF`

# 默认端口
port=3306

if [[ $1 ]]; then
	case $1 in
	1)
		env_name="本地"
		host=localhost
		user=root
		passwd=admin
		;;
	*)
		echo "unknown option '$1'"
		echo $usage
		exit 1
		;;
	esac
else
	echo $usage
	exit 2
fi

./mysql-expect.sh $env_name $host $user $port $passwd

