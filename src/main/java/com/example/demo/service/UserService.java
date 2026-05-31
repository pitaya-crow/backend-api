package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User login(String userName, String password) {
        return userMapper.login(userName, password);
    }
    
    public int register(User user){
        return userMapper.register(user.getUserName(), user.getPassword());
    }
    
    public String logout(String userId){
        return "退出成功,用户ID："+userId;
    }
    
    public User updateInfo(User user) {
        if (user == null || user.getPersonId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        userMapper.updateUser(user);
        
        return userMapper.findById(user.getPersonId());
    }
}
