package com.github.xiaoma.sniper.rpc;

import com.github.xiaoma.sniper.core.URL;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by machunxiao on 16/12/26.
 */
public class RpcContext {

    private static final ThreadLocal<RpcContext> local = ThreadLocal.withInitial(RpcContext::new);

    public static RpcContext getContext() {
        return local.get();
    }

    public static void removeContext() {
        local.remove();
    }

    private Future<?> future;

    private URL url;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private InetSocketAddress localAddress;

    private InetSocketAddress remoteAddress;

    private final Map<String, String> attachments = new HashMap<>();

    private final Map<String, Object> values = new HashMap<>();


}
