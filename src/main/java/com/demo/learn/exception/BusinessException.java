package com.demo.learn.exception;

/**
 * 自定义业务异常类，继承自运行时异常
 * @author luwt
 * @date 2020/5/13.
 */
public class BusinessException extends RuntimeException {


    private BaseExceptionInter exceptionInter;

    /**
     * 构造函数，接收参数为基础异常接口类
     * @param exceptionInter
     */
    public BusinessException(BaseExceptionInter exceptionInter) {
        super(exceptionInter.getMessage());
        this.exceptionInter = exceptionInter;
    }

    /**
     * 获取异常
     * @return
     */
    public BaseExceptionInter getException() {
        return exceptionInter;
    }

}
