#!/bin/sh
# 将历史的提交，修改提交人和作者信息

git filter-branch --env-filter '

# 要修改的邮箱
OLD_EMAIL="luwentao@bonc.com.cn"

# 新的邮箱以及用户名
CORRECT_NAME="xiqiuyimeng"
CORRECT_EMAIL="847466569@qq.com"

if [ "$GIT_COMMITTER_EMAIL" = "$OLD_EMAIL" ]
then
    export GIT_COMMITTER_NAME="$CORRECT_NAME"
    export GIT_COMMITTER_EMAIL="$CORRECT_EMAIL"
fi
if [ "$GIT_AUTHOR_EMAIL" = "$OLD_EMAIL" ]
then
    export GIT_AUTHOR_NAME="$CORRECT_NAME"
    export GIT_AUTHOR_EMAIL="$CORRECT_EMAIL"
fi
' --tag-name-filter cat -- --branches --tags

git filter-branch -f --index-filter 'git rm --cached --ignore-unmatch Rakefile' HEAD
