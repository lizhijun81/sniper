package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.Server;
import com.github.xiaoma.sniper.core.URL;

import java.net.InetSocketAddress;

/**
 * Created by machunxiao on 16/12/26.
 */
public abstract class AbstractServer extends AbstractEndpoint implements Server {

    private InetSocketAddress bindAddress;

    public AbstractServer(URL url, ChannelListener listener) throws RemotingException {
        super(url, listener);
        String host = getUrl().getHost();
        bindAddress = new InetSocketAddress(host, getUrl().getPort());
        try {
            doOpen();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new RemotingException(null, null);
        }
    }


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
        try {
            doClose();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(int timeout) {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void reset(URL url) {

    }

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;
}
