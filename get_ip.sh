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

function getIPStr() {
    ix=1
    for ip in `getIP`
    do
        let ix+=1
        echo "IP.$ix = $ip\n"
    done
}

# 打印
IP_STR=$(echo `getIPStr` | sed 's/\\n\s/\\n/g')
HOSTNAME_STR="DNS.2 = `hostname`"
echo -e "本机ip为：\n`getIP`\nhostname为：`hostname`"