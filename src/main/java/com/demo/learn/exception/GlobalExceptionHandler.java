package com.demo.learn.exception;

import com.demo.learn.result.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器，优先捕获子类异常
 * 指定处理的包：@RestControllerAdvice(basePackages = {"com.demo.learn.controller"})
 *              @RestControllerAdvice("com.demo.learn.controller")
 *              @RestControllerAdvice(value = "com.demo.learn.controller")
 * 指定具体的controller类：@RestControllerAdvice(basePackageClasses = {TestController.class})
 *                       @RestControllerAdvice(assignableTypes = {TestController.class})
 * 指定具体的注解，被这些注解标记的controller会被管理：@RestControllerAdvice(annotations = {RestController.class})
 * @author luwt
 * @date 2020/5/13.
 */
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 捕获 BusinessException 类异常，对其进行处理
     * @param be
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResultEntity businessExceptionHandler(BusinessException be) {
        logger.error("业务异常，异常类型为：{}，异常信息：{}，堆栈信息：", be.getClass(), be.getMessage(), be);
        return new ResultEntity<>(be.getException());
    }

    /**
     * 捕获异常，优先级最低
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResultEntity exceptionHandler(HttpServletRequest request, Exception e) {
        ResultEntity resultEntity;
        if (e instanceof NoHandlerFoundException) {
            resultEntity = new ResultEntity(ExceptionEnum.HANDLER_NOT_FOUND);
        } else {
            resultEntity = new ResultEntity(ExceptionEnum.UNDEFINED);
        }
        logger.error("未知异常，异常类型为：{}，异常信息：{}，堆栈信息：", e.getClass(), e.getMessage(), e);
        return resultEntity;
    }
}
