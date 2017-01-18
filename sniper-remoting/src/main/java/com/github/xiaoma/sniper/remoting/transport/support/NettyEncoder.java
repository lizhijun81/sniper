package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
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
    private final URL url;
    private final ChannelListener listener;

    public NettyEncoder(Codec codec, URL url, ChannelListener listener) {
        this.codec = codec;
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected void encode(ChannelHandlerContext chc, T t, ByteBuf out) throws Exception {
        ByteBuffer buffer = out.nioBuffer();

        NettyChannel channel = NettyChannel.getChannel(chc.channel(), url, listener);
        byte[] data = codec.encode(channel, buffer, t);
        out.writeInt(data.length);
        out.writeBytes(data);
        chc.flush();
    }
}
