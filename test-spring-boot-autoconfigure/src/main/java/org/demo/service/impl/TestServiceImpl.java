package org.demo.service.impl;

import org.demo.service.TestService;

/**
 * Created by luwt on 2021/4/3.
 */
public class TestServiceImpl implements TestService {

    private String name;

    private String msg;

    public TestServiceImpl(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    @Override
    public String testMethod() {
        return "Hello! this msg {" + msg + "} comes from {" + name + "}。";
    }
}
