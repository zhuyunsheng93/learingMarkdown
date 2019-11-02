#防火墙
* 查看防火墙开放端口状态 `firewall-cmd --list-all`

* 开启防火墙(暂时开启) `systemctl start firewalld.service`

* 关闭防火墙(暂时关闭) `systemctl stop firewalld.service`

* 永久关闭防火墙(重启后也是关闭) `systemctl disable firewalld.service`

* 永久开启防火墙(重启后自动启动) `systemctl enable firewalld.service`

* 重启防火墙 `firewall-cmd --reload`

* 开放指定的端口 `sudo firewall-cmd --add-service=http --permanent` `sudo firewall-cmd --add-port=80/tcp --permanent`

#内存使用
#进程使用和管理
#安装软件
#卸载软件
#安装jdk
1、自定义安装jdk
* 自己可用windows电脑来oracle官网下载符合自己情况的jdk包(tar包)
* 解压jdk压缩包  `tar -xvf  ******.tar.gz`
* 将解压的包到 /usr/local/  这个目录中
* 设置环境变量  
    * 打开 /etc/profilesm,并进行编辑，并将下面的设置环境变量
```text
JAVA_HOME=/usr/local/java/jdk1.8.0_171
CLASSPATH=.:$JAVA_HOME/lib.tools.jar
PATH=$JAVA_HOME/bin:$PATH
export JAVA_HOME CLASSPATH PATH
``` 
* 安装中的错误
`bad ELF interpreter: No such file or directory`,这个错误意味，你的jdk版本和linux的版本不同.
  
2 使用centos的云资源库自动安装()
* 注意,我在使用这个云资源库中的openjdk时,我在启动elasticsearch时,会报错(elasticsearch: OpenJDK 64-Bit Server VM warning: If the number of processors is expected to increase from one, then you should configure the number of parallel GC threads appropriately using -XX:ParallelGCThreads=N)
更换oracle的jdk后没有报错.
#安装redis
#安装mysql
#安装rabbitmq
#安装fastfsd
#安装elasticsearch
# 安装tomcat


