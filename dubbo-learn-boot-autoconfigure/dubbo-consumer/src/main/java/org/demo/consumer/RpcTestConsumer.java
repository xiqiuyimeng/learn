package org.demo.consumer;

import org.demo.result.RpcResult;
import org.demo.service.RpcTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luwt
 * @date 2021/10/26
 */
@Service
public class RpcTestConsumer {

    @Autowired
    private RpcTestService rpcTestServiceForConsumer;

    public RpcResult<?> testDubbo() {
        return rpcTestServiceForConsumer.testRpc();
    }
}
