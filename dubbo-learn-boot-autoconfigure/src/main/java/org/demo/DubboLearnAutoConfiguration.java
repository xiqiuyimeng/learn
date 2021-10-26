package org.demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author luwt
 * @date 2021/10/25
 */
@ComponentScan(basePackages = {"org.demo"})
@ImportResource(locations = {
        "classpath:learn-dubbo-service-provider.xml",
        "classpath:learn-dubbo-service-consumer.xml"})
@Configuration
public class DubboLearnAutoConfiguration {

}
