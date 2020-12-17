#!/bin/bash
# 创建docker认证所需证书，统一放置在/root/.docker目录下

# 证书有效期
VALID_YEARS=10
# 证书存放路径
DOCKER_CERT_PATH=`pwd`/.docker
# docker.service配置文件路径
DOCKER_SERVICE_PATH=/lib/systemd/system/docker.service
# 证书认证密码
PASSWORD=123456


VALID_DAYS=$((VALID_YEARS * 365))
COUNTRY=CN
STATE=BJ
COMMON_NAME=localhost

# 根据网卡名称获取ip地址
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

# 拼接ip字符串
function getIPStr() {
    ix=1
    for ip in `getIP`
    do
        let ix+=1
        echo "IP.$ix = $ip\n"
    done
}

# 主机的ip字符串
IP_STR=$(echo `getIPStr` | sed 's/\\n\s/\\n/g')
HOSTNAME_STR="DNS.2 = `hostname`"
echo -e "本机ip为：\n`getIP`\nhostname为：`hostname`"

# 定义创建证书函数，第一个参数是文件名，第二个参数是创建语句
function makeCert() {
    if [ -f "$1" ]; then
        echo "$1 已存在，跳过"
    else
      # 执行命令并丢弃输出
        eval $2 &>> /dev/null
        if [ $? -ne 0 ]; then
            exit 1
        else
            echo "$1 创建成功"
        fi
    fi
}

echo "------开始生成证书------"

# 创建证书存放路径
if [ -d $DOCKER_CERT_PATH ]; then
    echo "$DOCKER_CERT_PATH 已存在，跳过"
else
    mkdir $DOCKER_CERT_PATH && cd $DOCKER_CERT_PATH
    echo "$DOCKER_CERT_PATH 创建成功"
fi

# 创建ca.srl
CA_SRL=$DOCKER_CERT_PATH/ca.srl
CA_SRL_SH="echo 01 > ca.srl"
makeCert $CA_SRL "$CA_SRL_SH"

# 创建ca-key.pem
CA_KEY_PEM=$DOCKER_CERT_PATH/ca-key.pem
CA_KEY_PEM_SH="openssl genrsa -des3 -passout \"pass:$PASSWORD\" -out ca-key.pem 2048"
makeCert $CA_KEY_PEM "$CA_KEY_PEM_SH"

# 创建ca.pem
CA_PEM=$DOCKER_CERT_PATH/ca.pem
CA_PEM_SH="openssl req -new -x509 -days $VALID_DAYS -key ca-key.pem -out ca.pem -passin \"pass:$PASSWORD\" -subj \"/C=$COUNTRY/ST=$STATE\""
makeCert $CA_PEM "$CA_PEM_SH"

# 创建server-key-tmp.pem
SERVER_KEY_TMP_PEM=$DOCKER_CERT_PATH/server-key-tmp.pem
SERVER_KEY_TMP_PEM_SH="openssl genrsa -des3 -passout \"pass:$PASSWORD\" -out server-key-tmp.pem 2048"
makeCert $SERVER_KEY_TMP_PEM "$SERVER_KEY_TMP_PEM_SH"

# 创建服务端配置文件server-cert.txt
SERVER_CERT_TXT=$DOCKER_CERT_PATH/server-cert.txt
SERVER_CERT_TXT_SH="echo -e \"[req]\ndistinguished_name = req_distinguished_name\nreq_extensions = v3_req\n\n[req_distinguished_name]\ncommonName = TypeCommonNameHere\n\n[v3_req]\nsubjectAltName = @alt_names\n\n[alt_names]\nIP.1 = 127.0.0.1\nDNS.1 = localhost\n$IP_STR$HOSTNAME_STR\" > server-cert.txt"
makeCert $SERVER_CERT_TXT "$SERVER_CERT_TXT_SH"

# 创建server.csr
SERVER_CSR=$DOCKER_CERT_PATH/server.csr
SERVER_CSR_SH="openssl req -subj \"/CN=$COMMON_NAME\" -passin \"pass:$PASSWORD\" -new -key server-key-tmp.pem -out server.csr -config server-cert.txt"
makeCert $SERVER_CSR "$SERVER_CSR_SH"

# 创建server-cert.pem
SERVER_CERT_PEM=$DOCKER_CERT_PATH/server-cert.pem
SERVER_CERT_PEM_SH="openssl x509 -req -passin \"pass:$PASSWORD\" -days $VALID_DAYS -in server.csr -CA ca.pem -CAkey ca-key.pem -out server-cert.pem -extensions v3_req -extfile server-cert.txt"
makeCert $SERVER_CERT_PEM "$SERVER_CERT_PEM_SH"

# 创建key-tmp.pem
KEY_TMP_PEM=$DOCKER_CERT_PATH/key-tmp.pem
KEY_TMP_PEM_SH="openssl genrsa -des3 -passout \"pass:$PASSWORD\" -out key-tmp.pem 2048"
makeCert $KEY_TMP_PEM "$KEY_TMP_PEM_SH"

