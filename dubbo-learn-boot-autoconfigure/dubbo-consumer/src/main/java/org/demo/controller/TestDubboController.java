package org.demo.controller;


import org.demo.consumer.RpcTestConsumer;
import org.demo.result.RpcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luwt
 * @date 2021/10/26
 */
@RestController
public class TestDubboController {

    @Autowired
    private RpcTestConsumer rpcTestConsumer;

    @GetMapping("test/dubbo")
    public RpcResult<?> testDubbo() {
        return rpcTestConsumer.testDubbo();
    }
}
