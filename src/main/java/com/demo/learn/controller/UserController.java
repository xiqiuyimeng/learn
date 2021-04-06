package com.demo.learn.controller;

import com.demo.learn.annotation.MyFile;
import com.demo.learn.model.User;
import com.demo.learn.service.UserService;
import com.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = "user")
@ResponseStatus(HttpStatus.OK)
public class UserController {

    @Autowired
    TestService testService;

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

    @GetMapping("hello")
    public String hello() {
        return testService.testMethod();
    }

    @PostMapping("file")
    public String testFile(@MyFile(value = "file") MultipartFile file) throws Exception{
        userService.uploadFile(file);
        return "ok";
    }

    @PostMapping("fileArr")
    public String testFileArr(@MyFile("files") MultipartFile[] files) throws Exception{
        userService.uploadFileArr(files);
        return "ok";
    }

    @PostMapping("files")
    public String testFiles(@MyFile("files") List<MultipartFile> files) throws Exception{
        userService.uploadFiles(files);
        return "ok";
    }

}