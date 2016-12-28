package com.github.xiaoma.sniper.core.serialize;

import com.github.xiaoma.sniper.core.extension.SPI;

import java.io.IOException;

/**
 * Created by machunxiao on 2016/12/28.
 */
@SPI("protobuf")
public interface Serialization {

    Object serialize(byte[] data) throws IOException;

    byte[] deserialize(Object msg) throws IOException;

}
