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
#安装Nginx
安装
在Centos下，yum源不提供nginx的安装，可以通过切换yum源的方法获取安装。也可以通过直接下载安装包的方法，以下命令均需root权限执行：

首先安装必要的库（nginx 中gzip模块需要 zlib 库，rewrite模块需要 pcre 库，ssl 功能需要openssl库）。选定/usr/local为安装目录，以下具体版本号根据实际改变。

首先由于nginx的一些模块依赖一些lib库，所以在安装nginx之前，必须先安装这些lib库，这些依赖库主要有g++、gcc、openssl-devel、pcre-devel和zlib-devel 所以执行如下命令安装

    $   yum install gcc-c++ 
    $   yum install pcre pcre-devel 
    $   yum install zlib zlib-devel 
    $   yum install openssl openssl--devel  

安装之前，最好检查一下是否已经安装有nginx

    $   find -name nginx  

如果系统已经安装了nginx，那么就先卸载

    $   yum remove nginx  

首先进入/usr/local目录

    $   cd /usr/local  

从官网下载最新版的nginx

    $   wget http://nginx.org/download/nginx-1.7.4.tar.gz  

解压nginx压缩包

    $   tar -zxvf nginx-1.7.4.tar.gz  

会产生一个nginx-1.7.4 目录，这时进入nginx-1.7.4目录

    $   cd  nginx-1.7.4  

接下来安装，使用--prefix参数指定nginx安装的目录,make、make install安装

    $   ./configure  $默认安装在/usr/local/nginx  
    $   make 
    $   make install      

如果没有报错，顺利完成后，最好看一下nginx的安装目录

    $   whereis nginx  

安装完毕后，进入安装后目录（/usr/local/nginx）便可以启动或停止它了。

到此，使用CentOS安装nginx已经完成了，其实看看还是蛮简单的。

 

5.启动

$ /usr/local/nginx/sbin/nginx
检查是否启动成功：

打开浏览器访问此机器的 IP，如果浏览器出现 Welcome to nginx! 则表示 Nginx 已经安装并运行成功。

如果运行的时候不带-c参数，那就采用默认的配置文件，即/etc/nginx/nginx.conf

查看运行进程状态：
# ps aux | grep nginx

打开浏览器,访问http://localhost/看看nginx的默认页面:



部分命令如下：

重启：
$ /usr/local/nginx/sbin/nginx –s reload

停止：
$ /usr/local/nginx/sbin/nginx –s stop

测试配置文件是否正常：
$ /usr/local/nginx/sbin/nginx –t

强制关闭：
$ pkill nginx
#安装redis
#安装mysql
#安装rabbitmq
#安装fastfsd
#安装elasticsearch
1. 下载tar包，并上传tar包到linux服务器。

2. 解压tar包，并移动到/usr/local

3. 新建用户: 因为不能用root用户启动，所以要创建一个用户 `useradd leyou` 设置密码 `passwd leyou` 切换用户 `su - leyou`
 
4. 进入config修改jvm.options 修改内存的占用（lunce的底层使用java，调节jvm的设置）
      ```text
     -Xms512m
     -Xmx512m
      ```
5. 修改elasticsearch.yml，主要涉及日志和数据的存放位置和外部网络的访问
    ```text
     path.data: /home/leyou/elasticsearch/data # 数据目录位置
     path.logs: /home/leyou/elasticsearch/logs # 日志目录位置
     network.host: 0.0.0.0 # 绑定到0.0.0.0，允许任何ip来访问
    ```
    >在elasticsearch中还有很多节点信息供配置:
    
    | 属性名                             | 说明                                                         |
    | ---------------------------------- | ------------------------------------------------------------ |
    | cluster.name                       | 配置elasticsearch的集群名称，默认是elasticsearch。建议修改成一个有意义的名称。 |
    | node.name                          | 节点名，es会默认随机指定一个名字，建议指定一个有意义的名称，方便管理 |
    | path.conf                          | 设置配置文件的存储路径，tar或zip包安装默认在es根目录下的config文件夹，rpm安装默认在/etc/ elasticsearch |
    | path.data                          | 设置索引数据的存储路径，默认是es根目录下的data文件夹，可以设置多个存储路径，用逗号隔开 |
    | path.logs                          | 设置日志文件的存储路径，默认是es根目录下的logs文件夹         |
    | path.plugins                       | 设置插件的存放路径，默认是es根目录下的plugins文件夹          |
    | bootstrap.memory_lock              | 设置为true可以锁住ES使用的内存，避免内存进行swap             |
    | network.host                       | 设置bind_host和publish_host，设置为0.0.0.0允许外网访问       |
    | http.port                          | 设置对外服务的http端口，默认为9200。                         |
    | transport.tcp.port                 | 集群结点之间通信端口                                         |
    | discovery.zen.ping.timeout         | 设置ES自动发现节点连接超时的时间，默认为3秒，如果网络延迟高可设置大些 |
    | discovery.zen.minimum_master_nodes | 主结点数量的最少值 ,此值的公式为：(master_eligible_nodes / 2) + 1 ，比如：有3个符合要求的主结点，那么这里要设置为2 |
    |                                    |                                                              |

6. 创建上面的文件夹 `mkdir data`,`mkdir logs`

