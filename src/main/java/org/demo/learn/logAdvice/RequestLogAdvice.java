package org.demo.learn.logAdvice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 对使用了 @RequestBody 注解的controller方法生效
 * RequestBodyAdviceAdapter implements RequestBodyAdvice，继承 RequestBodyAdviceAdapter 可以让代码更简洁
 * 可以在输入的body参数被读取之前进行操作，比如打印日志，或者加解密数据，仅局限于使用了 @RequestBody 注解的controller方法，
 * 其他controller方法日志无法打印
 * @author luwt
 * @date 2021/11/8
 */
@Slf4j
@ControllerAdvice
public class RequestLogAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter,
                                           Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        try {
            return super.beforeBodyRead(new MyHttpInputMessage(inputMessage), parameter, targetType, converterType);
        } catch (IOException e) {
            log.error("inputMessage 转换为 inputMessage出错：", e);
            return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
        }
    }

    static class MyHttpInputMessage implements HttpInputMessage{

        private final HttpHeaders headers;

        private final InputStream body;

        public MyHttpInputMessage(HttpInputMessage message) throws IOException {
            this.headers = message.getHeaders();
            String requestBody = IOUtils.toString(message.getBody(), StandardCharsets.UTF_8);
            JSONObject jsonBody = JSON.parseObject(requestBody);
            log.info("使用RequestLogAdvice打印的请求体是：{}", jsonBody);
            // 处理完毕，给body赋值
            this.body = IOUtils.toInputStream(requestBody, StandardCharsets.UTF_8);
        }

        @Override
        public InputStream getBody() throws IOException {
            // 返回body
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
