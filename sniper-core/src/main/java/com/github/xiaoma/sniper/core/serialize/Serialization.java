package com.github.xiaoma.sniper.core.serialize;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.SPI;

import java.io.IOException;

/**
 * Created by machunxiao on 2016/12/28.
 */
@SPI("protostuff")
public interface Serialization {

    byte getContentTypeId();

    byte[] serialize(URL url, Object data) throws IOException;

    <T> T deserialize(URL url, byte[] data, Class<T> type) throws IOException;

}
