 # 2019-05-05
## java配置（可以应用配置多数据源）
  spring之前的配置全靠xml配置，spring3.0后注解变得非常完善，推荐使用完全的java类进行配置。
  - `@Configuration`：声明一个类作为配置类，代替xml文件
  - `@Bean`：声明在方法上，将方法的返回值加入Bean容器，代替`<bean>`标签，没有指定名称，会以函数名来命名这个对象。
  - `@value`：属性注入（从属性文件中的值进行赋予）
  - `@PropertySource`：指定外部属性文件，（springboot默认情况为application.properties这个配置文件）
  - `@ConfigurationProperties`：声明当前类为属性读取类。`@ConfigurationProperties(prfix=jdbc)` prefix表示读取文件中，前缀为prefix的值。类中的属性值必须和jdbc.后面保持一致。
 ```java
      @ConfigurationProperties(prefix = "jdbc")
      public class JdbcProperties {
          private String url;
          private String driverClassName;
          private String username;
          private String password;
          // ... 略
          // getters 和 setters
      }
```
- `@EnableConfigurationProperties(JdbcProperties.class)` 来表明使用JdbcProperties这个类的对象。
- `ComponentScan` 类似与 <<context:component-scan>> 标签，就是用于扫描包的。

## SpringBoot中的自动配置
- 从类似与`xxxxProperties`中的文件进行读取。
## 静态资源的访问
根据`ResourceProperties`的类，里面定义了静态资源的默认查找路径。
  - classpath:/META-INF/resources/
  - classpath:/resources/
  - classpath:/static/
  - classpath:/public
  
  经常做法是放在 `classpath:/static/`目录下
## 添加拦截器
# 2019-05-06
## Mybatis知识
- `@MapperScan` 注解在启动类，创建的Mapper文件就可以不用添加`@Mapper`注解来进行bean在spring中的注册。
```java
@SpringBootApplication
@MapperScan("com.springboot.demo.mapper")
  public class DemoApplication {
    public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
    }
  }
```
- 在application.properties中配置
```properties
#mybatis 别名扫描
mybatis.type-aliases-package=com.springboot.demo.pojo
#mapper.xml文件位置，如果没有映射文件，就注释掉
mybatis.mapper-locations=classpath:mappers/*.xml
```
- druid连接池连接mysql时区错误
```sql
show variables like '%time_zone%';
set global time_zone='+8:00';
```
## SpringBoot连接池
- 默认在引入jdbc启动器时就会添加一个Hikari连接池
 ![1525514424562](assets/1525514424562.png)
- 当在application.properties配置其他连接池时就会将这个默认的给覆盖掉，不启用。
```properties
# 连接四大参数
spring.datasource.url=jdbc:mysql://localhost:3306/heima
spring.datasource.username=root
spring.datasource.password=123
# 可省略，SpringBoot自动推断
spring.datasource.driverClassName=com.mysql.jdbc.Driver
#Hikari配置
spring.datasource.hikari.idle-timeout=60000
spring.datasource.hikari.maximum-pool-size=30
spring.datasource.hikari.minimum-idle=10
###########################################
#druid
#初始化连接数
spring.datasource.druid.initial-size=1
#最小空闲连接
spring.datasource.druid.min-idle=1
#最大活动连接
spring.datasource.druid.max-active=20
#获取连接时测试是否可用
spring.datasource.druid.test-on-borrow=true
#监控页面启动
spring.datasource.druid.stat-view-servlet.allow=true
```
## Model ModelAndView ModelMap 前台绑定数据
- Model
>  1、每一次的请求可以自动创建  
   2、Model只是用来传输数据，并不会进行业务寻址。  
   3、接收各种数据，但是来接收一组数据List 这时Model实际上是ModelMap。
```java
//通过form提交上来的，直接controller同名参数获取提交上来的值
 @PostMapping("/login")
  public String login(Model model,String userName,String password){
    System.out.println(password);
    model.addAttribute("userName",userName);
    return "index";
  }
```
- ModelAndView
>1、 自行进行创建。  
 2、 可以通过setViewName("xxx"),进行寻址。  
 3、函数的返回类型为ModelAndView。
 ```java
  @GetMapping("/modelandview")
   public ModelAndView all(){
     List<User> userList = userService.queryAll();
     ModelAndView mav = new ModelAndView();
     mav.addObject("users",userList);
     mav.setViewName("users");
     return mav;
   }
 ```
