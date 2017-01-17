package com.github.xiaoma.sniper.core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.github.xiaoma.sniper.core.utils.SerializationUtil;
import org.junit.Test;

import java.io.*;

/**
 * Created by machunxiao on 17/1/17.
 */
public class ProtostuffTest {

    @Test
    public void testSerialize() {
        String msg = "Hello world";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            SerializationUtil.serializer(msg, oos);
//            System.out.println(new String(baos.toByteArray()));

            oos.writeObject(msg);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));

            Object o = ois.readObject();
            System.out.println(o);

//            KryoPool pool = new KryoPool.Builder(Kryo::new).build();
//            Kryo kryo = pool.borrow();
//            Object obj = kryo.readClassAndObject(new Input(ois));
//            System.out.println(obj);
//
//
//            SerializationUtil.deserializer(baos.toByteArray(), null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Kryo kryo = new Kryo();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, msg);

        byte[] mesg = outputStream.toByteArray();

        System.out.println(new String(mesg));
        InputStream is = new ByteArrayInputStream(mesg);
        Input input = new Input(is);

        try {
            Object o = kryo.readObject(input, String.class);
            System.out.println("kryo read:" + o);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDeserialize() {

    }
}
