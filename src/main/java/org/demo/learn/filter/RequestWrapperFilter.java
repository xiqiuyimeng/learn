package org.demo.learn.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义的过滤器，实现过滤器接口。
 * 主要功能：包装 servletRequest 中的 inputStream，将流内容读出，并缓存至字节数组中，
 * 以缓存流后的 BufferedServletRequestWrapper 替换 servletRequest
 * Created by luwt on 2020/5/24.
 */
@Component
@WebFilter
public class RequestWrapperFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        // 只需要对请求类型为 application/json 的流进行包装，方便后续拦截器读取流内容
        String contentType = servletRequest.getContentType();
        boolean isJsonBody = StringUtils.isNotBlank(contentType)
                && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE);
        if (isJsonBody && servletRequest instanceof HttpServletRequest) {
            requestWrapper = new BufferedServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if (null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
