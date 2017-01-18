package com.github.xiaoma.sniper.core.serialize.support.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by machunxiao on 17/1/17.
 */
public enum ProtoUtils {
    ;
    private static final ConcurrentHashMap<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static final Objenesis objenesis = new ObjenesisStd(true);

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        return (Schema<T>) cachedSchema.computeIfAbsent(clazz, RuntimeSchema::getSchema);
    }

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    public static <T> byte[] serializer(T obj) throws IOException {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @return
     */
    public static <T> T deserializer(byte[] data, Class<T> clazz) throws IOException {
        try {
            T obj = objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, obj, schema);
            return obj;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
