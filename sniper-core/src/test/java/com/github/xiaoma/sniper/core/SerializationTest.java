package com.github.xiaoma.sniper.core;

import com.github.xiaoma.sniper.core.serialize.Serialization;
import com.github.xiaoma.sniper.core.serialize.support.java.JavaSerialization;
import com.github.xiaoma.sniper.core.serialize.support.kryo.KryoSerialization;
import com.github.xiaoma.sniper.core.serialize.support.protostuff.ProtoStuffSerialization;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by machunxiao on 17/1/17.
 */
public class SerializationTest {

    @Test
    public void testJava() {
        Serialization java = new JavaSerialization();
        testSerialization(java);
    }

    @Test
    public void testProto() {
        Serialization proto = new ProtoStuffSerialization();
        testSerialization(proto);
    }

    @Test
    public void testKryo() {
        Serialization serialization = new KryoSerialization();
        testSerialization(serialization);
    }


    private void testSerialization(Serialization serialization) {
        URL url = new URL("http", "127.0.0.1", 123);
        User user = new User(18, "张三");

        try {
            byte[] data = serialization.serialize(url, user);
            System.out.println(Arrays.toString(data));
            User user1 = serialization.deserialize(url, data, User.class);
            System.out.println(String.format("serialization:%d,data.length:[%d],obj:[before:%s,after:%s]", serialization.getContentTypeId(), data.length, String.valueOf(user), String.valueOf(user1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
