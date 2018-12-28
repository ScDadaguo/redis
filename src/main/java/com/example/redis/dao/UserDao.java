package com.example.redis.dao;


import com.example.redis.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
//@Mapper
public interface UserDao {
//    获取单个用户
    public User getUser(Long id);


//    保存单个用户
    public int insertUser(User user);

//    修改用户
    public int updateUser(User user);


//    查询用户，指定mybatis的参数名称
    List<User> findUsers(@Param("userName") String userName,@Param("note") String note);


//    删除用户
    int deleteUser(Long id);


    @Select("Select * from t_user")
    public List<User> getAllUsers();

    public int insertUsers(List<User> userList);



}
