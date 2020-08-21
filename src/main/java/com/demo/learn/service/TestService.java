package com.demo.learn.service;

import com.demo.learn.exception.ExceptionEnum;
import org.springframework.stereotype.Service;

/**
 * @author luwt
 * @date 2020/5/13.
 */
@Service
public class TestService {

    public void checkName(String name) {
        ExceptionEnum anEnum = ExceptionEnum.UNDEFINED;
        anEnum.setMessage("自定义异常信息");
        anEnum.assertTrue(name.equals("ok"));
    }

}
