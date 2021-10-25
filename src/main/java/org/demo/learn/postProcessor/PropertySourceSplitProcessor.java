package org.demo.learn.postProcessor;

import org.apache.commons.lang3.StringUtils;
import org.demo.learn.util.PropertySplitRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 假设从apollo读取的配置文件有一个复合配置，例如：test.url = http://localhost:8080/test
 * 对于之前的应用是符合的，应用改造后，需要拆分配置，应用内只用的配置为：${test.host}, ${test.api} 两个配置来完成当前功能
 * 检索代码可以发现，apollo读取配置文件是在{@link com.ctrip.framework.apollo.spring.config.PropertySourcesProcessor}中完成的
 * 并且配置的order为{@code Ordered.HIGHEST_PRECEDENCE}，优先级最高。spring处理属性占位符的类为
 * {@link org.springframework.context.support.PropertySourcesPlaceholderConfigurer}
 * apollo读取配置的类在此类之前，所以正常来说，存在需要替换的属性占位符时，都可以正常被替换到，
 * 现在apollo配置文件中配置与代码中需要替换的属性占位符不符合，我们就可以在这两个类之间加入一个自定义的 BeanFactoryPostProcessor，
 * 获取apollo的属性值，将其拆分，将新的属性添加到应用的当前环境中，添加属性时使用 {@link MapPropertySource} 应该是最合适的。
 *
 * 无论是@Value注解还是使用xml配置的形式来获取配置，都是在 PropertySourcesPlaceholderConfigurer 中处理属性占位符替换
 * 完整示例代码如下，注入这些测试元素的类为：{@link PropertySplitRunner}
 * 
 * @author luwt
 * @date 2021/9/27
 */
@Component
public class PropertySourceSplitProcessor implements BeanFactoryPostProcessor, PriorityOrdered, EnvironmentAware {

    private StandardServletEnvironment environment;

    private static final Pattern hostPattern = Pattern.compile("(?<=(http|https)://)(?<result>([a-zA-Z0-9-]+\\.*)+)(?=(:\\d+)*?/.*)");

    private static final Pattern apiPattern = Pattern.compile("(http|https)://([a-zA-Z0-9-]+\\.*)+(:\\d+)*?(?<result>(/.*))");

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String url = environment.getProperty("test.url");
        HashMap<String, Object> map = new HashMap<>();
        map.put("test.host", getHost(url));
        map.put("test.api", getApi(url));
        MapPropertySource mapPropertySource = new MapPropertySource("testPropertySource", map);
        environment.getPropertySources().addLast(mapPropertySource);
    }

    @Override
    public int getOrder() {
        // order 比 PropertySourcesProcessor 大一点即可，保证在其后执行
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (StandardServletEnvironment) environment;
    }

    /**
     * 假设 value 为 http://localhost:8080/test，host则返回 localhost
     * @param url url
     * @return
     */
    private static String getHost(String url) {
        return match(url, hostPattern);
    }

    /**
     * 假设 value 为 http://localhost:8080/test，host则返回 test
     * @param url url
     * @return
     */
    private static String getApi(String url) {
        return match(url, apiPattern);
    }

    /**
     * 根据模式匹配
     * @param url url
     * @param pattern pattern
     * @return
     */
    private static String match(String url, Pattern pattern) {
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group("result");
        }
        return StringUtils.EMPTY;
    }
}
