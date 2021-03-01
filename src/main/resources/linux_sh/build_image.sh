#!/bin/bash

# 批量构建镜像，假设已经存在多个Dockerfile文件，可按此脚本批量进行构建，需将脚本放在Dockerfile文件所在目录下。
# Dockerfile名称规则：Dockerfile-v1、Dockerfile-v2 ...
# 构建后的镜像仓库名为 IMAGE_REPOSITORY，标签为 Dockerfile-的后半部分。例如：test:v1，test:v2 ...

# 镜像的仓库名
IMAGE_REPOSITORY=test
# dockerfile存在的目录
DOCKERFILE_DIR=.
# 基础镜像tar名
base_tar=jre8.tar


function testSuccess(){
    # 判断是否执行成功
	if [ $? -ne 0 ]; then
		echo "----------$1 失败----------"
		echo
		exit 1
	else
		echo "----------$1 成功----------"
	fi
}

# 获取基础镜像tar
function ungz(){
    if [ -f $base_tar ];then
        echo "基础镜像tar存在"
    else
        echo "获取基础镜像tar"
        tar -zxvf $base_tar.gz
    fi
}

# 导入基础镜像
any_dockerfile=`ls -R $DOCKERFILE_DIR | grep Dockerfile | head -n1 | xargs find -name`
base_image=`cat $any_dockerfile | awk '/^FROM/{print$2}'`
base_search=`docker images | awk '{printf("%s:%s\n",$1,$2)}' | grep $base_image`
if [[ $base_search && $base_search == $base_image ]];then
    echo "$base_image 基础镜像已存在"
else
    echo "开始导入基础镜像"
    ungz
    docker load < $base_tar
fi


for file in `ls -R $DOCKERFILE_DIR | grep Dockerfile`
do
image=$IMAGE_REPOSITORY:`echo $file | cut -d- -f2`
echo
echo "**********$image 镜像构建开始**********"
# docker images | awk '$1 != "REPOSITORY" && $2 != "TAG" {printf("%s:%s\n",$1,$2)}' 等效于
# docker images | sed '2,$p' -n | awk '{printf("%s:%s", $1, $2)}' 等效于
# docker images | sed '2,$p' -n | awk '{print$1,$2}' OFS=":"
search=`docker images | awk '$1 != "REPOSITORY" && $2 != "TAG" {printf("%s:%s\n",$1,$2)}' | grep "$image"`
if [[ $search && $search == $image ]]; then
    echo "$image 镜像已存在，跳过"
else
    dockerfile=`find -name $file`
	docker build -t $image -f $dockerfile .
	testSuccess "构建镜像$image"
fi
echo "**********$image 镜像构建结束**********"
echo
done
