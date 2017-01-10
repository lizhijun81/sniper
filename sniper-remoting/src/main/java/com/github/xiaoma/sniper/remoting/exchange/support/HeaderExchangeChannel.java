package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.ResponseFuture;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeChannel;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeHandler;

import java.net.InetSocketAddress;

/**
 * Created by machunxiao on 2017/1/10.
 */
final class HeaderExchangeChannel implements ExchangeChannel {

    HeaderExchangeChannel(Channel channel) {
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
    public URL getUrl() {
        return null;
    }

    @Override
    public ChannelListener getChannelListener() {
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
    public boolean isClosed() {
        return false;
    }

    @Override
    public ResponseFuture request(Object request) throws RemotingException {
        return null;
    }

    @Override
    public ResponseFuture request(Object request, int timeout) throws RemotingException {
        return null;
    }

    @Override
    public ExchangeHandler getExchangeHandler() {
        return null;
    }

    @Override
    public void close(int timeout) {

    }
}
