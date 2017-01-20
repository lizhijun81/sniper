package com.github.xiaoma.sniper.rpc.protocol;

import com.github.xiaoma.sniper.rpc.Exporter;
import com.github.xiaoma.sniper.rpc.Invoker;
import com.github.xiaoma.sniper.rpc.Protocol;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by machunxiao on 17/1/10.
 */
public abstract class AbstractProtocol implements Protocol {

    protected final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();
    protected final Set<Invoker<?>> invokers = new HashSet<>();


    @Override
    public void destroy() {
        for (Invoker<?> invoker : invokers) {
            if (invoker != null) {
                invoker.destroy();
            }
        }
        for (String key : exporterMap.keySet()) {
            Exporter<?> exporter = exporterMap.get(key);
            if (exporter != null) {
                exporter.destroyed();
            }
        }
        invokers.clear();
        exporterMap.clear();
    }
}
