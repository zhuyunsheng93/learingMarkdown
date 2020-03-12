#Nginx的用途
Nginx是高性能的Web和反向代理服务器
* 反向代理 
* 负载均衡
* 动态路由
* 请求过滤
```nginx
#user  nobody;
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
   
    keepalive_timeout  65;

    gzip  on;
	server {
        listen       80;#监听的端口号
        server_name  manage.leyou.com;#监听的域名

        proxy_set_header X-Forwarded-Host $host;#下面三个是头信息
        proxy_set_header X-Forwarded-Server $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        location / {  #请求映射规则，/表示映射一切请求路径
			proxy_pass http://127.0.0.1:9001; #代理转发，所有80端口访问和manage.leyou.com都会转发给到这个网址
			proxy_connect_timeout 600;
			proxy_read_timeout 600;
        }
    }
	server {
        listen       80;
        server_name  api.leyou.com;

        proxy_set_header X-Forwarded-Host $host;
        proxy_set_header X-Forwarded-Server $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        location / {
			proxy_pass http://127.0.0.1:10010;
			proxy_connect_timeout 600;
			proxy_read_timeout 600;
        }
    }
}
```
#启动Nginx
* 下载Nginx，然后解压。
* 在当前解压目录，执行以下命令，完成操作：
*  `start nginx` 启动nginx。 `nginx.exe -s stop` 停止nginx  `nginx.exe -s reload` 重启nginx  
# Nginx配置文件

#路径拦截规则
nginx中拦截指定的请求路径映射到相对应的服务器的拦截规则是哪个路径长就会优先拦截。
```editorconfig
    location / {
			root   html;
            index  index.html index.htm;
			proxy_pass http://127.0.0.1:10010;
			proxy_connect_timeout 600;
			proxy_read_timeout 600;
        }
        location /api/upload {    #这个的拦截优先等级比上面高
		    proxy_pass http://127.0.0.1:8082;
			proxy_connect_timeout 600;
			proxy_read_timeout 600;
			rewrite "^/api/(.*)$" /$1 break;
			        # "用来匹配路径的正规" 重写后的路径
		}
```