- ModelMap
> 1、Spring框架自动创建ModelMap实例，并作为controller方法的参数传入，用户无需自行创建。  
  2、ModelMap对象主要用于传递控制方法处理数据到结果页面。
  ```java
   @GetMapping("/all")
    public String all(ModelMap model) {
      List<User> userList = userService.queryAll();
      model.addAttribute("users",userList);
      return "users";
    }
  ```  
## PostMapping和GetMapping
  在上面的中我们看到什么PostMapping和GetMapping，是什么意思呢。
  - `PostMapping` 等价于 `@RequestMapping(method = RequestMethod.POST)`
  - `GetMapping`等价于 `@RequestMapping(method = RequestMethod.GET)`
  - 剩下的，应该知道规律吧。
## Mysql出现SSL连接问题
这是因为高版本Mysql的问题，需要在连接方式中 添加`?useSSL=false`
## Mysql中出现时区问题
在application.yml配置文件中的数据库链接地址后添加serverTimezone=UTC就可以解决报错
## idea 一个应用，启动多次
## 用户登陆验证方式
1、登陆成功后，信息保存在session中，  
2、token  
3、JWT
## 使用Navicat Premium 12连接MySQL数据库时会出现Authentication plugin 'caching_sha2_password' cannot be loaded的错误。
* 原因：mysql8 之前的版本中加密规则是mysql_native_password,而在mysql8之后,加密规则是caching_sha2_password, 
* 解决问题方法有两种,一种是升级navicat驱动,一种是把mysql用户登录密码加密规则还原成mysql_native_password. 
`ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';`
##解决跨域问题cors
###ajax请求类型
浏览器会将ajax请求分为两类，简单请求、特殊请求。对于不同的请求处理的方式也有差异
* 简单请求
  * 请求方法是下面三种之一
    * HEAD
    * GET
    * POST
  * HTTP的头信息不超过以下几种字段
    * Accept
    * Accept-Language
    * Content-Language
    * Last-Event-ID
    * Content-Type 只限于三个值 application/x-www-form-urlencoded、multipart/form/data、text/html 
    
 简单请求 浏览器就会在请求头中携带一个字段，origin 这个origin会指出当前的请求属于哪个域（协议+域名+端口）服务会根据这个值来决定是否允许其跨域
 origin:http://manage.leyou.com
 如果服务器允许跨域，需要在返回的响应头中携带下面的信息。
 ```
Access-Control-Allow-Origin: http://manage.leyou.com
Access-Control-Allow-Credentials: true
Content-Type: text/html; charset=utf-8
```
* 如果想要操纵Cookies 需要满足三个条件
  * 服务的响应头中需要携带Access-Control-Allow-Credentials为true
  * 浏览器发起的ajax需要指定withCredentials为true
  * 响应头中的Access-Control-Allow-Origin 不能为* 必须是指定的域名
 ####复杂请求
 不符合简单请求条件的,就是复杂请求.例如put请求
 >预检请求
 
   特殊请求会在正式的通信之前,增加一次HTTP查询请求,成为""预检请求"(preLight)
   
   浏览器先询问服务器,当前网页所在的域名是否在服务器的许可名单之中,以及可以使用哪些HTTP动词和头信息字段,只有得到肯定答复,浏览器才会发送正式的XMLHttpRequest请求
 
一个预检请求模板 
```
OPTIONS /cors HTTP/1.1
Origin: http://manage.leyou.com
Access-Control-Request-Method: PUT          //接下来会用到的请求方式，put
Access-Control-Request-Headers: X-Custom-Header     //会额外用到的头信息
Host: api.leyou.com
Accept-Language: en-US
Connection: keep-alive
User-Agent: Mozilla/5.0...
```
服务对预检请求的回应（如果可跨域）
```
HTTP/1.1 200 OK
Date: Mon, 01 Dec 2008 01:15:39 GMT
Server: Apache/2.0.61 (Unix)
Access-Control-Allow-Origin: http://manage.leyou.com
Access-Control-Allow-Credentials: true
Access-Control-Allow-Methods: GET, POST, PUT     //允许的访问方式
Access-Control-Allow-Headers: X-Custom-Header    //允许携带的头
Access-Control-Max-Age: 1728000                  //本次许可的有效时间长度，单位是秒 过期之前的ajax请求就无需再次进行预检
Content-Type: text/html; charset=utf-8
Content-Encoding: gzip
Content-Length: 0
Keep-Alive: timeout=2, max=100
Connection: Keep-Alive
Content-Type: text/plain
```

