package com.example.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableCaching
@MapperScan(
        basePackages ="com.example.redis",
        annotationClass = Repository.class
)
public class RedisApplication {


    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Autowired
    private RedisTemplate redisTemplate=null;

//   redis连接工厂
    @Autowired
    private RedisConnectionFactory connectionFactory=null;

//    Redis消息监听器
    @Autowired
    private MessageListener redisMsgListenner=null;

//    任务池
    private ThreadPoolTaskScheduler taskScheduler=null;

    /**
    * @Description: 创建任务池,运行线程等待处理Redis的消息
     * @Param: []
    * @return: org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
    * @Author: 文兆杰
    * @Date: 2018/12/27
    */
    @Bean
    public ThreadPoolTaskScheduler initTaskScheduler(){
        if (taskScheduler!=null){
            return taskScheduler;
        }
        taskScheduler=new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return  taskScheduler;
    }

    /**
    * @Description: 定义Redis的监听容器
    * @Param: []
    * @return: 监听容器org.springframework.data.redis.listener.RedisMessageListenerContainer
     * * @Author: 文兆杰
    * @Date: 2018/12/27
    */
    @Bean
    public RedisMessageListenerContainer initRedisContainer(){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        Redis连接工厂
        container.setConnectionFactory(connectionFactory);
//        设置运行任务池
        container.setTaskExecutor(initTaskScheduler());
//        定义监听渠道,名称为topic1
        Topic topic=new ChannelTopic("topic1");
//        使用监听器监听Redis的消息
        container.addMessageListener(redisMsgListenner,topic);
        return container;
    }












    @PostConstruct
    public void init(){
            initReidsTemplate();
    }






//  设置模板的序列化器
    private void initReidsTemplate(){
        RedisSerializer stingSerializer =redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stingSerializer);
        redisTemplate.setHashKeySerializer(stingSerializer);
        redisTemplate.setValueSerializer(stingSerializer);
        redisTemplate.setHashValueSerializer(stingSerializer);
    }








}

