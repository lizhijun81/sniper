package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by machunxiao on 17/1/16.
 */
public class NioChannel extends AbstractChannel {

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private SocketChannel sc;

    public NioChannel(URL url, ChannelListener listener) {
        super(url, listener);
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        try {
            return (InetSocketAddress) sc.getRemoteAddress();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        try {
            return (InetSocketAddress) sc.getLocalAddress();
        } catch (IOException e) {
            return null;
        }
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

        try {
            SocketChannel sc = SocketChannel.open();
            sc.write(ByteBuffer.wrap("".getBytes()));
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
