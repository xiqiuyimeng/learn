package org.demo.learn.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author luwt-a
 * @date 2022/7/16
 */
@Slf4j
@Component
public class TestPublish {

    @Autowired
    private ApplicationContext applicationContext;

    public void testPublish(String msg) {
        log.info(msg);
        TestEvent event = new TestEvent(TestEvent.Type.TEST_EVENT, msg);
        applicationContext.publishEvent(event);
    }
}
