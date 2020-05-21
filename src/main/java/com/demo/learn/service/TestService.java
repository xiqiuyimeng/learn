package com.demo.learn.service;

import com.demo.learn.exception.BusinessException;
import com.demo.learn.exception.ExceptionEnum;
import org.springframework.stereotype.Service;

/**
 * @author luwt
 * @date 2020/5/13.
 */
@Service
public class TestService {

    public void checkName(String name) {
        if (name.equals("ok")) {
            throw new BusinessException(ExceptionEnum.UNDEFINED);
        } else {
//            throw new RuntimeException("运行时异常");
        }
    }

}