7. 进入到bin目录，输入./elasticsearch
8. 可能面临的问题
   * 内核过低
   * 文件权限不足
      1. 使用root用户赋权 sudo chown -R leyou:leyou /home/elasticsearch
      2. `max file descriptors [4096] for elasticsearch process likely too low, increase to at least [65536]` 这个错误需要打开 `/etc/security/limits.conf`添加下面的内容
         ```text
            * soft nofile 65536
            * hard nofile 131072
            * soft nproc 4096
            * hard nproc 4096
          ```
   * 线程数不够
   * 进程虚拟内存 打开sysctl.conf文件修改,如下图，修改后 `sysctl -p`
      ```text
        vm.max_map_count= 655360      
      ``` 

9. 测试安装是否成功 `http://服务器ip:9200`

# 安装tomcat
# 安装MariaDB
 1. 安装 `yum install mariadb-server `
 2. 启动 `systemctl start mariadb`
 3. 设置开机自启动 `systemctl enable mariadb`
 4. 首次设置mysql `mysql_source_installation`
 5. ``
#安装docker
 1. 安装Docker所需要的包 `yum remove docker \
                                     docker-client \
                                     docker-client-latest \
                                     docker-common \
                                     docker-latest \
                                     docker-latest-logrotate \
                                     docker-logrotate \
                                     docker-engine`
                                     
 2. 设置稳定的仓库 `yum-config-manager \
                 --add-repo \
                 https://download.docker.com/linux/centos/docker-ce.repo`
 
 3. 安装最新的docker引擎 `yum install docker-ce docker-ce-cli containerd.io`
 
 4. 启动docker `systemctl start docker`
 
 5. 查看docker的启动状态 `systemctl status docker`
 
##docker安装elk
 1. 安装docker后，安装docker-compose `curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose`
 -如果下载速度很慢的话，可以通过浏览器上github直接下载，上传到服务器。
 2. 对下载的二进制文件赋权 `chmod +x /usr/local/bin/docker-compose`
 
 3. 创建link `ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose`
 
 4. 查看是否安装成功 `docker-compose -v`
 
 5. 一些准备工作
    1.  Elasticsearch默认使用mmapfs目录来存储索引。操作系统默认的mmap计数太低可能导致内存不足，我们可以使用下面这条命令来增加内存：` sysctl -w vm.max_map_count=262144 `
    2. 创建Elasticsearch数据挂载路径： `mkdir -p /febs/elasticsearch/data`
    3. 对该路径进行赋权 `chmod 777 /febs/elasticsearch/data` 
    4. 创建Elasticsearch插件挂载路径 `mkdir -p /febs/elasticsearch/plugins`
    5. 创建Logstash配置文件存储路径 `mkdir -p /febs/logstash`
    6. 在该路径下创建logstash-febs.conf配置文件 `vim /febs/logstash/logstash-febs.conf`
        ```text
                input {
                  tcp {
                    mode => "server"
                    host => "0.0.0.0"
                    port => 4560
                    codec => json_lines
                  }
                }
                output {
                  elasticsearch {
                    hosts => "es:9200"
                    index => "febs-logstash-%{+YYYY.MM.dd}"
                  }
                }
        
        ```
       
    7. 创建ELK Docker Compose文件存储路径 `mkdir -p /febs/elk`
    8. 在该目录下创建docker-compose.yml文件 `vim /febs/elk/docker-compose.yml` 内容如下：
    
       ```yaml
        version: '3'
        services:
          elasticsearch:
            image: elasticsearch:6.4.1
            container_name: elasticsearch
            environment:
              - "cluster.name=elasticsearch" #集群名称为elasticsearch
              - "discovery.type=single-node" #单节点启动
              - "ES_JAVA_OPTS=-Xms512m -Xmx512m" #jvm内存分配为512MB
            volumes:
              - /febs/elasticsearch/plugins:/usr/share/elasticsearch/plugins
              - /febs/elasticsearch/data:/usr/share/elasticsearch/data
            ports:
              - 9200:9200
          kibana:
            image: kibana:6.4.1
            container_name: kibana
            links:
              - elasticsearch:es #配置elasticsearch域名为es
            depends_on:
              - elasticsearch
            environment:
              - "elasticsearch.hosts=http://es:9200" #因为上面配置了域名，所以这里可以简写为http://es:9200
            ports:
              - 5601:5601
          logstash:
            image: logstash:6.4.1
            container_name: logstash
            volumes:
              - /febs/logstash/logstash-febs.conf:/usr/share/logstash/pipeline/logstash.conf
            depends_on:
              - elasticsearch
            links:
              - elasticsearch:es
            ports:
              - 4560:4560 ```
 6. 切换到/febs/elk 目录下，使用命令启动 `docker-compose up -d`  
    1. 第一次启动时后，会拉取ELK的镜像，过程中可能出错。这是镜像网址太过老旧，应设置新的网址。在/etc/docker/目录下新建daemon.json，在里面输入
        ```json
        {
            "registry-mirrors":["https://docker.mirrors.ustc.edu.cn"]
        }
        ```
    2. 重启守护线程 `systemctl daemon-reload` `systemctl restart docker`
    3. systemctl enable docker.service