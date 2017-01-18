package com.github.xiaoma.sniper.core.serialize.support.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by machunxiao on 17/1/18.
 */
public enum KryoUtils {
    ;

    private static class SniperKryoFactory implements KryoFactory {

        @Override
        public Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setAutoReset(true);
            return kryo;
        }
    }

    private static KryoPool pool = new KryoPool.Builder(new SniperKryoFactory()).softReferences().build();

    public static byte[] serialize(Object data) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Output output = new Output(baos)
        ) {
            return pool.run((kryo) -> {
                kryo.writeObject(output, data);
                output.flush();
                return baos.toByteArray();
            });
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        try (ByteArrayInputStream baos = new ByteArrayInputStream(data);
             Input input = new Input(baos)) {
            return pool.run((kryo -> kryo.readObject(input, type)));
        }
    }

}
