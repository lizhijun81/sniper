package com.github.xiaoma.sniper.rpc.protocol.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeClient;
import com.github.xiaoma.sniper.rpc.Invocation;
import com.github.xiaoma.sniper.rpc.Invoker;
import com.github.xiaoma.sniper.rpc.Result;

import java.util.Set;

/**
 * Created by machunxiao on 17/1/10.
 */
public class SniperInvoker<T> extends AbstractInvoker<T> {

    private final ExchangeClient[] clients;

    private final Set<Invoker<?>> invokers;

    private final String version;

    public SniperInvoker(Class<T> serviceType, URL url, ExchangeClient[] clients) {
        this(serviceType, url, clients, null);
    }

    public SniperInvoker(Class<T> serviceType, URL url, ExchangeClient[] clients, Set<Invoker<?>> invokers) {
        super(serviceType, url, new String[]{Constants.INTERFACE_KEY, Constants.GROUP_KEY, Constants.TOKEN_KEY, Constants.TIMEOUT_KEY});
        this.clients = clients;
        this.invokers = invokers;
        this.version = url.getParameter(Constants.VERSION_KEY, "0.0.0");
    }

    @Override
    protected Result doInvoke(Invocation invocation) throws Throwable {
        return null;
    }
}
