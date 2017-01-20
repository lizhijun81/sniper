package com.github.xiaoma.sniper.remoting;

import com.github.xiaoma.sniper.core.URL;

import java.net.InetSocketAddress;

/**
 * Created by machunxiao on 16/12/26.
 */
public interface Endpoint {

    URL getUrl();

    ChannelListener getChannelListener();

    InetSocketAddress getLocalAddress();

    void send(Object message) throws RemotingException;

    void send(Object message, boolean sent) throws RemotingException;

    void close();

    void close(int timeout);

    boolean isClosed();

}
