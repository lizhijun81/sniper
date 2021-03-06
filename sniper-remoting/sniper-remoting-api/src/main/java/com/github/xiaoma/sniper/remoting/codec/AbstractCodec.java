package com.github.xiaoma.sniper.remoting.codec;

import com.github.xiaoma.sniper.core.serialize.Serialization;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.Codec;

import java.io.*;

/**
 * Created by machunxiao on 17/1/18.
 */
public abstract class AbstractCodec implements Codec {

    protected Serialization getSerialization(Channel channel) {
        return CodecSupport.getSerialization(channel.getUrl());
    }

    protected ObjectOutput createOutput(OutputStream outputStream) throws IOException {
        return new ObjectOutputStream(outputStream);
    }

    protected ObjectInput createInput(InputStream inputStream) throws IOException {
        return new ObjectInputStream(inputStream);
    }
}
