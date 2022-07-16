package org.demo.learn.event;

import lombok.Getter;

/**
 * @author luwt-a
 * @date 2022/7/16
 */
public class TestEvent extends BaseEvent{

    @Getter
    private String msg;

    public TestEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public enum Type {
        TEST_EVENT("测试事件");

        public String msg;

        Type(String msg) {
            this.msg = msg;
        }
    }
}
