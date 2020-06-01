package com.demo.learn.controller;

import com.demo.learn.exception.ExceptionEnum;
import com.demo.learn.result.ResultEntity;
import com.demo.learn.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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
        HashMap map = new HashMap();
        map.put("a", new String[]{});
        String[] strings = (String[]) map.get("a");
        ExceptionEnum.ILLEGAL_PARAM.assertNonNull(strings);
        ExceptionEnum.NPE.assertNonNull(name);
        ExceptionEnum.ILLEGAL_PARAM.assertTrue(name.equals("test"));
        testService.checkName(name);
        resultEntity = new ResultEntity(ExceptionEnum.SUCCESS);
        return ResponseEntity.ok().body(resultEntity);
    }

}
