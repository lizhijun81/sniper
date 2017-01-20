package com.github.xiaoma.sniper.rpc.proxy.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.rpc.Invoker;
import com.github.xiaoma.sniper.rpc.RpcException;
import com.github.xiaoma.sniper.rpc.proxy.AbstractProxyFactory;

/**
 * Created by machunxiao on 17/1/10.
 */
public class JavassistProxyFactory extends AbstractProxyFactory {

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] types) {
        return null;
    }
}
