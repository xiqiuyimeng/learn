package org.demo.learn.result;

import lombok.Data;
import org.demo.learn.exception.BaseExceptionInter;
import org.demo.learn.exception.ExceptionEnum;

/**
 * 返回结果类
 * @author luwt
 * @date 2020/5/13.
 */
@Data
public class ResultEntity<T> {

    private int code;

    private String message;

    private T data;

    /**
     * 返回结果构造函数，接收异常接口类
     * @param exceptionInter
     */
    public ResultEntity(BaseExceptionInter exceptionInter) {
        this.code = exceptionInter.getCode();
        this.message = exceptionInter.getMessage();
    }

    /**
     * 返回结果构造函数，接收异常枚举类
     * @param exceptionEnum
     */
    public ResultEntity(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    /**
     * 返回结果构造函数，接收异常枚举类，并可自定义返回数据，否则为空
     * @param exceptionEnum
     * @param data
     */
    public ResultEntity(ExceptionEnum exceptionEnum, T data) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
        this.data = data;
    }

    /**
     * 返回结果构造函数，自定义错误码，错误信息，返回数据
     * @param code
     * @param message
     * @param data
     */
    public ResultEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
