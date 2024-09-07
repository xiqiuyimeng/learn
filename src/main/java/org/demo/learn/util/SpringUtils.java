package org.demo.learn.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 一般需要一个能静态、随时随地获取spring bean的工具类，可以用于一些特殊场景，
 * 例如：在使用一些注解时，比如@Async，这些注解的实现机制都是动态代理，那么使其生效，则必须使用动态代理过的类来调用此方法
 * 但是实际开发过程中，经常会遇到在同一个 service 或者动态代理类中，注解方法作为嵌套子方法调用，这样的话，会导致注解不生效，原因就是嵌套调用的时候，
 * 调用栈已经不是动态代理生成的类，或者说隔了几层，这种情况下，如果想让注解生效，最简单的办法，就是手动获取动态代理类，然后调用对应注解方法
 *
 * 不同类调用直接注入代理对象调用
 * eg:
 * @Component
 * public class Test {
 *
 *     public void test1() {
 *         // do something before...
 *         // 这种调用会导致异步注解不生效，原因是如今的栈已经是  动态代理类 -> test1 -> test2
 *         this.test2();
 *         // do something after ...
 *
 *         // 同一个类中正确的调用方法，手动获取动态代理类，再次调用，与上面不同的是，栈调用层次变为：
 *         // 动态代理类 -> test1 -> 动态代理类 -> test2
 *         Test test = SpringUtils.getBean(Test.class);
 *         test.test2();
 *     }
 *
 *     @Async
 *     public void test2() {
 *         // do something
 *     }
 *
 * }
 *
 *
 * @author luwt-a
 * @date 2022/7/25
 */
@Component
public class SpringUtils implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static <T> List<T> getBeans(Class<T> tClass) {
        return new ArrayList<>(applicationContext.getBeansOfType(tClass).values());
    }
}
