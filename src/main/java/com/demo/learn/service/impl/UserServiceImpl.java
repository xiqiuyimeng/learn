package com.demo.learn.service.impl;

import com.demo.learn.dao.UserMapper;
import com.demo.learn.model.User;
import com.demo.learn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(int userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void addUser(User user) {
        userMapper.insertSelective(user);
        System.out.println(user.getUserId());
    }

    @Override
    public void deleteUser(int userId) {
        userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public void editUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

}