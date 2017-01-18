package com.github.xiaoma.sniper.core.serialize.support.protostuff;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.serialize.Serialization;

import java.io.IOException;

/**
 * Created by machunxiao on 2016/12/29.
 */
public class ProtoStuffSerialization implements Serialization {

    @Override
    public byte getContentTypeId() {
        return 3;
    }

    @Override
    public byte[] serialize(URL url, Object data) throws IOException {
        return ProtoUtils.serializer(data);
    }

    @Override
    public <T> T deserialize(URL url, byte[] data, Class<T> type) throws IOException {
        return ProtoUtils.deserializer(data, type);
    }
}
