#!/bin/bash

# 查看java文件和properties文件的有效行数，去除空行和注释
find -name *.java -o -name *.properties | xargs cat | sed '/^$/d; /\//d; /\*/d; /#/d' | wc -l

