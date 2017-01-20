package com.github.xiaoma.sniper.rpc;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.SPI;

/**
 * Created by machunxiao on 17/1/6.
 */
@SPI
public interface Protocol {

    int getDefaultPort();

    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;

    <T> Invoker<T> refer(Class<T> clazz, URL url) throws RpcException;

    void destroy();
}
