package com.github.xiaoma.sniper.rpc.sniper;

import com.github.xiaoma.sniper.rpc.Exporter;
import com.github.xiaoma.sniper.rpc.Invoker;
import com.github.xiaoma.sniper.rpc.protocol.AbstractExporter;

import java.util.Map;

/**
 * Created by machunxiao on 17/1/10.
 */
public class SniperExporter<T> extends AbstractExporter<T> {

    private final String key;

    private final Map<String, Exporter<?>> exporterMap;

    public SniperExporter(Invoker<T> invoker, String serviceKey, Map<String, Exporter<?>> exporterMap) {
        super(invoker);
        this.key = serviceKey;
        this.exporterMap = exporterMap;
    }

    @Override
    public void destroyed() {
        super.destroyed();
        exporterMap.remove(key);
    }
}
