package com.example.redis.service.impl;

import com.example.redis.dao.UserDao;
import com.example.redis.pojo.User;
import com.example.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    /**
    * @Description: 获取id，取参数id缓存用户
    * @Param: [id]
    * @return: com.example.redis.pojo.User
    * @Author: 文兆杰
    * @Date: 2018/12/28
    */
    @Override
    @Transactional
    @Cacheable(value = "redisCache",key = "'redis_user_'+#id")
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

/**
* @Description: 插入用户，最后mybatis回填id，取id缓存用户
* @Param: [user]
* @return: int
* @Author: 文兆杰
* @Date: 2018/12/28
*/
    @Override
    @Transactional
    @CachePut(value = "redisCache",key = "'redis_user_'+#result.id")
    public User insertUser(User user) {
        userDao.insertUser(user);
        return user;
    }

    /**
    * @Description: 更新数据后，更新缓存，如果condition配置项使结果返回为NULL，就不缓存
    * @Param: [id, userName]
    * @return: com.example.redis.pojo.User
    * @Author: 文兆杰
    * @Date: 2018/12/28
    */
    @Override
    @Transactional
    @CachePut(value = "redisCache",condition = "#result!='null'",key = "'redis_user_'+#id")
    public User updateUserName(Long id, String userName) {
//        此处调用getUser方法，该方法缓存注解失效，
//        所以这里还会执行sql，将查询到数据库最新数据
        User user = this.getUser(id);
        if (user==null)
            return null;
        user.setUserName(userName);
        userDao.updateUser(user);
        return user ;
    }
    /**
    * @Description:命中率低，所以不采用缓存机制
    * @Param: [userName, note]
    * @return: java.util.List<com.example.redis.pojo.User>
    * @Author: 文兆杰
    * @Date: 2018/12/28
    */
    @Override
    @Transactional
    public List<User> findUsers(String userName, String note) {
        return userDao.findUsers(userName,note);
    }


    /**
    * @Description: 移除缓存
    * @Param: [id]
    * @return: int
    * @Author: 文兆杰
    * @Date: 2018/12/28
    */
    @Override
    @Transactional
    @CacheEvict(value = "redisCache",key ="'redis_user_'+#id")
    public int deleteUser(Long id) {
        return userDao.deleteUser(id);
    }
}
