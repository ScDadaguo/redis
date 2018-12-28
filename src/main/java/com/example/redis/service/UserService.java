package com.example.redis.service;

import com.example.redis.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {

    //    获取单个用户
    public User getUser(Long id);


    //    保存单个用户
    public User insertUser(User user);

    //    修改用户,指定mybatis的参数名称
    public User updateUserName(Long id,String userName);


    //    查询用户，指定mybatis的参数名称
    List<User> findUsers(String userName, String note);


    //    删除用户
    int deleteUser(Long id);
}
