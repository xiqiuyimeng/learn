#!/bin/bash

# ----------------------------------------------------------------------
# 脚本用以导出指定的python类库，操作方式为，搜寻指定类库的安装位置，
# 将其与所依赖的类库一并导出，将会递归进行处理，最终得到的压缩包，
# 可以一键解压到新的同版本python环境中使用，省去安装过程。
# ----------------------------------------------------------------------


# 要导出的类库名
py_lib=flask


num=0
# 定义函数，循环处理，使用pip show找到该库信息，取出所依赖的库，继续循环
function findPyLibs(){
	py_libs[$num]="$1"
	((num++))
	# 获取类库信息，去除空格
	local py_info=`pip show $1 | sed 's/[[:space:]]//g'`
	# 获取安装文件夹,所有的依赖都会装在一个目录下，找到此目录即可
	lib_path=`echo "$py_info" | sed /^Location/p -n | cut -d: -f2`
	# 获取到依赖的库
	local require=`echo "$py_info" | sed /^Requires/p -n | cut -d: -f2`
	if [[ $require ]]; then
		# 如果存在依赖库，则循环
		for py_lib_temp in $(echo $require | sed 's/,/ /g')
		do
			findPyLibs $py_lib_temp
		done
	fi
}

findPyLibs $py_lib
echo "安装路径是：$lib_path"
echo "共 ${#py_libs[*]} 个依赖"

for temp in ${py_libs[@]}
do
echo "依赖：$temp"
done
