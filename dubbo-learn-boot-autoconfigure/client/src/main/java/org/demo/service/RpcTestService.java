package org.demo.service;

import org.demo.param.RpcParam;
import org.demo.result.Result;
import org.demo.result.RpcResult;

/**
 * @author luwt
 * @date 2021/10/25
 */
public interface RpcTestService {

    RpcResult<Boolean> testRpc();

    RpcResult<Result> testRpcParams(RpcParam param);

    RpcResult<Result> testRpcArgs(Integer id, String name, String message);

}
