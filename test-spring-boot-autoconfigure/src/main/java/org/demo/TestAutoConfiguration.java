package org.demo;

import org.demo.service.impl.TestServiceImpl;
import org.demo.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by luwt on 2021/4/3.
 */
@Configuration
// 这个注解用来配合@ConfigurationProperties注解使用
@EnableConfigurationProperties(value = TestProperties.class)
public class TestAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TestAutoConfiguration.class);

    @Autowired
    private TestProperties testProperties;

    /**
     * 注册Bean
     * @return TestService
     */
    @Bean
    public TestService testService() {
        log.info("进入注册TestService方法");
        TestService testService = new TestServiceImpl(testProperties.getName(), testProperties.getMsg());
        log.info("注册结束!");
        return testService;
    }


}
