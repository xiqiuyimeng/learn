#!/bin/bash
# 获取ip地址
function getIP() {
    # /sys/class/net 目录下存储了所有网卡信息，子目录均为网卡名，
    # /sys/devices/virtual/net/ 目录下存储了所有的虚拟网卡名
    NETWORK_CARD_NAME=`ls /sys/class/net | grep -v "\`ls /sys/devices/virtual/net/\`"`
    for net_name in $NETWORK_CARD_NAME
    do
        possible_ip=`ip a | sed "/$net_name$/p" -n | awk '{print $2}'| awk -F/ '{print$1}'`
        if [ $possible_ip ]; then
            echo $possible_ip
        fi
    done
}

# 打印
echo -e "本机ip为：\n`getIP`\nhostname为：`hostname`"