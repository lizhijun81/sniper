package com.github.xiaoma.sniper.rpc;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.SPI;

/**
 * Created by machunxiao on 17/1/10.
 */
@SPI("jdk")
public interface ProxyFactory {

    <T> T getProxy(Invoker<T> invoker) throws RpcException;

    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;

}
