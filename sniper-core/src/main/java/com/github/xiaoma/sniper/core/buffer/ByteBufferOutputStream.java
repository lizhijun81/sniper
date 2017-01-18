package com.github.xiaoma.sniper.core.buffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/18.
 */
public class ByteBufferOutputStream extends OutputStream {

    private final ByteBuffer buffer;

    public ByteBufferOutputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public void write(int b) throws IOException {
        if (!buffer.hasRemaining()) flush();
        buffer.put((byte) b);
    }

    public void write(byte[] bytes, int offset, int length) throws IOException {
        if (buffer.remaining() < length) flush();
        buffer.put(bytes, offset, length);
    }

    @Override
    public void flush() throws IOException {
        buffer.flip();
    }
}
