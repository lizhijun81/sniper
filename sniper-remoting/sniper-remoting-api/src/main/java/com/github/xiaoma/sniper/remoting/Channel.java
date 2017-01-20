package com.github.xiaoma.sniper.remoting;

import java.net.InetSocketAddress;

/**
 * Created by machunxiao on 16/12/26.
 */
public interface Channel extends Endpoint {

    InetSocketAddress getRemoteAddress();

    boolean isConnected();

    boolean hasAttribute(String key);

    Object getAttribute(String key);

    void setAttribute(String key, Object value);

    void removeAttribute(String key);

}
