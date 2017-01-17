package com.github.xiaoma.sniper.core.serialize.support.protostuff;

import com.github.xiaoma.sniper.core.serialize.ObjectInput;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by machunxiao on 17/1/17.
 */
public class ProtoStuffInput implements ObjectInput {

    public ProtoStuffInput(InputStream is) {
    }

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        return null;
    }
}
