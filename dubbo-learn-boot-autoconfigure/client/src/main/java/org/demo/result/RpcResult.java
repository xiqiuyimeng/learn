package org.demo.result;

import lombok.Data;

import java.io.Serializable;

/**
 * dubbo序列化要求必须实现 Serializable
 * @author luwt
 * @date 2021/10/25
 */
@Data
public class RpcResult<T> implements Serializable {

    private static final long serialVersionUID = -4692595825514337288L;

    private int code;

    private boolean success;

    private T data;

    public static <T> RpcResult<T> successResult(T data) {
        RpcResult<T> result = new RpcResult<T>();
        result.code = 200;
        result.success = true;
        result.data = data;
        return result;
    }
}
