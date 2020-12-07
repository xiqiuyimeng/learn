package com.demo.learn.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author luwt
 * @date 2020/9/4.
 */
@Component
public class ResolverBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    MyRequestParamResolver silasArgumentResolver;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 初始化后的操作
        System.out.println("初始化后-------------------------------" + beanName);
        if(beanName.equals("requestMappingHandlerAdapter")){
            //requestMappingHandlerAdapter进行修改
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter)bean;
            List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();

            //添加自定义参数处理器
            argumentResolvers = addArgumentResolvers(argumentResolvers);

            adapter.setArgumentResolvers(argumentResolvers);
        }
        return bean;
    }

    private List<HandlerMethodArgumentResolver> addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        //将自定的添加到最前面
        resolvers.add(silasArgumentResolver);
        //将原本的添加后面
        resolvers.addAll(argumentResolvers);
        return  resolvers;
    }
}
