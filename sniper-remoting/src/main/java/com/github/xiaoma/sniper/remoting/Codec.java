package com.github.xiaoma.sniper.remoting;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.extension.Adaptive;
import com.github.xiaoma.sniper.core.extension.SPI;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by machunxiao on 17/1/17.
 */
@SPI
public interface Codec {

    @Adaptive({Constants.CODEC_KEY})
    void encode(Channel channel, ByteBuffer buffer, Object message) throws IOException;

    @Adaptive({Constants.CODEC_KEY})
    Object decode(Channel channel, ByteBuffer buffer) throws IOException;


}
