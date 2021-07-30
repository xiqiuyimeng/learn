#!/usr/bin/expect
# expect 自动登录mysql脚本
# 设置变量语法：set var value
# value 可以取自shell变量，语法为：[lindex $argv 0]，变量索引从0开始

set env_name [lindex $argv 0]
# 超时设为用不超时
set timeout -1
set host [lindex $argv 1]
set user [lindex $argv 2]
set port [lindex $argv 3]
set password [lindex $argv 4]

# send_user 可以打印变量，类似echo
send_user "开始登录$env_name的mysql服务：host：$host, user：$user, port：$port\n"

# spawn 开始交互命令
spawn mysql -u$user -h$host -P$port -p
# 判断返回值中是否包含 *assword*
expect "*assword*"
# 发送密码
send "$password\r"
# 登录完成，进入用户交互模式
interact
