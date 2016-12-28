package com.github.xiaoma.sniper.core.serialize.support.protostuff;

import com.github.xiaoma.sniper.core.serialize.Serialization;

import java.io.IOException;

/**
 * Created by machunxiao on 2016/12/29.
 */
public class ProtoStuffSerialization implements Serialization {

    @Override
    public Object serialize(byte[] data) throws IOException {
        return null;
    }

    @Override
    public byte[] deserialize(Object msg) throws IOException {
        return new byte[0];
    }
}
