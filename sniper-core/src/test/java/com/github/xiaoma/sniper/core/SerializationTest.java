package com.github.xiaoma.sniper.core;

import com.github.xiaoma.sniper.core.serialize.Serialization;
import com.github.xiaoma.sniper.core.serialize.support.java.JavaSerialization;
import com.github.xiaoma.sniper.core.serialize.support.kryo.KryoSerialization;
import com.github.xiaoma.sniper.core.serialize.support.protostuff.ProtoStuffSerialization;
import org.junit.Test;

/**
 * Created by machunxiao on 17/1/17.
 */
public class SerializationTest {

    @Test
    public void testJava() {
        Serialization java = new JavaSerialization();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            testSerialization(java);
        }
        long end = System.currentTimeMillis();
        System.out.println("Java 100w次：" + (end - start));
    }

    @Test
    public void testProto() {
        Serialization proto = new ProtoStuffSerialization();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            testSerialization(proto);
        }
        long end = System.currentTimeMillis();
        System.out.println("Proto 100w次：" + (end - start));
    }

    @Test
    public void testKryo() {
        Serialization serialization = new KryoSerialization();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            testSerialization(serialization);
        }
        long end = System.currentTimeMillis();
        System.out.println("Kryo 100w次：" + (end - start));
    }


    private void testSerialization(Serialization serialization) {
        URL url = new URL("http", "127.0.0.1", 123);
        User user = new User(18, "张三");

        try {
            byte[] data = serialization.serialize(url, user);
            serialization.deserialize(url, data, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
