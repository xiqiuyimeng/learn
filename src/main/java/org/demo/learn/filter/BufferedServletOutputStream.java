package org.demo.learn.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 自定义的 ServletOutputStream，用来保存流数据
 * @author luwt
 * @date 2021/3/15.
 */
public class BufferedServletOutputStream extends ServletOutputStream {

    private ByteArrayOutputStream outputStream;

    public BufferedServletOutputStream(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}
