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
