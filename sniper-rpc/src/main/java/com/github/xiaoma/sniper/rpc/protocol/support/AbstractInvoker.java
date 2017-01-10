package com.github.xiaoma.sniper.rpc.protocol.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by machunxiao on 17/1/10.
 */
public abstract class AbstractInvoker<T> implements Invoker<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Class<T> type;

    private final URL url;

    private final Map<String, String> attachment;

    private volatile boolean available = true;

    private volatile boolean destroyed = false;

    public AbstractInvoker(Class<T> type, URL url) {
        this(type, url, (Map<String, String>) null);
    }

    public AbstractInvoker(Class<T> type, URL url, String[] keys) {
        this(type, url, convertAttachment(url, keys));
    }

    public AbstractInvoker(Class<T> type, URL url, Map<String, String> attachment) {
        if (type == null)
            throw new IllegalArgumentException("service type == null");
        if (url == null)
            throw new IllegalArgumentException("service url == null");
        this.type = type;
        this.url = url;
        this.attachment = attachment == null ? null : Collections.unmodifiableMap(attachment);
    }

    private static Map<String, String> convertAttachment(URL url, String[] keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        Map<String, String> attachment = new HashMap<String, String>();
        for (String key : keys) {
            String value = url.getParameter(key);
            if (value != null && value.length() > 0) {
                attachment.put(key, value);
            }
        }
        return attachment;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        destroyed = true;
        setAvailable(false);
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        try {
            return doInvoke(invocation);
        } catch (Throwable e) {
            return new RpcResult(e);
        }
    }

    protected abstract Result doInvoke(Invocation invocation) throws Throwable;


    public String toString() {
        return getInterface() + " -> " + (getUrl() == null ? "" : getUrl().toString());
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
