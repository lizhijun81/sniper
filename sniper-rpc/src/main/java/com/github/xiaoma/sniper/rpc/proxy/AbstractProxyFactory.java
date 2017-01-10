package com.github.xiaoma.sniper.rpc.proxy;

import com.github.xiaoma.sniper.rpc.Invoker;
import com.github.xiaoma.sniper.rpc.ProxyFactory;
import com.github.xiaoma.sniper.rpc.RpcException;

/**
 * Created by machunxiao on 17/1/10.
 */
public abstract class AbstractProxyFactory implements ProxyFactory {


    @Override
    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        return getProxy(invoker, new Class[]{invoker.getInterface()});
    }

    public abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] types);
}
