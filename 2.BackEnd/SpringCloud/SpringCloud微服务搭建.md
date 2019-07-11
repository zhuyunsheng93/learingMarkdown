# Eureka Server搭建步骤
1、 创建过程，和普通springBoot创建过程大致相同，但在创建的过程中，选择默认配置依赖时，选择Spring Cloud Discovery，选择Eureka Server 时，pom文件会引入SpringCloud的依赖管理
```xml
  <!-- Eureka服务端 -->
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
<dependencyManagement>
		<dependencies>
            <!-- SpringCloud依赖，一定要放到dependencyManagement中，起到管理版本的作用即可 -->
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
```
2、添加EurekaServe依赖
```xml
<!-- Eureka服务端 -->
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>
		
```
3、application.yml配置
```yml
server:
  port: 10086
spring:
  application:
    name: eureka-server
eureka:
  client:
    register-with-eureka: false # 是否注册自己的信息到EurekaServer，默认是true
    fetch-registry: false # 是否拉取其它服务的信息，默认是true
    service-url: # EurekaServer的地址，现在是自己的地址，如果是集群，需要加上其它Server的地址。
      defaultZone: http://127.0.0.1:${server.port}/eureka
```
4、打开网址`http://127.0.0.1:10086`进入Eureka Serve控制页面
#服务注册到Eureka Server
1、创建服务的过程和创建SpringBoot很相似，只是在选择依赖的时候，选择一个Eureka Client依赖。
同样也会引入SpringClound的依赖，创建后的pom.xml会有如下：
```xml
<!--Eureka Client-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <!--SpringCloud的依赖-->
      <dependencyManagement>
        <dependencies>
          <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
          </dependency>
        </dependencies>
      </dependencyManagement>
```
2、erureka-client 的application.yml
```yaml
server:
  port: 8081
spring:
  application:
    name: user-service-demo
  datasource:
    password: root
    username: root
    url: jdbc:mysql://localhost:3306/leyou
    hikari:
      maximum-pool-size: 20
      minimum-idle: 10
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:   #这个地址是Eureka Server的地址，注意一定要加上eureka
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    prefer-ip-address: true #当调用getHostname获取实例的hostname时，返回ip而不是host名称
    ip-address: 127.0.0.1 # 指定自己的ip信息，不指定的话会自己寻找
mybatis:
  type-aliases-package: com.leyou.userservicedemo.pojo
```
服务端怎么注册到eureka server中？ 就是靠service-url这个，将eureka server的地址信息，告诉eureka client，服务端就会发送注册信息给服务端  
3、开启客户端功能 在启动类添加 `@EnableDiscoveryClient` `@@EnableEurekaClient`  
#构建服务集群
多个eureka server之间可以互相注册为服务，当服务提供者注册到eureka server集群中的某个节点，该节点就会同步到其他的server中，访问的任一节点时，得到的都是全部的服务信息。
## 搭建EurekaServer集群
1、搭建有两个eureka server的集群，端口分别为10086和10087
* 修改原EurekaServer的配置：
```yml
server:
  port: 10086 # 端口
spring:
  application:
    name: eureka-server # 应用名称，会在Eureka中显示
eureka:
  client:
    service-url: # 配置其他Eureka服务的地址，而不是自己，比如10087
      defaultZone: http://127.0.0.1:10087/eureka
```
`注意:所谓高可用注册中心，其实就是把EurekaServer自己作为一个服务进行注册，故：`
* register-with-eureka: true   fetch-registry: true 都要添加上。
* 把service-url的值改成另外一台EurekaServer的地址。而不是自己。这样就把自己注册到另一个Eureka-server中。
## 服务提供者配置修改
  ```yaml
eureka:
  client:
    service-url: # EurekaServer地址,多个地址以','隔开
      defaultZone: http://127.0.0.1:10086/eureka,http://127.0.0.1:10087/eureka
```