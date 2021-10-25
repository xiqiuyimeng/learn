package org.demo.learn.service;

import org.demo.learn.model.User;

import java.util.List;


public interface UserService {

    User getUser(int userId);

    void addUser(User record);

    void deleteUser(int userId);

    void editUser(User record);

    List<User> getUserList();

}