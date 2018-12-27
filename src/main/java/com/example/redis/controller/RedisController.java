package com.example.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.log.LogInputStream;

import javax.annotation.Resource;
import javax.jws.Oneway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate=null;

    @RequestMapping("/stringAndHash")
    public Map<String,Object> testStringAndHash(){
        redisTemplate.opsForValue().set("key1","value1");
        redisTemplate.opsForValue().set("int_key","1");

//        定义1然后加1
        stringRedisTemplate.opsForValue().set("int","1");
        stringRedisTemplate.opsForValue().increment("int",1);
//        获取底层jedis连接
//        RedisProperties.Jedis jedis= (RedisProperties.Jedis) stringRedisTemplate.getConnectionFactory()
//                .getConnection().getNativeConnection();


        Map<String,String> hash=new HashMap<>();
        hash.put("filed1","value1");
        hash.put("filed2","value2");
//        存入一个散列数据类型
        stringRedisTemplate.opsForHash().putAll("hash",hash);
//        新增一个字段
        stringRedisTemplate.opsForHash().put("hash","filed3","value3");
//        绑定散列操作的key，这样可以连续对用一个散列数据进行操作
        BoundHashOperations hashOperations=stringRedisTemplate.boundHashOps("hash");
//        删除两个字段
        hashOperations.delete("filed1","filed2");
//        新增一个字段
        hashOperations.put("filed4","value5");

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("Success",true);
        return map;

    }

    @RequestMapping("/list")
    public Map<String,Object> testList(){
//        反着放进去
        stringRedisTemplate.opsForList().leftPushAll(
                "list1","v2","v4","v6","v8","v10"
        );

//        正着放进去
        stringRedisTemplate.opsForList().rightPushAll(
                "list2","v1","v2","v3","v4"
        );

//        绑定list2链表操作
        BoundListOperations listOperations=stringRedisTemplate.boundListOps("list2");
//        从右边弹出一个成员
        Object result1 =listOperations.rightPop();
        System.out.println(result1);

//        取出定位元素
        Object result2=listOperations.index(2);
        System.out.println(result2);

//        从左边插入链表
        listOperations.leftPush("v0");
//         查看链表长度
        Long size=listOperations.size();
//        求链表区间成员
        List elements=listOperations.range(0,size-1);
        elements.forEach((element)-> System.out.print(element+","));

        Map<String,Object> map=new HashMap<>();
        map.put("success",true);
        return  map;
    }

    @RequestMapping("/set")
    public Map<String,Object> testSet(){
        stringRedisTemplate.opsForSet().add(
                "set1","v1","v2","v3","v4"
        );
        stringRedisTemplate.opsForSet().add(
                "set2","v2","v4","v6","v8");
        BoundSetOperations operations= (BoundSetOperations) stringRedisTemplate.boundSetOps("set1");
        operations.add("v6","v7");
//        求交集
        Set inter =operations.intersect("set2");
//        求交集,并用新集合inter 保存
        operations.intersectAndStore("set2","inter");
//        求差集,并用新集合保存
        operations.diffAndStore("set2","diff");
//        求并集,并用新集合保存
        operations.unionAndStore("set2","union");

        Map<String,Object> map=new HashMap<>();
        map.put("success",true);
        return  map;
    }

    @RequestMapping("/multi")
    public Map<String, Object> testMulti() {
        redisTemplate.opsForValue().set("key1", "1");
        List list = (List) redisTemplate.execute((new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
//                设置要监控key1
                operations.watch("key1");
//                开启事务,再exec命令执行钱,全部都知识进入队列
                operations.multi();
                operations.opsForValue().set("key2","value2");

                operations.opsForValue().increment("key1",1);

                Object value2=operations.opsForValue().get("key2");
                System.out.println("命令再队列,所以value为null["+value2+"]");

                operations.opsForValue().set("key3","value3");
                Object value3 = operations.opsForValue().get("key3");
                System.out.println("命令再队列,所以value为null["+value3+"]");

//                执行exec命令,先判别key1是否再监控后被修改过,如果是则不执行事务,否者就执行事务
                return operations.exec();
            }
        }));
        return null;
    }
}
