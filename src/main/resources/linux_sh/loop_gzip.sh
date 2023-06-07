#!/bin/bash

# 循环处理日志文件，因为当前日志文件会自动压缩为gz格式


# gzip解压用法，将test.gz文件解压为test.log
gz_file='test.gz'
gzip -dc gz_file > test.log

# 日志文件都是按日期处理的，所以需要生成序列，例如有
# 2023-06-01.log.gz, 2023-06-02.log.gz, 2023-06-03.log.gz, 2023-06-04.log.gz
# 那么可以for循环处理
for i in {1..4}
do
  gzip -dc 2023-06-0$i.log.gz > 2023-06-0$i.log
done
