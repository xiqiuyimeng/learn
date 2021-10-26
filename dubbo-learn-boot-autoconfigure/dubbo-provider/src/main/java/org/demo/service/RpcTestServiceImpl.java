package org.demo.service;

import org.demo.result.RpcResult;
import org.springframework.stereotype.Service;

/**
 * 注解式配置dubbo service
 * @author luwt
 * @date 2021/10/25
 */
//@DubboService(version = "0.0.1")
@Service("rpcTestService")
public class RpcTestServiceImpl implements RpcTestService{

    @Override
    public RpcResult<Boolean> testRpc() {
        return RpcResult.successResult(true);
    }
}
