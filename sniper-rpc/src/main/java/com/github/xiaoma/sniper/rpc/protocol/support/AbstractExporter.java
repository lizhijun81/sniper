package com.github.xiaoma.sniper.rpc.protocol.support;

import com.github.xiaoma.sniper.rpc.Exporter;
import com.github.xiaoma.sniper.rpc.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by machunxiao on 17/1/10.
 */
public abstract class AbstractExporter<T> implements Exporter<T> {


    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Invoker<T> invoker;

    private volatile boolean destroyed = false;

    public AbstractExporter(Invoker<T> invoker) {
        if (invoker == null)
            throw new IllegalStateException("service invoker == null");
        if (invoker.getInterface() == null)
            throw new IllegalStateException("service type == null");
        if (invoker.getUrl() == null)
            throw new IllegalStateException("service url == null");
        this.invoker = invoker;
    }

    @Override
    public Invoker<T> getInvoker() {
        return invoker;
    }

    @Override
    public void destroyed() {
        if (destroyed) {
            return;
        }
        getInvoker().destroy();
        logger.info(invoker.getInterface().getName() + " destroyed");
    }

    @Override
    public String toString() {
        return getInvoker().toString();
    }
}
