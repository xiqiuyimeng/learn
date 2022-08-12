package org.demo.learn.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.demo.learn.model.User;

@Mapper
public interface UserMapper {

    int deleteByPrimaryKey(int userId);

    int insert(User user);

    int insertSelective(User user);

    User selectByPrimaryKey(int userId);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);

    Page<User> getUserList();
}