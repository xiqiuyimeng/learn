package org.demo.learn.controller;

import com.github.pagehelper.PageInfo;
import org.demo.learn.designPattern.strategy.StrategyUser;
import org.demo.learn.model.User;
import org.demo.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "user")
@ResponseStatus(HttpStatus.OK)
public class UserController {
//
//    @Autowired
//    TestService testService;

    @Autowired
    StrategyUser strategyUser;

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
    public PageInfo<User> getList(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam String name){
        return userService.getUserList(pageNum, pageSize, name);
    }

//    @GetMapping("hello")
//    public String hello(@RequestParam("param1") String param1,
//                        @RequestParam("param2") String param2) {
//        System.out.println(param1);
//        System.out.println(param2);
//        return testService.testMethod();
//    }

    @GetMapping("strategy")
    public List<?> strategy(@RequestParam Integer num) {
        if (num == 1) {
            return strategyUser.handleStrategyFirst();
        } else if (num == 2) {
            return strategyUser.handleStrategySecond();
        } else {
            return strategyUser.handleStrategyAll();
        }
    }

}