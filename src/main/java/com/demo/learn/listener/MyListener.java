package com.demo.learn.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author luwt
 * @date 2021/4/22.
 * 实现SpringApplicationRunListener接口，完成对springboot生命周期的监听
 */
@Slf4j
public class MyListener implements SpringApplicationRunListener {

    public MyListener(SpringApplication springApplication, String[] arg) {
    }

    @Override
    public void starting() {
        log.info("系统正在启动");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("已经准备好环境");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("已经准备好上下文");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("上下文加载完毕");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("系统已经开始运行");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("系统运行中");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("系统启动失败");
    }
}
