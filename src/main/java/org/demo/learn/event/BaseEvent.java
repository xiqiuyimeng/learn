package org.demo.learn.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author luwt-a
 * @date 2022/7/16
 */
public class BaseEvent extends ApplicationEvent {

    public BaseEvent(Object source) {
        super(source);
    }
}
