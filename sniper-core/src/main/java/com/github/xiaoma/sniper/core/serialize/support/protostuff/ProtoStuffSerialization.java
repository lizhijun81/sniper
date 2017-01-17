package com.github.xiaoma.sniper.core.serialize.support.protostuff;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.serialize.ObjectInput;
import com.github.xiaoma.sniper.core.serialize.ObjectOutput;
import com.github.xiaoma.sniper.core.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by machunxiao on 2016/12/29.
 */
public class ProtoStuffSerialization implements Serialization {

    @Override
    public byte getContentTypeId() {
        return 2;
    }

    @Override
    public ObjectOutput serialize(URL url, OutputStream os) throws IOException {
        return new ProtoStuffOutput(os);
    }

    @Override
    public ObjectInput deserialize(URL url, InputStream is) throws IOException {
        return new ProtoStuffInput(is);
    }
}
