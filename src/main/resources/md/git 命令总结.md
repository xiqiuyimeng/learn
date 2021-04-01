1. 本地 git 操作：

    1. git 仓库：
        - 建立 git 仓库：`git init`
        - 取消 git 仓库：`rm -rf .git`
    2. git 管理文件：
        - 将文件添加到 git 管理：`git add .`（可以指定文件，“.”为所有）
        - 取消添加到 git 的文件：`git reset`
        - 查看工作区状态：`git status`
    3. git 本地仓库操作：
        - 将本地被管理的文件添加到本地仓库：`git commit -m"message"`
        - 撤销提交（head^代表上个版本，可以连用，也可以指定版本号）：
            - `git reset HEAD^`，即为撤销到提交的前一个版本，默认包含参数`--mixed`，撤销添加和提交，但是不删除工作区改动。
            - `git reset --soft HEAD^`，撤销提交，不撤销添加，不删除工作区改动。
            - `git reset --hard HEAD^`，撤销提交和添加，并且删除工作区改动，慎用！
            - 只是修改注释：`git commit --amend`
    4. git 本地暂存区：
        - 将工作区改动暂存至暂存区：`git stash`
        - 从暂存区取出暂存内容：`git stash pop` （后面可接参数，暂存区的版本号，可任意恢复）
        - 查看暂存区列表：`git stash list`
    5. git 本地分支：
        - 切换分支：
            - 切换一个已有分支：`git checkout branch_name`  or `git switch branch_name`
            - 切换到一个新分支：
                - 会带有之前分支历史信息：`git checkout -b branch_name`
                - 创建不带有分支历史信息的全新分支：`git checkout --orphan branch_name`（实际为空，所以开始看不到，只有在提交以后才能看到）
                - 从某次提交历史中切出一个新分支：`git checkout -b branch_name version_id`
        - 查看分支：`git branch`
        - 删除分支：`git branch -d branch_name`
        - 重命名分支：`git branch -m old new`（不影响与远程分支对应关系）
        - 合并分支：`git merge branch_name` （将分支合并到当前分支）
        - 挑选其他分支提交添加到当前分支：`git cherry-pick commit_version`（commit_version为版本库中某一次提交的版本号）

2. git 远程仓库：

    1. git 远程仓库：

        - 将本地仓库与远程仓库关联：`git remote add origin git_address`
        - 本地不存在仓库，从远程克隆：`git clone git_address`
        - 删除远程仓库关联：`git remote remove origin`
        - 远程仓库信息：`git remote -v` （fetch代表拉取权限，push代表推送权限）

    2. git 远程分支操作：

        - 查看所有分支：`git branch -a`
        - 查看远程分支：`git branch -r`
        - 查看远程分支和本地分支对应关系：`git branch -vv`（参数为两个v）
        - 将本地分支推送到远程：`git push -u origin master` （第一次需要加 -u，-u为短选项，与长选项--set-upstream同一个意思）
        - 在远程创建分支：
            1. 在远程创建一个和本地名字一样的分支并推送：`git push --set-upstream origin branch_name` （这里的远程分支名字不需要加前缀 origin/，并且会使远程和本地分支建立联系， --set-upstream可以替换为-u）
            2. 将本地分支推送到远程分支，名字任意：`git push origin branch_name:branch_name`（若第一个branch_name为空，等同于删除远程分支，此操作不会使远程分支与本地分支建立联系）
        - 从远程分支拉取到本地：`git checkout -b branch_name origin/branch`（会建立远程分支与本地分支联系，如果不是同一分支最好解除联系）
        - 远程分支与本地分支的联系：
            1. 建立联系：`git branch --set-upstream-to origin/branch_name`（同样可以用 -u 代替长选项）
            2. 取消联系：`git branch --unset-upstream`

        - 获取远程分支的所有变化：`git fetch`
        - 拉取远程分支的改变：`git pull`
        - 删除远程分支：`git push origin -d branch_name` or `git push origin :branch_name`