package org.demo.learn;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApolloConfig
@SpringBootApplication
public class LearnApplication {

    /**
     * 为了支撑使用apollo配置，需要在启动时，添加vm参数如下：
     * -Denv=local
     * -Dapollo.cacheDir=src/main/resources/apollo，指定为apollo配置文件所在的目录（当前项目已经配置，按此配置即可），
     *      apollo会自动在此目录下搜寻配置文件：
     *          learn/config-cache/learn+default+application.properties，
 *          app.id在META-INF/app.properties文件中定义
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(LearnApplication.class, args);
    }

}
