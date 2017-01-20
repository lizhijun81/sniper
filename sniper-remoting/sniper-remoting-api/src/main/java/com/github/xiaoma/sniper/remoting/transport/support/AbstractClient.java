package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.Client;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.core.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by machunxiao on 16/12/26.
 */
public abstract class AbstractClient extends AbstractEndpoint implements Client {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

    private static final Lock connectLock = new ReentrantLock();

    public AbstractClient(URL url, ChannelListener listener) throws RemotingException {
        super(url, listener);
        try {
            doOpen();
        } catch (Throwable t) {
            close();
            throw new RemotingException(null, "");
        }
        try {
            connect();
        } catch (RemotingException ex) {

        } catch (Throwable t) {
            close();
            throw new RemotingException(null, "");
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {

    }

    @Override
    public void reconnect() throws RemotingException {
        disconnect();
        connect();
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
    public void close() {

    }

    @Override
    public void close(int timeout) {

    }

    @Override
    public String toString() {
        return getClass().getName() + " [" + getLocalAddress() + " -> " + getRemoteAddress() + "]";
    }

    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;

    protected abstract void doConnect() throws Throwable;

    protected abstract void doDisconnect() throws Throwable;

    protected abstract Channel getChannel();

    protected void connect() throws RemotingException {
        connectLock.lock();
        try {
            if (isConnected()) {
                return;
            }
            doConnect();
        } catch (RemotingException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new RemotingException(null, "", ex);
        } finally {
            connectLock.unlock();
        }
    }

    protected void disconnect() throws RemotingException {
        connectLock.lock();
        try {
            try {
                Channel channel = getChannel();
                if (channel != null) {
                    channel.close();
                }
            } catch (Throwable ex) {
                logger.error("", ex);
            }
            try {
                doDisconnect();
            } catch (Throwable ex) {
                logger.error("", ex);
            }
        } finally {
            connectLock.unlock();
        }
    }

    protected SocketAddress getConnectAddress() {
        return null;
    }
}
