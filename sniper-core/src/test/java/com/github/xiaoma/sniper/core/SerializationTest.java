package com.github.xiaoma.sniper.core;

import com.github.xiaoma.sniper.core.serialize.Serialization;
import com.github.xiaoma.sniper.core.serialize.support.java.JavaSerialization;
import com.github.xiaoma.sniper.core.serialize.support.kryo.KryoSerialization;
import com.github.xiaoma.sniper.core.serialize.support.protostuff.ProtoStuffSerialization;
import org.junit.Test;

import java.util.*;

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
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(1L, "a1", 1, "p1", 11, "c1"));
        addresses.add(new Address(2L, "a2", 2, "p2", 22, "c2"));
        addresses.add(new Address(3L, "a3", 3, "p3", 33, "c3"));
        addresses.add(new Address(4L, "a4", 4, "p4", 44, "c4"));

        Map<String, Object> names = new HashMap<>();
        names.put("n1", "n111");
        names.put("n2", "n111");
        names.put("n3", "n111");
        names.put("n4", "n111");
        names.put("n5", new User());

        List<String> phones = Arrays.asList("1234567889", "1234567889", "1234567889", "1234567889", "1234567889", "1234567889");

        User user = new User(18000000L, 25, "张三", addresses, names, phones);

        try {
            byte[] data = serialization.serialize(url, user);
            serialization.deserialize(url, data, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
