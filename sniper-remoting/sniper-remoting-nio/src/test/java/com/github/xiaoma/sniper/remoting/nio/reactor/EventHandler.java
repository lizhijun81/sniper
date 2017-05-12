package com.github.xiaoma.sniper.remoting.nio.reactor;

/**
 * Created by machunxiao on 2017/5/11.
 */
public interface EventHandler {

    void onOpen();

    void onConnect();

    void onRead();

    void onWrite();

    void onFlush();

    void onClose();

    void onThrow();
}