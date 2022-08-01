package org.demo.learn.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步写法：使用 Async 注解，前提是需要配置 EnableAsync 注解
 * 并配置线程池 {@link org.demo.learn.event.TaskConfig} ThreadPoolTaskExecutor，
 * 使用注解失效的场景，可能是由于没有直接使用cglib代理类来调用对应方法
 * 详细描述见：{@link org.demo.learn.util.SpringUtils} 注释
 * @author luwt-a
 * @date 2022/8/1
 */
@Slf4j
@Component
public class AsyncAnno {

    @Async
    public void test() {
        try {
            Thread.sleep(1000);
            log.info("asyncAnno 类 test 方法");
        } catch (InterruptedException e) {
            log.error("异常", e);
        }
    }

}
