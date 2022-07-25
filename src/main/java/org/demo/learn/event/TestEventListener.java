package org.demo.learn.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author luwt-a
 * @date 2022/7/16
 */
@Slf4j
@Component
public class TestEventListener {

    @EventListener
    @Async
    public void onTestEvent(TestEvent event) throws InterruptedException {
        log.info("开始接收信息");
        Thread.sleep(3000);
        log.info("接收到了: " + event.getMsg());
    }

    @EventListener
    @Async
    public void onTestEvent2(TestEvent event) throws InterruptedException {
        log.info("开始接收信息22222");
        Thread.sleep(3000);
        log.info("接收到了22222: " + event.getMsg());
    }
}
