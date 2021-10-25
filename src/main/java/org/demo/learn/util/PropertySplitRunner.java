package org.demo.learn.util;

import lombok.extern.slf4j.Slf4j;
import org.demo.learn.postProcessor.PropertySourceSplitProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 为了展示 {@link PropertySourceSplitProcessor} 拦截拆分属性效果而创建
 * @author luwt
 * @date 2021/9/28
 */
@Slf4j
@Component
public class PropertySplitRunner implements CommandLineRunner {

    @Value("${test.url}")
    private String url;

    @Value("${test.host}")
    private String host;

    @Value("${test.api}")
    private String api;

    @Override
    public void run(String... args) throws Exception {
        log.info("获取到的test.url为:{}, test.host为:{}, test.api为:{}", url, host, api);
    }

}
