#关于SpringMvc

* 一些使用方式，自己不知道的  
1、从路径中拿到参数
```java
      @GetMapping("/{id}")
       public User queryById(@PathVariable("id") Long id)
```
`输入网址为：http://localhost:8081/user/7`  

2、从路径中拿到一个集合参数
```java
@GetMapping
public List<User> consume(@RequestParam("ids") List<Long> ids )
```
`输入网址为：` http://localhost:8080/consume?ids=6,7,8  
3、得到ajax中的json参数
```java
@PostMapping
public String getUser(@RequestBody HashMap<String,String> map){
   String requestParam = map.get("paramName");
   String requestPram1 = map.get("paramName");
}
```
4、@ModelAttribute,@RequestParam,@RequestBody,的使用。

5、控制跨域问题 （cors跨域）
写一个配置类，并且进行注册
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin("http://manage.leyou.com");
        //2) 是否发送Cookie信息
        config.setAllowCredentials(true);
        //3) 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        // 4）允许的头信息
        config.addAllowedHeader("*");

        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}
```
