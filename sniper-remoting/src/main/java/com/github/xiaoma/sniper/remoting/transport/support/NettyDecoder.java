package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by machunxiao on 17/1/18.
 */
public class NettyDecoder extends ByteToMessageDecoder {

    private static final int HEAD_LENGTH = 4;

    private final Codec codec;
    private final URL url;
    private final ChannelListener listener;

    public NettyDecoder(Codec codec, URL url, ChannelListener listener) {
        this.codec = codec;
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected void decode(ChannelHandlerContext chc, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < HEAD_LENGTH) { //这个HEAD_LENGTH是我们用于表示头长度的字节数。  由于上面我们传的是一个int类型的值，所以这里HEAD_LENGTH的值为4.
            return;
        }
        in.markReaderIndex();                   // 标记一下当前的readIndex的位置
        int dataLength = in.readInt();          // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
        if (dataLength < 0) {                   // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
            chc.close();
        }

        if (in.readableBytes() < dataLength) {  // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex.
            in.resetReaderIndex();              // 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
            return;
        }

        byte[] body = new byte[dataLength];         // 取出满足长度的字节
        in.readBytes(body);                         //
        ByteBuffer buffer = ByteBuffer.wrap(body);
        NettyChannel channel = NettyChannel.getChannel(chc.channel(), url, listener);
        Object o = codec.decode(channel, buffer);    // 将byte数据转化为我们需要的对象
        out.add(o);
    }
}
