package com.github.xiaoma.sniper.core.serialize.support.kryo;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.serialize.Serialization;

import java.io.IOException;

/**
 * Created by machunxiao on 17/1/18.
 */
public class KryoSerialization implements Serialization {


    @Override
    public byte getContentTypeId() {
        return 2;
    }

    @Override
    public byte[] serialize(URL url, Object data) throws IOException {
        return KryoUtils.serialize(data);

    }

    @Override
    public <T> T deserialize(URL url, byte[] data, Class<T> type) throws IOException {
        return KryoUtils.deserialize(data, type);
    }
}
