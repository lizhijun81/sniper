package com.github.xiaoma.sniper.core.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by machunxiao on 2017/1/18.
 */
public enum RpcIdGenerator {
    ;
    protected static final AtomicLong offset = new AtomicLong();

    public static long rpcId() {
        return offset.incrementAndGet();
    }
}
