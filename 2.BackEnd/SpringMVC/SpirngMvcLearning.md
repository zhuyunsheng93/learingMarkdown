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

