package com.github.xiaoma.sniper.core.serialize;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by machunxiao on 17/1/17.
 */
public interface ObjectInput {

    Object readObject() throws IOException, ClassNotFoundException;

    <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException;

    <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException;

}
