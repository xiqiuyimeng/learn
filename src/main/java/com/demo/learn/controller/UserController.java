package com.demo.learn.controller;

import com.demo.learn.model.User;
import com.demo.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "user")
@ResponseStatus(HttpStatus.OK)
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("get")
    public User get(@RequestParam("userId") int userId){
        return userService.getUser(userId);
    }

    @PostMapping("add")
    public String add(@RequestBody User user){
        userService.addUser(user);
        return "ok";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("userId") int userId){
        userService.deleteUser(userId);
        return "ok";
    }

    @PostMapping("edit")
    public String edit(@RequestBody User user){
        userService.editUser(user);
        return "ok";
    }

    @GetMapping("list")
    public List<User> getList(){
        return userService.getUserList();
    }

}