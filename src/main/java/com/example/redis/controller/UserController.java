package com.example.redis.controller;

import com.example.redis.pojo.User;
import com.example.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/getUser/{id}")
    public User getUser(@PathVariable Long id){
        return  userService.getUser(id);
    }

    @RequestMapping("/insertUser")
    public User insertUser(String userName,String note){
        User user=new User();
        user.setUserName(userName);
        user.setNote(note);
        userService.insertUser(user);
        return  user;
    }

    @RequestMapping("/findUsers")
    public List<User> findUsers (String userName,String note){
        return userService.findUsers(userName,note);
    }

    @RequestMapping("/updateUserName")
    public Map<String,Object> updateUserName(Long id ,String userName){
        User user=userService.updateUserName(id,userName);
        boolean flag=user!=null;
        String message=flag?"更新成功":"更新失败";
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put(message,user);
        return  resultMap;
    }

    @RequestMapping("/deleteUser")
    private Map<String,Object> deleteUser(Long id){
        int result =userService.deleteUser(id);
        boolean flag=result==1;
        String message=flag?"删除成功":"删除失败";
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put(message,result);
        return  resultMap;
    }

}