# 创建client.csr
CLIENT_CSR=$DOCKER_CERT_PATH/client.csr
CLIENT_CSR_SH="openssl req -passin \"pass:$PASSWORD\" -subj '/CN=client' -new -key key-tmp.pem -out client.csr"
makeCert $CLIENT_CSR "$CLIENT_CSR_SH"

# 创建extfile.cnf
EXTFILE_CNF=$DOCKER_CERT_PATH/extfile.cnf
EXTFILE_CNF_SH="echo extendedKeyUsage = clientAuth > extfile.cnf"
makeCert $EXTFILE_CNF "$EXTFILE_CNF_SH"

# 创建cert.pem
CERT_PEM=$DOCKER_CERT_PATH/cert.pem
CERT_PEM_SH="openssl x509 -req -passin \"pass:$PASSWORD\" -days $VALID_DAYS -in client.csr -CA ca.pem -CAkey ca-key.pem -out cert.pem -extfile extfile.cnf"
makeCert $CERT_PEM "$CERT_PEM_SH"

# 创建server-key.pem
SERVER_KEY_PEM=$DOCKER_CERT_PATH/server-key.pem
SERVER_KEY_PEM_SH="openssl rsa -in server-key-tmp.pem -passin \"pass:$PASSWORD\" -out server-key.pem"
makeCert $SERVER_KEY_PEM "$SERVER_KEY_PEM_SH"

# 创建key.pem
KEY_PEM=$DOCKER_CERT_PATH/key.pem
KEY_PEM_SH="openssl rsa -in key-tmp.pem -passin \"pass:$PASSWORD\" -out key.pem"
makeCert $KEY_PEM "$KEY_PEM_SH"

echo "------生成证书结束------"

# 修改配置文件
echo "------开始配置------"
# 需要分开匹配，社区版的也就是用菜鸟教程安装的，一种是yum装的，配置文件内容不一致
# yum版存在多行
LINE_NUM=`sed '/^ExecStart=.*\\$/=' -n $DOCKER_SERVICE_PATH`
# 社区版，单行
COMMUNITY_LINE_NUM=`sed '/^ExecStart=\/usr\/bin\/dockerd -H fd:\/\/ --containerd=\/run\/containerd\/containerd.sock\s*$/=' -n $DOCKER_SERVICE_PATH`
if [[ $LINE_NUM && $LINE_NUM -gt 0 ]]; then
    # 注释之前的
    sed "$LINE_NUM s/^/#/" -i $DOCKER_SERVICE_PATH
    if [ $? -eq 0 ]; then
        echo "已注释第 $LINE_NUM 行的原有配置"
    else
        echo "注释第 $LINE_NUM 的配置失败"
        exit 1
    fi
    # 添加新值
    sed "$LINE_NUM a\ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock -H tcp://0.0.0.0:2376 -H unix://var/run/docker.sock -D --tlsverify --tlscert=$SERVER_CERT_PEM --tlskey=$SERVER_KEY_PEM --tlscacert=$CA_PEM" -i $DOCKER_SERVICE_PATH
    if [ $? -eq 0 ]; then
        echo "在第 $((LINE_NUM + 1)) 行增加配置"
    else
        echo "在第 $((LINE_NUM + 1)) 行增加配置失败"
        exit 2
    fi
    echo "配置成功"
else
    echo "配置已存在，按本次生成结果应配置为：ExecStart=/usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock -H tcp://0.0.0.0:2376 -H unix://var/run/docker.sock -D --tlsverify --tlscert=$SERVER_CERT_PEM --tlskey=$SERVER_KEY_PEM --tlscacert=$CA_PEM，请检查当前配置是否正确。"
fi
echo "------配置结束------"

# 修改权限为只读
echo "------修改权限为只读开始------"
chmod -v 400 ca-key.pem ca.pem ca.srl cert.pem client.csr key.pem key-tmp.pem server-cert.pem server.csr server-key.pem server-key-tmp.pem
echo "------修改权限结束------"

# 打包客户端证书ca.pem cert.pem key.pem
echo "------开始打包客户端证书------"
TARBALL_NAME="`hostname`-client-certs.tar.gz"
tar -czvf $TARBALL_NAME ca.pem key.pem cert.pem
echo "客户端证书文件已经打包 ==> `pwd`/$TARBALL_NAME"
echo "------打包客户端证书结束------"

# 重启docker
# 如果信任脚本，可以放开以下语句
echo "------重启docker服务------"
systemctl daemon-reload
systemctl restart docker
echo "------重启完成------"

# 处理防火墙端口
echo "------处理防火墙端口开始------"
firewall-cmd --state &> /dev/null
if [ $? -eq 0 ]; then
    echo "防火墙开启"
    firewall-cmd --zone=public --query-port=2376/tcp &> /dev/null
    if [ $? -eq 0 ]; then
        echo "2376端口已开放"
    else
        firewall-cmd --zone=public --add-port=2376/tcp --permanent &> /dev/null \
        && firewall-cmd --reload &> /dev/null
        if [ $? -eq 0 ]; then
            echo "成功开放2376 tcp端口"
        else
            echo "2376端口开放失败"
            exit 3
        fi
    fi
else
    echo "防火墙已关闭，2376端口可用"
fi
echo "------处理防火墙端口结束------"
