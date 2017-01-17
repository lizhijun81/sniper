package com.github.xiaoma.sniper.core.serialize.support.java;


import com.github.xiaoma.sniper.core.serialize.ObjectOutput;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by machunxiao on 17/1/17.
 */
public class JavaOutput implements ObjectOutput {

    private final ObjectOutputStream oos;

    public JavaOutput(OutputStream os) throws IOException {
        this.oos = new ObjectOutputStream(os);
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        oos.writeObject(obj);
    }
}
