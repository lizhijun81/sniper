package com.github.xiaoma.sniper.rpc.sniper;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.Codec;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/20.
 */
public class SniperCodec implements Codec {

    @Override
    public byte[] encode(Channel channel, ByteBuffer buffer, Object message) throws IOException {
        return new byte[0];
    }

    @Override
    public Object decode(Channel channel, ByteBuffer buffer) throws IOException {
        return null;
    }
}
