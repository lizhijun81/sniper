package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.codec.AbstractCodec;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/18.
 */
public class ExchangeCodec extends AbstractCodec {

    @Override
    public byte[] encode(Channel channel, ByteBuffer buffer, Object message) throws IOException {
        return null;
    }

    @Override
    public Object decode(Channel channel, ByteBuffer buffer) throws IOException {

        return null;
    }

}
