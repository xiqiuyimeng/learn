package org.demo.learn.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 拦截器，负责打印出请求的url和参数
 * Created by luwt on 2020/5/24.
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {


    private final String TRACE_ID = "mdc_trace_id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        MDC.put(TRACE_ID, uuid);
        log.info("使用拦截器打印的请求信息开始！");
        log.info("Request begin !");
        String method = request.getMethod();
        log.info("Request method is ===> {}", method);
        StringBuffer url = request.getRequestURL();
        log.info("Request url is ===> {}", url);
        String param = request.getQueryString();
        if (StringUtils.isNoneBlank(param)) {
            log.info("Request param is ===> {}", param);
        }
        // 只有请求类型是 application/json 的流才被包装过，可以读取，其他的不可以读取
        String contentType = request.getContentType();
        boolean isJsonBody = StringUtils.isNotBlank(contentType)
                && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE);
        if (isJsonBody) {
            String requestBody = IOUtils.toString(request.getInputStream());
            if (StringUtils.isNoneBlank(requestBody)) {
                log.info("Request body is ===> {}", requestBody);
            }
        }
        log.info("使用拦截器打印的请求信息结束！");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        利用反射，在拦截器中获取到接口返回结果。由于过滤器中使用了BufferedServletResponseWrapper，
//        所以第一行的类型无法转换，使用这种方法就舍弃过滤器打印日志的实现
//        ResponseFacade responseFacade = (ResponseFacade) response;
//        ServletOutputStream servletOutputStream = responseFacade.getOutputStream();
//        Field field = servletOutputStream.getClass().getDeclaredField("ob");
//        field.setAccessible(true);
//        OutputBuffer outputBuffer = (OutputBuffer) field.get(servletOutputStream);
//        Field field1 = outputBuffer.getClass().getDeclaredField("bb");
//        field1.setAccessible(true);
//        ByteBuffer byteBuffer = (ByteBuffer) field1.get(outputBuffer);
//        byte[] bytes = byteBuffer.array();
//        log.info("使用拦截器打印的接口响应信息开始！");
//        log.info(new String(bytes, StandardCharsets.UTF_8).trim());
//        log.info("使用拦截器打印的接口响应信息结束！");
//        log.info("Request over !");
        MDC.clear();
    }
}
