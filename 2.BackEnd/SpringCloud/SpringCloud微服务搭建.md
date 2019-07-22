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
    fetch-registry: true  #从注册中心获取注册的服务。
    service-url:   #这个地址是Eureka Server的地址，注意一定要加上eureka
      defaultZone: http://127.0.0.1:10086/eureka
  instance:
    prefer-ip-address: true #当调用getHostname获取实例的hostname时，返回ip而不是host名称
    ip-address: 127.0.0.1 # 指定自己的ip信息，不指定的话会自己寻找
    lease-expiration-duration-in-seconds: 90  # 服务超过这个时间不向server发送心跳，就会从服务列表剔除。
    lease-renewal-interval-in-seconds: 30   # 这个时间间隔，服务向server发送心跳。
mybatis:
  type-aliases-package: com.leyou.userservicedemo.pojo
```
服务端怎么注册到eureka server中？ 就是靠service-url这个，将eureka server的地址信息，告诉eureka client，服务端就会发送注册信息给服务端  
3、开启客户端功能 在启动类添加 `@EnableDiscoveryClient`（用这个注解，其他类型微服务中心也可以发现） `@@EnableEurekaClient`  
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
* 注册到多个注册中心
 ```yaml
eureka:
  client:
    service-url: # EurekaServer地址,多个地址以','隔开
      defaultZone: http://127.0.0.1:10086/eureka,http://127.0.0.1:10087/eureka #如果是集群的话，只要注册一个eureka服务器上就可以
```
* 修改实列名称  
 1、默认的格式是：${hostname} + ${spring.application.name} + ${server.port}
 2、instance-id是区分同一服务的不同实例的唯一标准，因此不能重复。
 3、通过下面配置修改  
```yaml
 eureka:
   instance:
     instance-id: ${spring.application.name}:${server.port}
```
* 服务列表  
1、 `registry-fetch-interval-seconds` 为`true`，默认情况下会向注册中心读取注册的服务，时间间隔为`30s`(生产环境就不要修改，开发环境为了自己能更快看到现象，可以将时间修改的短一点儿)，自定义修改如下：
```yaml
eureka:
  client:
    registry-fetch-interval-seconds: 5
```
## 对服务的失效剔除和自我保护
* 服务因为一些不可抗原因，导致不正常下线，注册中心需要及时将这些服务进行剔除，注册中心会默认开启一个定时任务，每隔60s就将所有失效的服务进行剔除。自定义的话就要修改 `eureka.server.eviction-interval-timer-in-ms`
```yaml
eureka:
  server:
    enable-self-preservation: false # 关闭自我保护模式（缺省为打开）
    eviction-interval-timer-in-ms: 1000 # 扫描失效服务的间隔时间（缺省为60*1000ms）
