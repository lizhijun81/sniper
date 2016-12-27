package com.github.xiaoma.sniper.core.extension.support;

import com.github.xiaoma.sniper.core.extension.ExtensionFactory;
import com.github.xiaoma.sniper.core.extension.ExtensionLoader;
import com.github.xiaoma.sniper.core.extension.SPI;

/**
 * Created by machunxiao on 16/12/27.
 */
public class SpiExtensionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (!type.isInterface() || !type.isAnnotationPresent(SPI.class)) {
            return null;
        }
        ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
        return loader.getExtension("spi");
    }
}
