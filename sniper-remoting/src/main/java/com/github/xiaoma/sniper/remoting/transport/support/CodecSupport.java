package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.ExtensionLoader;
import com.github.xiaoma.sniper.core.serialize.Serialization;

/**
 * Created by machunxiao on 17/1/17.
 */
public class CodecSupport {

    private CodecSupport() {
    }

    public static Serialization getSerialization(URL url) {
        ExtensionLoader<Serialization> loader = ExtensionLoader.getExtensionLoader(Serialization.class);
        return loader.getExtension(url.getParameter(Constants.SERIALIZATION_KEY, Constants.DEFAULT_SERIALIZATION));
    }
}
