#!/bin/bash

# 批量构建镜像，假设已经存在多个Dockerfile文件，可按此脚本批量进行构建，需将脚本放在Dockerfile文件所在目录下。
# Dockerfile名称规则：Dockerfile-v1、Dockerfile-v2 ...
# 构建后的镜像仓库名为 IMAGE_REPOSITORY，标签为 Dockerfile-的后半部分。例如：test:v1，test:v2 ...

# 镜像的仓库名
IMAGE_REPOSITORY=test

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


for file in `ls Dockerfile*`
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
	docker build -t $image -f $file .
	testSuccess "构建镜像$image"
fi
echo "**********$image 镜像构建结束**********"
echo
done
