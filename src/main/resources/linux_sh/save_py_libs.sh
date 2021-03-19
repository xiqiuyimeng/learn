#!/bin/bash

# ----------------------------------------------------------------------
# 脚本用以导出指定的python类库，操作方式为，搜寻指定类库的安装位置，
# 将其与所依赖的类库一并导出，将会递归进行处理，最终得到的压缩包，
# 可以一键解压到新的同版本python环境中使用，省去安装过程。
# ----------------------------------------------------------------------


if [ $# -eq 0 ];then
	echo "没有参数，退出"
	exit -2
else
	echo "共传递 $# 个参数"
	echo "$@"
fi


# 定义函数，循环处理，使用pip show找到该库信息，取出所依赖的库，继续循环
function findPyLibs(){
	# 获取类库信息，去除空格
	local py_info=`pip show $1 | sed 's/[[:space:]]//g'`
	# 判断是否有不存在的库
	if test -z "$py_info"; then
		echo "未搜索到 $1 类库信息,跳过"
		continue
	fi
	temp_array+=("$1")
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


echo "-------------------------------------------------- 开始搜索类库 --------------------------------------------------"
for py_lib in $@
do
	echo "**************************************** 开始搜索 $py_lib 类库 ****************************************"
	findPyLibs $py_lib
	echo -e "共 ${#temp_array[*]} 个依赖库与 $py_lib 类库相关："
	for p in ${temp_array[@]}
	do
		lib_dir=$(ls $lib_path | grep -i $p)
		lib_dirs+=("`echo $lib_dir`")
		echo -e "\t$p"
		echo -e "\t目录："
		echo "$lib_dir" | sed 's/^/&\t\t/g'
	done
	py_libs+=(${temp_array[@]})
	# 重置
	unset temp_array
	echo "**************************************** 搜索 $py_lib 类库结束 ****************************************"
done
echo "-------------------------------------------------- 搜索类库结束 --------------------------------------------------"
echo "类库安装路径是：$lib_path"
echo "共 ${#py_libs[*]} 个类库"
echo "类库：${py_libs[@]}"
echo

# 进入安装目录
cd $lib_path

echo "最终需要压缩的目录为：\n`echo ${lib_dirs[@]} | sed 's/ /\n/g'`"

echo "-------------------------------------------------- 开始压缩 --------------------------------------------------"
tar_name=py_lib.tgz
echo "${lib_dirs[@]}" | xargs tar -czf "$tar_name"
echo "压缩包路径：$lib_path/$tar_name"
echo "-------------------------------------------------- 压缩结束 --------------------------------------------------"

