#!/bin/bash
DOCKER_SERVICE_PATH=`pwd`/docker-config
# 获取service这一节点的内容
# 获取 ExecStart 配置内容有多少行，如果是单行应该就是社区版的配置，多行就是yum版
EXECSTART_NUM=`sed '/^\[Service\]$/,/^\[.*\]$/p' -n $DOCKER_SERVICE_PATH | sed '$d;/^$/d' | sed '/^ExecStart=/,/^[A-Z]/p' -n | sed '$d' | sed '$=' -n`
LINE_NUM=`sed '/^ExecStart=/=' -n $DOCKER_SERVICE_PATH`
SERVER_CERT_PEM='/root/.docker/server-cert.pem'
SERVER_KEY_PEM='/root/.docker/server-key.pem'
CA_PEM='/root/.docker/ca.pem'
TARGET_CONFIG="-H tcp://0.0.0.0:2376 -H unix://var/run/docker.sock -D --tlsverify --tlscert=$SERVER_CERT_PEM --tlskey=$SERVER_KEY_PEM --tlscacert=$CA_PEM"
if [ $EXECSTART_NUM -eq 1 ]; then
    # 处理社区版，获取在文件中的行数
    # 判断当前配置中是否包含目标配置，如果不包含，注释当前行，在下一行进行配置，包含则提示已存在，请核对
    CONFIGURED=`sed "$LINE_NUM p" -n $DOCKER_SERVICE_PATH | sed "\#$TARGET_CONFIG#=" -n`
    if [[ $CONFIGURED && $CONFIGURED -gt 0 ]]; then
        echo "配置已存在，请核对配置中是否包含 $TARGET_CONFIG"
    else
        echo "配置不存在，开始进行配置"
        OLD_CONFIG=`sed "$LINE_NUM p" -n $DOCKER_SERVICE_PATH`
        # 注释之前的配置
        sed "$LINE_NUM s/^/#/" -i $DOCKER_SERVICE_PATH
        if [ $? -eq 0 ]; then
            echo "已注释第 $LINE_NUM 行的原有配置"
        else
            echo "注释第 $LINE_NUM 的配置失败"
            exit 4
        fi
        # 添加新值
        sed "$LINE_NUM a\\$OLD_CONFIG -H tcp://0.0.0.0:2376 -H unix://var/run/docker.sock -D --tlsverify --tlscert=$SERVER_CERT_PEM --tlskey=$SERVER_KEY_PEM --tlscacert=$CA_PEM" -i $DOCKER_SERVICE_PATH
        if [ $? -eq 0 ]; then
            echo "在第 $((LINE_NUM + 1)) 行增加配置"
        else
            echo "在第 $((LINE_NUM + 1)) 行增加配置失败"
            exit 2
        fi
        echo "配置成功"
    fi
else
    END_LINE_NUM=`expr $LINE_NUM + $EXECSTART_NUM - 1`
    echo "ExecStart配置项开始行号：$LINE_NUM，结束行号：$END_LINE_NUM"
    # 判断是否已经配置
    CONFIGURED=`sed "$LINE_NUM,$END_LINE_NUM p" -n $DOCKER_SERVICE_PATH | sed "\#$TARGET_CONFIG#=" -n`
    if [[ $CONFIGURED && $CONFIGURED -gt 0 ]]; then
        echo "配置已存在，请核对配置中是否包含 $TARGET_CONFIG"
    else
        echo "配置不存在，开始进行配置"
        # 在原配置下一行，拷贝原配置一份，注释原配置，将证书配置添加于新配置处。
        OLD_CONFIG=`sed "$LINE_NUM,$END_LINE_NUM p" -n $DOCKER_SERVICE_PATH`
        sed "$LINE_NUM,$END_LINE_NUM s/^/#/" -i $DOCKER_SERVICE_PATH
        if [ $? -eq 0 ]; then
            echo "已注释 $LINE_NUM 行 到 $END_LINE_NUM 行的原有配置"
        else
            echo "注释 $LINE_NUM 行 到 $END_LINE_NUM 行的配置失败"
            exit 4
        fi
        sed "$END_LINE_NUM a\\$OLD_CONFIG -H tcp://0.0.0.0:2376 -H unix://var/run/docker.sock -D --tlsverify --tlscert=$SERVER_CERT_PEM --tlskey=$SERVER_KEY_PEM --tlscacert=$CA_PEM" -i $DOCKER_SERVICE_PATH \
        && sed "$((END_LINE_NUM + 1)), $((END_LINE_NUM + $EXECSTART_NUM - 1)) s/$/\\\/" -i $DOCKER_SERVICE_PATH
        if [ $? -eq 0 ]; then
            echo "在第 $((END_LINE_NUM + 1)) 行增加配置 $EXECSTART_NUM 行"
        else
            echo "在第 $((END_LINE_NUM + 1)) 行增加配置失败"
            exit 2
        fi
        echo "配置成功"
    fi
fi