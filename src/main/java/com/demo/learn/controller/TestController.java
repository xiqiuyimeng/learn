package com.demo.learn.controller;

import com.demo.learn.exception.BusinessException;
import com.demo.learn.exception.ExceptionEnum;
import com.demo.learn.result.ResultEntity;
import com.demo.learn.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luwt
 * @date 2020/5/13.
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("test")
    public ResponseEntity<?> testGet(@RequestParam(required = false) String name){
        ResultEntity resultEntity;
        if (name == null) throw new BusinessException(ExceptionEnum.NPE);
        if (name.equals("test")) {
            resultEntity = new ResultEntity(ExceptionEnum.ILLEGAL_PARAM);
        } else {
            testService.checkName(name);
            resultEntity = new ResultEntity(ExceptionEnum.SUCCESS);
        }
        return ResponseEntity.ok().body(resultEntity);
    }

}
