package com.demo.learn.dao;

import com.demo.learn.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int deleteByPrimaryKey(int userId);

    int insert(User user);

    int insertSelective(User user);

    User selectByPrimaryKey(int userId);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);

    List<User> getUserList();
}