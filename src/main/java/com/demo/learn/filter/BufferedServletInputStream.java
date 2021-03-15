package com.demo.learn.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 自定义 ServletInputStream，接受字节数组，构造输入流
 * Created by luwt on 2020/5/24.
 */
public class BufferedServletInputStream extends ServletInputStream {

    private ByteArrayInputStream inputStream;

    public BufferedServletInputStream(byte[] bytes) {
        this.inputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public int available() throws IOException {
        return inputStream.available();
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputStream.read(b, off, len);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

}
