package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.serialize.Serialization;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.Codec;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/17.
 */
public class NioCodec implements Codec {

    @Override
    public void encode(Channel channel, ByteBuffer buffer, Object message) throws IOException {
        Serialization serialization = CodecSupport.getSerialization(channel.getUrl());
        serialization.serialize(message, null);
    }

    @Override
    public Object decode(Channel channel, ByteBuffer buffer) throws IOException {
        int bodyLength = buffer.getInt();
        Serialization serialization = CodecSupport.getSerialization(channel.getUrl());
        byte[] data = new byte[bodyLength];
        return serialization.deserialize(buffer.get(data).array(), null);
    }
}
