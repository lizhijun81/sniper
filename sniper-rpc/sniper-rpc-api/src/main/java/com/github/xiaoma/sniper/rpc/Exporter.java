package com.github.xiaoma.sniper.rpc;

/**
 * Created by machunxiao on 17/1/6.
 */
public interface Exporter<T> {

    Invoker<T> getInvoker();

    void destroyed();
}
