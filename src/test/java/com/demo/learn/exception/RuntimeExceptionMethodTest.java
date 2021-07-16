package com.demo.learn.exception;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * 如果一个方法声明的异常为运行时异常，调用时可以不显式处理
 */
@Slf4j
public class RuntimeExceptionMethodTest {

    /**
     * 测试调用声明运行时异常静态方法，不需要显式处理异常
     */
    @Test
    public void testRuntimeExceptionStaticMethod() {
        runtimeExceptionStaticMethod();
    }

    /**
     * 测试调用声明运行时异常普通方法，不需要显式处理异常
     */
    @Test
    public void testRuntimeExceptionMethod() {
        runtimeExceptionMethod();
    }

    /**
     * 测试调用声明普通异常静态方法，不需要显式处理异常
     */
    @Test
    public void testExceptionStaticMethod() {
        try {
            exceptionStaticMethod();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试调用声明普通异常普通方法，不需要显式处理异常
     */
    @Test
    public void testExceptionMethod() {
        try {
            exceptionMethod();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当一个静态方法声明的异常为运行时异常时，调用方可以不显式的处理异常
     * @throws RuntimeException 运行时异常
     */
    static void runtimeExceptionStaticMethod() throws RuntimeException {
        log.info("声明运行时异常的静态方法！");
    }

    /**
     * 当一个普通方法声明的异常为运行时异常时，调用方可以不显式的处理异常
     * @throws RuntimeException 运行时异常
     */
    void runtimeExceptionMethod() throws RuntimeException {
        log.info("声明运行时异常的普通方法！");
    }

    /**
     * 当一个静态方法声明的异常为普通异常时，调用方需要显式的处理异常
     * @throws IOException io异常，普通异常
     */
    static void exceptionStaticMethod() throws IOException {
        log.info("声明IOException的静态方法！");
    }

    /**
     * 当一个普通方法声明的异常为普通异常时，调用方需要显式的处理异常
     * @throws IOException io异常，普通异常
     */
    void exceptionMethod() throws IOException {
        log.info("声明IOException的普通方法！");
    }


}
