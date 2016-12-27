package com.github.xiaoma.sniper.core.extension;

/**
 * Created by machunxiao on 16/12/27.
 */
@SPI
public interface ExtensionFactory {

    <T> T getExtension(Class<T> type, String name);

}
