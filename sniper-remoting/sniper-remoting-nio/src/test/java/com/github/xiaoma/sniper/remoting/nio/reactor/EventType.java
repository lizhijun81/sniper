package com.github.xiaoma.sniper.remoting.nio.reactor;

/**
 * Created by machunxiao on 2017/5/11.
 */
public enum EventType {
    OPEN,
    CONNECT,
    READ,
    WRITE,
    FLUSH,
    CLOSE,
    THROW;
}
