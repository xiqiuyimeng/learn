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
        ExceptionEnum.UNDEFINED.assertTrue(name.equals("ok"));
    }

}
