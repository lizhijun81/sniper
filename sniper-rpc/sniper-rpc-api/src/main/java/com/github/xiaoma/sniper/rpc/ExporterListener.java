package com.github.xiaoma.sniper.rpc;

/**
 * Created by machunxiao on 17/1/10.
 */
public interface ExporterListener {

    void exported(Exporter<?> exporter) throws RpcException;

    void destroyed(Exporter<?> exporter);
}
