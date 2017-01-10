package com.github.xiaoma.sniper.rpc;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.URL;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

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

    @Getter
    @Setter
    private Future<?> future;

    @Getter
    @Setter
    private URL url;

    @Getter
    @Setter
    private String methodName;

    @Getter
    @Setter
    private Class<?>[] parameterTypes;

    @Getter
    @Setter
    private Object[] arguments;

    @Getter
    @Setter
    private InetSocketAddress localAddress;

    @Getter
    @Setter
    private InetSocketAddress remoteAddress;

    @Getter
    @Setter
    private final Map<String, String> attachments = new HashMap<>();

    private final Map<String, Object> values = new HashMap<>();

    public String getAttachment(String key) {
        return attachments.get(key);
    }

    public RpcContext setAttachment(String key, String value) {
        if (value == null) {
            attachments.remove(key);
        } else {
            attachments.put(key, value);
        }
        return this;
    }

    public RpcContext removeAttachment(String key) {
        attachments.remove(key);
        return this;
    }

    public Object get(String key) {
        return values.get(key);
    }

    public Map<String, Object> get() {
        return values;
    }

    public RpcContext set(String key, Object value) {
        values.put(key, value);
        return this;
    }

    public RpcContext remove(String key) {
        values.remove(key);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> Future<T> asyncCall(Callable<T> callable) {
        try {
            setAttachment(Constants.ASYNC_KEY, "true");
            try {
                T o = callable.call();
                if (o != null) {
                    FutureTask<T> f = new FutureTask<>(callable);
                    f.run();
                    return f;
                }
            } catch (Exception e) {
                throw new RpcException(e);
            } finally {
                removeAttachment(Constants.ASYNC_KEY);
            }
        } catch (RpcException e) {
            return new Future<T>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return false;
                }

                @Override
                public T get() throws InterruptedException, ExecutionException {
                    throw new ExecutionException(e.getCause());
                }

                @Override
                public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return get();
                }
            };
        }
        return (Future<T>) getContext().getFuture();
    }

    public void asyncCall(Runnable runnable) {
        try {
            setAttachment(Constants.RETURN_KEY, "false");
            runnable.run();
        } catch (Exception ex) {
            throw new RpcException(ex);
        } finally {
            removeAttachment(Constants.RETURN_KEY);
        }
    }
}
