#!/bin/bash

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