```
#负载均衡Robbin
 同样功能的服务可能部署在多个服务器上，启动多个实例，用户使用服务时，就可以分布到多个服务器上，分担彼此压力，但请求如何选择哪一台服务器呢？这就需要负载均衡来帮你。
 ## 开启负载均衡
 因为Eureka集成了Ribbon,无需引入新的依赖，直接修改代码 (创建远端请求类时，加入@LoadBalanced) 
 ```java
 @Bean
 @LoadBalanced
 public RestTemplate restTemplate() {
     return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
 }
 ```
 * 修改调用方式，不在手动获取ip和端口，而是直接通过服务名称调用
 ```java
 @Service
 public class UserService {
 
     @Autowired
     private RestTemplate restTemplate;
 
     @Autowired
     private DiscoveryClient discoveryClient;
 
     public List<User> queryUserByIds(List<Long> ids) {
         List<User> users = new ArrayList<>();
         // 地址直接写服务名称即可
       //List<ServiceInstance> instances = discoveryClient.getInstances("user-service-demo");
       //ServiceInstance instance = instances.get(0);
       //String baseUrl = "http://" + instance.getHost() + ":" + instance.getPort() + "/user/";
       //上面的方式，等同于拿到一个固定的实例，而指定的话，负载均衡的意义何在。
         String baseUrl = "http://user-service/user/";
         ids.forEach(id -> {
             // 我们测试多次查询，
             users.add(this.restTemplate.getForObject(baseUrl + id, User.class));
             // 每次间隔500毫秒
             try {
                 Thread.sleep(500);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         });
         return users;
     }
 }
 ```
 * 负载均衡策略
 1、查看Ribbon的查询方式（怎样随机发送请求）  
 1.1、创建一个Test类
 ```java
 @RunWith(SpringRunner.class)
 @SpringBootTest(classes = UserConsumerDemoApplication.class)
 public class LoadBalanceTest {
 
     @Autowired
     RibbonLoadBalancerClient client;
 
     @Test
     public void test(){
         for (int i = 0; i < 100; i++) {
             ServiceInstance instance = this.client.choose("user-service");
             System.out.println(instance.getHost() + ":" + instance.getPort());
         }
     }
 }
 ```
 经过测试，发现是轮询方式。更改负载均衡策略(改为随机)
 ```yaml
 user-service:  # 这个名称是服务名称
   ribbon:
     NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
 ```
 * 重试机制
  eureka的服务治理强调CAP中的AP，即可用性和可靠性，它与Zookeeper这一类强调CP(一致性，可靠性)的服务治理框架，最大的区别在于，eureka为了更高的服务可用性，牺牲了一定的一致性，极端情况下，宁愿接收故障的实例也不愿丢失健康实例，如自我保护机制
  解决方案：  
  1、使用spring-retry 来增强RestTemplate的重试能力，当一次服务调用失败后，不是抛出错误，而是再次重试另一个服务。配置如下（user-service指的是需要远程调用的服务名）：
  ```ymal
  spring:
    cloud:
      loadbalancer:
        retry:
          enabled: true # 开启Spring Cloud的重试功能
  user-service:
    ribbon:
      ConnectTimeout: 250 # Ribbon的连接超时时间
      ReadTimeout: 1000 # Ribbon的数据读取超时时间
      OkToRetryOnAllOperations: true # 是否对所有操作都进行重试
      MaxAutoRetriesNextServer: 1 # 切换实例的重试次数
      MaxAutoRetries: 1 # 对当前实例的重试次数
      NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
  ```
  2、引入spring-retry依赖
  ```yml
  <dependency>
      <groupId>org.springframework.retry</groupId>
      <artifactId>spring-retry</artifactId>
  </dependency>
  ```
  #熔断器
  ##Hystrix
  复杂的微服务分布式，各自重重依赖，一旦发生任何问题，持续的请求就会将应用资源被耗尽（线程被占，而不释放），Hystrix使用自己的线程池，和主服务器的线程池进行隔离。  
  Hystrix实现弹性容错，自行探知服务是否反应慢或者大量超时，通过断路的方式将请求给拒绝掉，一段时间后，会尝试将部分请求放行，如果服务可以了，就不再断路。
  * 应用场景
  1、当服务繁忙时，如果服务出现异常，不是粗暴的直接报错，而是返回一个友好的提示，虽然拒绝了用户的访问，但是会返回一个结果。
    
    这就好比去买鱼，平常超市买鱼会额外赠送杀鱼的服务。等到逢年过节，超时繁忙时，可能就不提供杀鱼服务了，这就是服务的降级。
    
    系统特别繁忙时，一些次要服务暂时中断，优先保证主要服务的畅通，一切资源优先让给主要服务来使用，在双十一、618时，京东天猫都会采用这样的策略。  
 ##使用过程
 * 引入依赖
 ```yaml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```
 我们在user-consume-service中，进行试验。 （Hystrix默认超过1000ms就会触发熔断）
 * 添加一个访问user-service-demo的Dao,并且声明一个失败时回滚处理函数(就是在调用其他服务方法上添加`@HystrixCommand(fallbackMethod = "queryUserByIdFallback")`，其中，后面的为回滚处理函数)
 ```java
 @Component
 public class UserDao {
 
     @Autowired
     private RestTemplate restTemplate;
 
     private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
 
     @HystrixCommand(fallbackMethod = "queryUserByIdFallback")
     public User queryUserById(Long id){
         long begin = System.currentTimeMillis();
         String url = "http://user-service/user/" + id;
         User user = this.restTemplate.getForObject(url, User.class);
         long end = System.currentTimeMillis();
         // 记录访问用时：
         logger.info("访问用时：{}", end - begin);
         return user;
     }
 
     public User queryUserByIdFallback(Long id){
         User user = new User();
         user.setId(id);
         user.setName("用户信息查询出现异常！");
         return user;
     }
 }
 ```
 * 改造服务提供者（user-service-demo）,并让其反应时间有时大于1000ms，触发熔断
 ```java
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User queryById(Long id) throws InterruptedException {
        // 为了演示超时现象，我们在这里然线程休眠,时间随机 0~2000毫秒
        Thread.sleep(new Random().nextInt(2000));
        return this.userMapper.selectByPrimaryKey(id);
    }
}
```
 * 启动类改造，添加`@EnableCircuitBreaker`注解 
 * 优化   
 1、熔断实现了，但是重试机制，没有生效,这是因为 Hystrix设置的熔断时间和Ribbon设置的时间相同，先触发了熔断。
 #Feign
 在前面的学习中，我们使用负载均衡，大大简化了远程调时的代码
 ```java
 String baseUrl = "http://user-service-demo/user/"
 User user = this.resetTemplate.getForObject(baseUrl+id,User.class)
```
但是这样会让你在调用服务时，编写大量重复的代码，每次调用都要编写这个，无非一些参数不一样，这时我们就需要Feign。  
##介绍
Feign可以把Rest的请求进行隐藏，伪装成类似SpringMVC的Controller一样。你不用再自己拼接url，拼接参数等等操作，一切都交给Feign去做。
##快速入门
1、 导入依赖  
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
2、 Feign的客户端  
```java
@FeignClient("user-service")
public interface UserFeignClient {

    @GetMapping("/user/{id}")
    User queryUserById(@PathVariable("id") Long id);
}
```
* 首先这是一个接口，Feign会通过动态代理，帮我们实现类，这点跟mybaits的mapper很像。
* @FeignClient 声明这是一个Feign客户端，类似mapper注解，同时通过value属性指定服务名称。
* 接口中的定义方法，完全采用SpringMvc的注解，Feign会帮助我们生成URL，并访问获取结果。