package org.demo.learn.controller;

import org.demo.learn.event.TestPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author luwt-a
 * @date 2022/7/16
 */
@RestController
@RequestMapping(value = "testEvent")
@ResponseStatus(HttpStatus.OK)
public class TestEventController {

    @Autowired
    private TestPublish testPublish;

    @GetMapping("publish")
    public String publish(@RequestParam String msg) {
        testPublish.testPublish(msg);
        return "ok";
    }
}
