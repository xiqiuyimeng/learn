package org.demo.service;

import org.demo.result.RpcResult;

/**
 * @author luwt
 * @date 2021/10/25
 */
public interface RpcTestService {

    RpcResult<Boolean> testRpc();

}
