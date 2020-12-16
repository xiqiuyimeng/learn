#!/bin/bash
# 获取ip地址
function getIP() {
    NETWORK_CARD_NAME=`ls /etc/sysconfig/network-scripts | grep 'ifcfg-' | grep -v 'ifcfg-lo' | cut -d "-" -f2`
    IP_INFO_NUM=$((`ifconfig | sed "/^$NETWORK_CARD_NAME/=" -n` + 1))
    ifconfig | sed "$IP_INFO_NUM p" -n | awk '{print $2}'
}
echo `getIP`