package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by machunxiao on 2017/1/18.
 */
public class NettyChannel extends AbstractChannel {

    private static final ConcurrentMap<Channel, NettyChannel> channelMap = new ConcurrentHashMap<>();

    private NettyChannel(URL url, ChannelListener listener) {
        super(url, listener);
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean hasAttribute(String key) {
        return false;
    }

    @Override
    public Object getAttribute(String key) {
        return null;
    }

    @Override
    public void setAttribute(String key, Object value) {

    }

    @Override
    public void removeAttribute(String key) {

    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    public static NettyChannel getChannel(Channel ch, URL url, ChannelListener listener) {
        if (ch == null) {
            return null;
        }
        return channelMap.computeIfAbsent(ch, channel -> new NettyChannel(url, listener));
    }
}
