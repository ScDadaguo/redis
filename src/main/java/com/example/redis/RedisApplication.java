package com.example.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Autowired
    private RedisTemplate redisTemplate=null;

    @PostConstruct
    public void init(){
            initReidsTemplate();
    }

    private void initReidsTemplate(){
        RedisSerializer stingSerializer =redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stingSerializer);
        redisTemplate.setHashKeySerializer(stingSerializer);
        redisTemplate.setValueSerializer(stingSerializer);
        redisTemplate.setHashValueSerializer(stingSerializer);
    }

}

