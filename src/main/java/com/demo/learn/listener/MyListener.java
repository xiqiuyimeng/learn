package com.demo.learn.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author luwt
 * @date 2021/4/22.
 * 实现SpringApplicationRunListener接口，完成对springboot生命周期的监听
 */
public class MyListener implements SpringApplicationRunListener {

    public MyListener(SpringApplication springApplication, String[] arg) {
    }

    @Override
    public void starting() {
        System.out.println("正在启动");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("准备好环境了");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("准备好上下文了");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("上下文加载了");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println("开始了");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println("运行中");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("失败了 ");
    }
}
