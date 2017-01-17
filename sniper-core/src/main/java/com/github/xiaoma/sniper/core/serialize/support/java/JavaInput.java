package com.github.xiaoma.sniper.core.serialize.support.java;

import com.github.xiaoma.sniper.core.serialize.ObjectInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;

/**
 * Created by machunxiao on 17/1/17.
 */
public class JavaInput implements ObjectInput {

    private final ObjectInputStream ois;

    public JavaInput(InputStream is) throws IOException {
        this.ois = new ObjectInputStream(is);
    }

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return (T) readObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        return (T) readObject();
    }
}
