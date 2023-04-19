package org.demo.learn.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.demo.learn.dao.UserMapper;
import org.demo.learn.model.User;
import org.demo.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    public PageInfo<User> getUserList(Integer pageNum, Integer pageSize, String name) {
        Page<User> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> userMapper.getUserList(name));
        return new PageInfo<>(page);
    }

}