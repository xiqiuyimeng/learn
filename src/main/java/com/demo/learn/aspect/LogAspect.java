package com.demo.learn.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 使用切面形式来打印请求的日志，但是无法获取到全局异常的信息
 * @author luwt
 * @date 2021/3/15.
 */
@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(public * com.demo.learn.controller.*.*(..))")
    public void log() {

    }

    @Around("log()")
    public Object around(ProceedingJoinPoint invocation) throws Throwable{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 这里也可以打印日志信息
        HttpServletRequest request = attributes.getRequest();
        String s = IOUtils.toString(request.getInputStream());
        Object result = invocation.proceed();
        log.info("使用切面：参数：{}, 结果：{}", s, result);
        return result;
    }

}
