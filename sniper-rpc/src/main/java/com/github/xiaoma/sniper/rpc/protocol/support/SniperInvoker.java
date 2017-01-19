package com.github.xiaoma.sniper.rpc.protocol.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.ResponseFuture;
import com.github.xiaoma.sniper.remoting.TimeoutException;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeClient;
import com.github.xiaoma.sniper.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by machunxiao on 17/1/10.
 */
public class SniperInvoker<T> extends AbstractInvoker<T> {

    private static final Logger logger = LoggerFactory.getLogger(SniperInvoker.class);

    private static final AtomicInteger index = new AtomicInteger(0);

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
        RpcInvocation inv = (RpcInvocation) invocation;
        final String methodName = inv.getMethodName();
        ExchangeClient currentClient;
        if (clients.length == 1) {
            currentClient = clients[0];
        } else {
            currentClient = clients[index.getAndIncrement() % clients.length];
        }

        try {
            int timeout = getUrl().getMethodParameter(methodName, Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
            ResponseFuture responseFuture = currentClient.request(invocation, timeout);
            Future<Result> future = new FutureAdapter<>(responseFuture);
            RpcContext.getContext().setFuture(future);
            return future.get();
        } catch (TimeoutException ex) {
            logger.error("", ex);
            throw ex;
        } catch (RemotingException ex) {
            logger.error("", ex);
            throw ex;
        }

    }
}
