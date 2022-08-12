package org.demo.learn.service;

import com.github.pagehelper.PageInfo;
import org.demo.learn.model.User;


public interface UserService {

    User getUser(int userId);

    void addUser(User record);

    void deleteUser(int userId);

    void editUser(User record);

    PageInfo<User> getUserList(Integer pageNum, Integer pageSize);

}