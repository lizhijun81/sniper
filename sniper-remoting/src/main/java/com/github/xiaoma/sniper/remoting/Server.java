package com.github.xiaoma.sniper.remoting;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Created by machunxiao on 16/12/26.
 */
public interface Server extends Endpoint {
    /**
     * is bound.
     *
     * @return bound
     */
    boolean isBound();

    /**
     * get channels.
     *
     * @return channels
     */
    Collection<Channel> getChannels();

    /**
     * get channel.
     *
     * @param remoteAddress
     * @return channel
     */
    Channel getChannel(InetSocketAddress remoteAddress);
}
