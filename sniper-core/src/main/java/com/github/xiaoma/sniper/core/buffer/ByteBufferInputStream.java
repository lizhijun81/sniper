package com.github.xiaoma.sniper.core.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/18.
 */
public class ByteBufferInputStream extends InputStream {

    private final ByteBuffer buffer;

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public int read() throws IOException {
        if (!buffer.hasRemaining()) return -1;
        return buffer.get() & 0xFF;
    }

    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (length == 0) return 0;
        int count = Math.min(buffer.remaining(), length);
        if (count == 0) return -1;
        buffer.get(bytes, offset, count);
        return count;
    }

    public int available() throws IOException {
        return buffer.remaining();
    }
}
