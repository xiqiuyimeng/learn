package com.demo.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author luwt
 * @date 2020/9/4.
 */
@Component
public class MyRequestParamResolver extends AbstractNamedValueMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 如果有自定义@MyFile注解并且是文件请求就返回true
        return parameter.hasParameterAnnotation(MyFile.class)
                && MultipartResolutionDelegate.isMultipartArgument(parameter);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);

        if (servletRequest != null) {
            Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
            if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
                // 处理下多文件的情况，数组文件或者list文件，返回不为空，判断下直接置空，与单文件处理保持一致
                if (mpArg instanceof List && ((List) mpArg).isEmpty()) {
                    mpArg = null;
                } else if (mpArg instanceof MultipartFile[] && ((MultipartFile[]) mpArg).length == 0) {
                    mpArg = null;
                }
                return mpArg;
            }
        }
        Object arg = null;
        MultipartRequest multipartRequest = request.getNativeRequest(MultipartRequest.class);
        if (multipartRequest != null) {
            List<MultipartFile> files = multipartRequest.getFiles(name);
            if (!files.isEmpty()) {
                arg = (files.size() == 1 ? files.get(0) : files);
            }
        }
        if (arg == null) {
            String[] paramValues = request.getParameterValues(name);
            if (paramValues != null) {
                arg = (paramValues.length == 1 ? paramValues[0] : paramValues);
            }
        }
        return arg;
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        MyFile myFile = parameter.getParameterAnnotation(MyFile.class);
        return (myFile != null ? new MyFileNamedValueInfo(myFile) : new MyFileNamedValueInfo());
    }

    private static class MyFileNamedValueInfo extends NamedValueInfo {

        public MyFileNamedValueInfo() {
            super("", false, ValueConstants.DEFAULT_NONE);
        }

        public MyFileNamedValueInfo(MyFile annotation) {
            super(annotation.name(), annotation.required(), annotation.defaultValue());
        }
    }
}
