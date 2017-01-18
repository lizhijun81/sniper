package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.*;
import com.github.xiaoma.sniper.core.extension.ExtensionLoader;
import com.github.xiaoma.sniper.remoting.*;

/**
 * Created by machunxiao on 16/12/27.
 */
public abstract class AbstractPeer implements Endpoint, ChannelListener {

    private final ChannelListener listener;
    private volatile URL url;
    private volatile boolean closed;

    public AbstractPeer(URL url, ChannelListener listener) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener == null");
        }
        this.url = url;
        this.listener = listener;

    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public ChannelListener getChannelListener() {
        return listener;
    }

    @Override
    public void send(Object message) throws RemotingException {
        send(message, false);
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public void close(int timeout) {
        close();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void connected(Channel channel) throws RemotingException {
        if (closed) {
            return;
        }
        listener.connected(channel);
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        listener.disconnected(channel);
    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {
        if (closed) {
            return;
        }
        listener.sent(channel, message);
    }

    @Override
    public void received(Channel channel, Object message) throws RemotingException {
        if (closed) {
            return;
        }
        listener.received(channel, message);
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        listener.caught(channel, exception);
    }

}
