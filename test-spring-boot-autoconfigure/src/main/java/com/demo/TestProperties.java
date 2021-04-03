package com.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by luwt on 2021/4/3.
 */
// 此注解可以批量从配置文件中注入值，相当于多个@Value
@ConfigurationProperties(prefix = "test-autoconfigure")
public class TestProperties {

    private String name;

    private String msg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
