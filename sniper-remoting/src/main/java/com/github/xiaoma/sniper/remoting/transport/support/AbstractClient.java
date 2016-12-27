package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.Client;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.core.URL;

import java.net.InetSocketAddress;

/**
 * Created by machunxiao on 16/12/26.
 */
public abstract class AbstractClient implements Client {

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public void send(Object message) throws RemotingException {

    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {

    }

    @Override
    public void close() {

    }

    @Override
    public void close(int timeout) {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void reconnect() throws RemotingException {

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

    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;

    protected abstract void doConnect() throws Throwable;

    protected abstract void doDisconnect() throws Throwable;

    protected abstract Channel getChannel();
}
