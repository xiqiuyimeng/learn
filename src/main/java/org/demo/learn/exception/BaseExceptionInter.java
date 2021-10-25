package org.demo.learn.exception;

/**
 * 异常基础接口类，定义方法获取错误码及错误信息
 * @author luwt
 * @date 2020/5/13.
 */
public interface BaseExceptionInter {

    int getCode();

    String getMessage();

}
