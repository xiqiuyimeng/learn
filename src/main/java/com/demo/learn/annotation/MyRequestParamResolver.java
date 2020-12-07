package com.demo.learn.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author luwt
 * @date 2020/9/4.
 */
@Component
public class MyRequestParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        System.out.println("2222");
        return null;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        System.out.println("111");
        return parameter.hasParameterAnnotation(MyFile.class);
//        if () {
//            if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
//                RequestFile requestFile = parameter.getParameterAnnotation(RequestFile.class);
//                return (requestFile != null && StringUtils.hasText(requestFile.name()));
//            }
//            else {
//                return true;
//            }
//        }
//        return false;
    }
}
