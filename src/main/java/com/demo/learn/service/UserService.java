package com.demo.learn.service;

import com.demo.learn.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {

    User getUser(int userId);

    void addUser(User record);

    void deleteUser(int userId);

    void editUser(User record);

    List<User> getUserList();

}