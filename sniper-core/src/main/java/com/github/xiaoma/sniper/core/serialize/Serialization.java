package com.github.xiaoma.sniper.core.serialize;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.SPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by machunxiao on 2016/12/28.
 */
@SPI("protobuf")
public interface Serialization {

    byte getContentTypeId();

    ObjectOutput serialize(URL url, OutputStream os) throws IOException;

    ObjectInput deserialize(URL url, InputStream is) throws IOException;

}
