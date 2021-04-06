package com.demo.learn.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luwt
 * @date 2020/9/4.
 */
@Slf4j
@Component
public class ResolverBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    MyRequestParamResolver myRequestParamResolver;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 初始化后的操作
        log.info("初始化后-------------------------------" + beanName);
        if(beanName.equals("requestMappingHandlerAdapter")){
            //requestMappingHandlerAdapter进行修改
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter)bean;
            List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();

            ArrayList<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
            resolvers.add(myRequestParamResolver);
            resolvers.addAll(argumentResolvers);
            adapter.setArgumentResolvers(resolvers);
        }
        return bean;
    }
}
