package com.example.redis.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageListener implements MessageListener {
    
    /** 
    * @Description: 得到消息后的处理方法,其中message是redis发送过来的消息,pattern是渠道名称 
    * @Param: [message, pattern] 
    * @return: void 
    * @Author: 文兆杰
    * @Date: 2018/12/27 
    */ 
    @Override
    public void onMessage(Message message, byte[] pattern) {
//        消息体
        String body=new String(message.getBody());
//        渠道名称
        String topic =new String(pattern);
        System.out.println(body);
        System.out.println(topic);
    }
}
