#!/bin/bash

# 从目标服务器将镜像导出为tar包，然后传回本地，加载为镜像，推送到远程，并对应修改数据库记录

# 传入参数：镜像id或镜像名称
if [[ $1 ]]; then
	echo "需要导出的镜像为：$1"
	image=$1
else
	echo "缺少镜像信息，镜像id或镜像名"
	exit -1
fi

# 镜像所在服务器信息，user@host
login_info="centos121"

# 导入镜像的仓库名
repo='harbor.centos121.com/library/test'
# 导入镜像的tag
tag='v1.1'
# 导入镜像的名称
image_name=$repo:$tag

# 登录镜像所在服务器
login="ssh $login_info"

# 镜像保存的tar名称
tarfile=`date +%F-%H`-model.tar

# 远程执行脚本
$login 'bash -s' << EOF 
echo "将要导出的镜像是：$image，保存的tar包名称是：$tarfile"
# 如果文件存在，则删除
if [ -f $tarfile ]; then
	echo "删除文件 $tarfile"
	rm -f $tarfile
fi

# 导出镜像，保存为tar
echo "导出镜像"
docker save > $tarfile $image
EOF

# 如果本地文件存在，则删除
if [ -f $tarfile ]; then
	echo "删除文件 $tarfile"
	rm -f $tarfile
fi

# 传到本地
remote_home=`$login "pwd"`
scp $login_info:$remote_home/$tarfile .

# 删除远程文件
echo "删除远程文件：$tarfile"
$login "rm -f $tarfile"

# 在本地导入
echo "导入镜像"
loaded=`docker load < $tarfile`
# 使用echo可以将导入产生的日志合并为一行，方便提取
loaded_tag=`echo $loaded | awk -F 'Loaded image: ' '{print$2}'`
loaded_id=`echo $loaded | awk -F 'Loaded image ID: sha256:' '{print$2}'`
if [[ $loaded_tag ]];then
	echo "镜像名称是：$loaded_tag"
	# tag 镜像
	docker tag $loaded_tag $image_name
	# 删除原镜像标签
	docker rmi $loaded_tag
elif [[ $loaded_id ]]; then
	echo "镜像id是：$loaded_id"
	# tag 镜像
	docker tag $loaded_id $image_name
fi

# 删除本地文件
echo "删除本地文件：$tarfile"
rm -f $tarfile

# 推送远程
echo "开始向远程仓库推送镜像: $image_name"
docker push $image_name

# ************************************************** 分割线，数据库操作需谨慎 **************************************************
# 数据库信息
mysql_host=centos121
mysql_port=3306
mysql_user=root
mysql_pwd=admin
mysql_db=test

# 更新数据库
echo "数据库中的原始镜像信息为："
\mysql -P $mysql_port -h $mysql_host -u $mysql_user -p$mysql_pwd 2>/dev/null -e "use $mysql_db;SELECT concat(i.image_addr,':', v.tag)as image FROM ds_t_model_imagemanagement_version v left join ds_t_model_imagemanagement_image i on i.id = v.image_id where i.image_type=5;update ds_t_model_imagemanagement_version v LEFT JOIN ds_t_model_imagemanagement_image i on i.id = v.image_id set v.tag = '$tag' where i.image_type = 5;"
echo "更新后的镜像信息为："
\mysql -P $mysql_port -h $mysql_host -u $mysql_user -p$mysql_pwd 2>/dev/null -e "use $mysql_db;SELECT concat(i.image_addr,':', v.tag)as image FROM ds_t_model_imagemanagement_version v left join ds_t_model_imagemanagement_image i on i.id = v.image_id where i.image_type=5"
