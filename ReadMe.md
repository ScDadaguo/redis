
### 如果你是用spring.io自动生成项目的话，整合过程出现的最大的bug就是什么 pool报错  ：
结局方法：
        
        ```<dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.0</version>
        </dependency>```

### 当初先你什么一定要配置url数据库的时候,但是你确实是没有用到:
这时springboot本身就在默认使用了jpa 一定会要配置数据源
解决方式:
```
    @SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
```

