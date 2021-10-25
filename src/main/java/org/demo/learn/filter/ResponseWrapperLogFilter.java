package org.demo.learn.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 三种打印接口返回值的方法：
 * 1. 过滤器：需要构造包装流，略微麻烦，但效果很好，能获取到全局异常的信息。
 * 2. 拦截器反射：使用反射暴力获取输出流的信息，效果也不错，能获取到全局异常的信息。
 * 3. 切面：定义切面，打印输出结果，结果其实是方法执行的结果，也就是会有类属性。无法获取到全局异常的信息，不推荐。
 * @author luwt
 * @date 2021/3/15.
 */
@Slf4j
@Component
@WebFilter
public class ResponseWrapperLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        BufferedServletResponseWrapper bufferedServletResponseWrapper = new BufferedServletResponseWrapper(response);
        filterChain.doFilter(servletRequest, bufferedServletResponseWrapper);
        byte[] bytes = bufferedServletResponseWrapper.getBytes();
        log.info("使用过滤器打印的接口响应信息开始！");
        log.info("Response body is ===> {}", new String(bytes, StandardCharsets.UTF_8));
        log.info("使用过滤器打印的接口响应信息结束！");
        servletResponse.getOutputStream().write(bytes);
    }

    @Override
    public void destroy() {

    }
}
