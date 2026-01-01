package com.company.logging.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream capture = new ByteArrayOutputStream();
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {

        if (writer != null) {
            throw new IllegalStateException("getWriter() already called");
        }

        if (outputStream == null) {
            outputStream = new ServletOutputStream() {

                @Override
                public void write(int b) {
                    capture.write(b);
                }

                @Override
                public boolean isReady() {
                    return true;
                }


                @Override
                public void setWriteListener(WriteListener writeListener) {
                    // no-op
                }
            };
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {

        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() already called");
        }

        if (writer == null) {
            Charset charset = Charset.forName(getCharacterEncoding());
            writer = new PrintWriter(new OutputStreamWriter(capture, charset));
        }
        return writer;
    }

    /**
     * response body raw bytes
     */
    public byte[] getBody() {
        return capture.toByteArray();
    }

    /**
     * response body as string
     */
    public String getBodyAsString() {
        return capture.toString(StandardCharsets.UTF_8);
    }
}