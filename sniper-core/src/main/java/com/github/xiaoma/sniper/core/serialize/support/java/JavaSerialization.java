package com.github.xiaoma.sniper.core.serialize.support.java;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.serialize.Serialization;

import java.io.*;

/**
 * Created by machunxiao on 2016/12/29.
 */
public class JavaSerialization implements Serialization {

    @Override
    public byte getContentTypeId() {
        return 1;
    }

    @Override
    public byte[] serialize(URL url, Object data) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(data);
            oos.flush();
            return baos.toByteArray();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(URL url, byte[] data, Class<T> type) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("ClassNotFoundException: ", e);
        }

    }
}
