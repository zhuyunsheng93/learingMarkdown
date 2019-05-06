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
