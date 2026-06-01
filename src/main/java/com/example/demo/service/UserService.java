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

    public User getById(Integer personId) {
        if (personId == null) return null;
        return userMapper.findById(personId);
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

        // 更新基本信息
        userMapper.updateUser(user);

        // 如果提供了密码，单独更新
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userMapper.updatePassword(user.getPersonId(), user.getPassword());
        }

        User result = userMapper.findById(user.getPersonId());
        if (result != null) result.setPassword(null); // 不返回密码
        return result;
    }
}
