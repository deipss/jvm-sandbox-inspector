package me.deipss.jvm.sandbox.inspector.agent.core.plugin.http.copier;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServletOutputStreamCopier extends ServletOutputStream {

    private ServletOutputStream outputStream;
    private ByteArrayOutputStream copy;

    public ServletOutputStreamCopier(ServletOutputStream outputStream) {
        this.outputStream = outputStream;
        this.copy = new ByteArrayOutputStream(1024);
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        copy.write(b);
    }

    public byte[] getCopy() {
        return copy.toByteArray();
    }

    @Override
    public boolean isReady() {
        return this.outputStream.isReady();
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        if(null!=writeListener) {
            outputStream.setWriteListener(writeListener);
        }
    }
}