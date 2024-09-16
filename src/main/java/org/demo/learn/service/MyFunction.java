package org.demo.learn.service;

/**
 * 自定义 function 接口，可以自定义多个参数，例如当前，定义三个参数
 *  jdk 自带 Function {@link java.util.function.Function} 接口，接受一个参数，
 *  BiFunction {@link java.util.function.BiFunction} 接口，接受两个参数
 *  FunctionalInterface 可有可无，只要保证接口，只有一个方法
 * @author luwt-a
 * @date 2024/9/16
 */
@FunctionalInterface
public interface MyFunction<T, U, P, R> {

    /**
     * 接受三个参数，返回 R
     * @param t 参数1
     * @param u 参数2
     * @param p 参数3
     * @return R
     */
    R apply(T t, U u, P p);
}
