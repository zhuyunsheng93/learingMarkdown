#Nginx安装
##Linux安装
1、 上传nginx的tar包到linux系统

2、 tar -xvf  tar包名进行解压

3、 配置安装的路径  `./configure --prefix=/opt/nginx --sbin-path=/usr/bin/nginx`

4、 编译安装  `make && make install` 编译安装

5、 日常操作

  - 启动  `nginx` 
  - 查看启动状态  `ps -ef | grep nginx`
  - 停止nginx `nginx -s stop`
  - 重新加载 `nginx -s reload`
  
 6、编辑配置文件