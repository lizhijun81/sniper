package com.github.xiaoma.sniper.remoting.nio;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.transport.support.AbstractChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by machunxiao on 17/1/16.
 */
final class NioChannel extends AbstractChannel {

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private SocketChannel sc;

    public NioChannel(SocketChannel sc, URL url, ChannelListener listener) {
        super(url, listener);
        this.sc = sc;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) sc.socket().getRemoteSocketAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) sc.socket().getLocalSocketAddress();
    }

    @Override
    public boolean isConnected() {
        return sc.isConnected();
    }

    @Override
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {
        super.send(message, sent);
        // encode
        ByteBuffer buffer = null;
        try {
            sc.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
