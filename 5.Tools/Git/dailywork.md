# 如何将本地的git推送到远端
## 本地存在项目 推送到远端
1. 注册github账号
2. 在本机生成SSH Key

   ssh-keygen -t rsa -C "youremail@example.com"
3. 在本机中找到id_rsa.pub 中的SSH Key添加到自己的github中
4. 在github创建一个新的git仓库
5. 建立git追踪

   git init
6. 在本地中将已经存在的项目和github中的项目进行关联

    git remote add origin 自己项目的地址
7. 添加自己的修改

   git add . 
   
   git commit -m "填写自己修改的信息"
7. 推送自己的项目到远端

   git push -u origin master(u 这个参数是因为新建项目为空。不为空后可以不用添加)
## 远端存在
 1. 将远端的项目下载
 
    git clone 项目地址
 2. 更新自己的本地项目
 
    git pull（无需再进行配置就可以更新代码）
# 添加.gitignore后.idea文件夹下文件还能被追踪
* 原因是因为，在上传项目的时候就已经被追踪，后续虽添加.gitignore后，但是还是被追踪
* 解决办法  `git rm -r --cached .idea`清除.idea的git缓存
# 创建分支
- 本地创建分支，推送到服务器  
`git checkout -b 分支名`  
`git push origin 分支名:分支名`
- 删除分支  
`git push origin :分支名` 前面的为空就是删除。
`git push origin --delete 分支名`
- 注意点  
origin 这个是默认远端名称，根据自己的命名来进行选择。

