package com.github.xiaoma.sniper.rpc.protocol;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.rpc.Invocation;
import com.github.xiaoma.sniper.rpc.Invoker;
import com.github.xiaoma.sniper.rpc.Result;
import com.github.xiaoma.sniper.rpc.RpcException;

/**
 * Created by machunxiao on 17/1/20.
 */
public class InvokerWrapper<T> implements Invoker<T> {

    private final Invoker<T> invoker;

    private final URL url;

    public InvokerWrapper(Invoker<T> invoker, URL url) {
        this.invoker = invoker;
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return invoker.getUrl();
    }

    @Override
    public boolean isAvailable() {
        return invoker.isAvailable();
    }

    @Override
    public void destroy() {
        invoker.destroy();
    }

    @Override
    public Class<T> getInterface() {
        return invoker.getInterface();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }
}
