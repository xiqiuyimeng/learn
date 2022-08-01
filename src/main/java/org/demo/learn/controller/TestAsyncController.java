package org.demo.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.demo.learn.async.AsyncAnno;
import org.demo.learn.async.CompletableFutureService;
import org.demo.learn.async.ExecutorPool;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author luwt-a
 * @date 2022/8/1
 */
@Slf4j
@RestController
@RequestMapping(value = "testAsync")
@ResponseStatus(HttpStatus.OK)
public class TestAsyncController {

    @Resource
    AsyncAnno asyncAnno;

    @Resource
    ExecutorPool executorPool;

    @Resource
    CompletableFutureService completableFutureService;

    @GetMapping("asyncAnno")
    public String asyncAnno() {
        log.info("asyncAnno 方法");
        asyncAnno.test();
        return "ok";
    }

    @GetMapping("executorPool")
    public String executorPool() {
        log.info("executorPool 方法");
        executorPool.test();
        return "ok";
    }

    @GetMapping("completableFuture")
    public String completableFuture() {
        log.info("completableFuture 方法");
        completableFutureService.test();
        return "ok";
    }

}
