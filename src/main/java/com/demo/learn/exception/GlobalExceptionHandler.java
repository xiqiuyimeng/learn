package com.demo.learn.exception;

import com.demo.learn.result.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器，优先捕获子类异常
 * @author luwt
 * @date 2020/5/13.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 捕获 BusinessException 类异常，对其进行处理
     * @param be
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<?> businessExceptionHandler(BusinessException be) {
        logger.error("业务异常，异常类型为：{}，异常信息：{}，堆栈信息：", be.getClass(), be.getMessage(), be);
        return ResponseEntity.ok().body(new ResultEntity<>(be.getException()));
    }

    /**
     * 捕获异常，优先级最低
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exceptionHandler(HttpServletRequest request, Exception e) {
        ResultEntity resultEntity;
        if (e instanceof NoHandlerFoundException) {
            resultEntity = new ResultEntity(ExceptionEnum.HANDLER_NOT_FOUND);
        } else {
            resultEntity = new ResultEntity(ExceptionEnum.UNDEFINED);
        }
        logger.error("未知异常，异常类型为：{}，异常信息：{}，堆栈信息：", e.getClass(), e.getMessage(), e);
        return ResponseEntity.ok().body(resultEntity);
    }
}
