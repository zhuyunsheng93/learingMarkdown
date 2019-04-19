#如何将本地的git推送到远端
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
##远端存在
