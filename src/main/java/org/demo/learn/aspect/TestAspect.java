package org.demo.learn.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.demo.learn.annotation.TestAnnotate;
import org.springframework.stereotype.Component;

/**
 * @author luwt
 * @date 2020/8/7.
 */
@Aspect
@Component
public class TestAspect {

    @Pointcut("@annotation(testAnnotate)")
    public void testAspectImpl(TestAnnotate testAnnotate){

    }

    @Before("testAspectImpl(testAnnotate)")
    public void doBefore(JoinPoint joinPoint, TestAnnotate testAnnotate){
        System.out.println(111);
    }
}
