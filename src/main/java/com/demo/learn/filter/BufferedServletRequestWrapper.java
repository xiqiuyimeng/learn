package com.demo.learn.filter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 实现缓存 HttpServletRequest 中 inputStream 的功能。
 * 将流内容读取并缓存到数组中，重写 getInputStream 方法，此方法在后续框架中会被调用，
 * 将其重写为返回缓存内容的输入流。
 * Created by luwt on 2020/5/24.
 */
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] buffer;

    public BufferedServletRequestWrapper(HttpServletRequest request) throws IOException{
        super(request);
        InputStream is = request.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte bytes[] = new byte[1024];
        int read;
        while ((read = is.read(bytes)) > 0) {
            bos.write(bytes, 0, read);
        }
        this.buffer = bos.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new BufferedServletInputStream(this.buffer);
    }
}
