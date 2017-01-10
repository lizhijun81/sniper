package com.github.xiaoma.sniper.rpc.proxy.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.rpc.Invoker;
import com.github.xiaoma.sniper.rpc.RpcException;
import com.github.xiaoma.sniper.rpc.proxy.AbstractProxyFactory;
import com.github.xiaoma.sniper.rpc.proxy.AbstractProxyInvoker;
import com.github.xiaoma.sniper.rpc.proxy.InvokerInvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by machunxiao on 17/1/10.
 */
public class JdkProxyFactory extends AbstractProxyFactory {

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException {
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] types) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), types, new InvokerInvocationHandler(invoker));
    }
}
