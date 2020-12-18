#!/bin/bash
DOCKER_SERVICE_PATH=`pwd`/docker-config
# COMMUNITY_DOCKER=/root/community-docker
# 获取service这一节点的内容
# 获取 ExecStart 配置内容有多少行，如果是单行应该就是社区版的配置，多行就是yum版
EXECSTART_NUM=`sed '/^\[Service\]$/,/^\[.*\]$/p' -n $DOCKER_SERVICE_PATH | sed '$d;/^$/d' | sed '/^ExecStart=/,/^[A-Z]/p' -n | sed '$d' | sed '$=' -n`
if [ $EXECSTART_NUM -eq 1 ]; then
    echo "社区版docker"
    # 处理社区版，获取在文件中的行数
    LINE_NUM=`sed '/^ExecStart=/=' -n $DOCKER_SERVICE_PATH`
    # 判断当前配置中是否包含目标配置，如果不包含，注释当前行，在下一行进行配置，包含则提示已存在，请核对
    TARGET_CONFIG='-H tcp://0.0.0.0:2376 -H unix://var/run/docker.sock -D --tlsverify --tlscert=/root/test/root/.docker/server-cert.pem --tlskey=/root/test/root/.docker/server-key.pem --tlscacert=/root/test/root/.docker/ca.pem'
    `sed "$LINE_NUMp" -n $DOCKER_SERVICE_PATH | sed "\#$TARGET_CONFIG#=" -n`
else
    echo "yum版docker"
fi
