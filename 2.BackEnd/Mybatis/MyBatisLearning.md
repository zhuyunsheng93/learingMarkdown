#扫描mapper的三种方式
* 添加@Mapper注解
* 在application文件中配置 mybatis.mapper-locations = com.xxx.xxx/*.xml
* SpringBoot启动类上添加注解@MapperScan("com.xxx.mapper")