package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/18.
 */
public class NettyEncoder<T> extends MessageToByteEncoder<T> {

    private final Codec codec;
    private final Channel client;

    public NettyEncoder(Codec codec, Channel client) {
        this.codec = codec;
        this.client = client;
    }

    @Override
    protected void encode(ChannelHandlerContext chc, T t, ByteBuf out) throws Exception {
        ByteBuffer buffer = out.nioBuffer();
        byte[] data = codec.encode(client, buffer, t);
        out.writeInt(data.length);
        out.writeBytes(data);
        chc.flush();
    }
}
