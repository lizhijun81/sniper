package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.codec.AbstractCodec;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/18.
 */
public class TransportCodec extends AbstractCodec {

    @Override
    public byte[] encode(Channel channel, ByteBuffer buffer, Object message) throws IOException {
        return getSerialization(channel).serialize(channel.getUrl(), message);
    }

    @Override
    public Object decode(Channel channel, ByteBuffer buffer) throws IOException {
        return getSerialization(channel).deserialize(channel.getUrl(), buffer.array(), null);
    }
}
