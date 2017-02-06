package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.codec.AbstractCodec;
import com.github.xiaoma.sniper.remoting.exchange.Request;
import com.github.xiaoma.sniper.remoting.exchange.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/18.
 */
public class ExchangeCodec extends AbstractCodec {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeCodec.class);
    private static final int HEADER_LENGTH = 16;

    @Override
    public byte[] encode(Channel channel, ByteBuffer buffer, Object message) throws IOException {
        if (message instanceof Request) {
            return encodeRequest(channel, buffer, (Request) message);
        } else if (message instanceof Response) {
            return encodeResponse(channel, buffer, (Response) message);
        }
        return null;
    }

    @Override
    public Object decode(Channel channel, ByteBuffer buffer) throws IOException {
        int readable = buffer.getInt();
        byte[] header = new byte[Math.min(readable, HEADER_LENGTH)];
        return decode(channel, buffer, readable, header);
    }

    private byte[] encodeRequest(Channel channel, ByteBuffer buffer, Request request) {
        buffer.clear();
        long requestId = request.getRequestId();
        String version = request.getVersion();
        byte event = request.getEvent();
        Object data = request.getData(); // RpcInvocation

        byte[] header = new byte[HEADER_LENGTH];
        buffer.putLong(requestId);
        return buffer.array();
    }

    private byte[] encodeResponse(Channel channel, ByteBuffer buffer, Response response) {
        return new byte[0];
    }

    private Object decode(Channel channel, ByteBuffer buffer, int readable, byte[] header) {
        return null;
    }
}
