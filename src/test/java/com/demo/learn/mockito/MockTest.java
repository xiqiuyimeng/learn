package com.demo.learn.mockito;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.demo.learn.controller.UserController;
import org.demo.learn.dao.UserMapper;
import org.demo.learn.model.User;
import org.demo.learn.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeClass;

import static org.mockito.Mockito.doReturn;

/**
 * @author luwt
 * @date 2022/1/7
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class MockTest {

    private static final Integer USERID = 1;

    // InjectMocks 实例化对象
    @InjectMocks
    private UserController userController;

    @InjectMocks
    private UserServiceImpl userService;

    // mock对象
    @Mock
    private UserMapper userMapper;

    @BeforeClass
    public void setup(){
        MockitoAnnotations.initMocks(this);

        // Spring提供的反射注入工具，将@InjectMocks提供的userService实例注入到 userController 中名为 userService 的变量中
        ReflectionTestUtils.setField(userController, "userService", userService);

        User user = new User();
        user.setUserId(USERID);
        user.setName("test_name");

        // 指定mock对象输入和输出，也就是mock效果
        doReturn(user).when(userMapper).selectByPrimaryKey(USERID);
    }

    @Test
    public void testSelect(){
        setup();
        // mock调用
        User user = userController.get(USERID);
        log.info(JSON.toJSONString(user));
    }

}
