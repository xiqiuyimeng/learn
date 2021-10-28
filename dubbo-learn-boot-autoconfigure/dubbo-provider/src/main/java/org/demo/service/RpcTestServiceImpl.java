package org.demo.service;

import org.demo.param.RpcParam;
import org.demo.result.Result;
import org.demo.result.RpcResult;
import org.springframework.beans.BeanUtils;
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

    @Override
    public RpcResult<Result> testRpcParams(RpcParam param) {
        Result result = new Result();
        BeanUtils.copyProperties(param, result);
        return RpcResult.successResult(result);
    }

    @Override
    public RpcResult<Result> testRpcArgs(Integer id, String name, String message) {
        Result result = new Result();
        result.setId(id);
        result.setName(name);
        result.setMessage(message);
        return RpcResult.successResult(result);
    }
}
