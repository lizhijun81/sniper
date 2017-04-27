package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.utils.RpcIdGenerator;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.ResponseFuture;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeChannel;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeHandler;
import com.github.xiaoma.sniper.remoting.exchange.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by machunxiao on 2017/1/10.
 */
final class HeaderExchangeChannel implements ExchangeChannel {

    private static final Logger logger = LoggerFactory.getLogger(HeaderExchangeChannel.class);

    private final Channel channel;

    HeaderExchangeChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel == null");
        }
        this.channel = channel;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return channel.getRemoteAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
    public boolean hasAttribute(String key) {
        return channel.hasAttribute(key);
    }

    @Override
    public Object getAttribute(String key) {
        return channel.getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        channel.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        channel.removeAttribute(key);
    }

    @Override
    public URL getUrl() {
        return channel.getUrl();
    }

    @Override
    public ChannelListener getChannelListener() {
        return channel.getChannelListener();
    }

    @Override
    public void send(Object message) throws RemotingException {
        send(message, getUrl().getParameter(Constants.SENT_KEY, false));
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {
        Request req = new Request();
        req.setRequestId(RpcIdGenerator.requestId());
        req.setData(message);   // message instance Invocation == true
        req.setEvent((byte) 0);
        req.setVersion("0.0.1");
        channel.send(req, sent);
    }

    @Override
    public void close() {
        try {
            channel.close();
        } catch (Throwable e) {
            logger.error("", e);
        }
    }

    @Override
    public boolean isClosed() {
        return channel.isClosed();
    }

    @Override
    public ResponseFuture request(Object request) throws RemotingException {
        return request(request, channel.getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT));

    }

    @Override
    public ResponseFuture request(Object request, int timeout) throws RemotingException {
        Request req = new Request();
        req.setRequestId(RpcIdGenerator.requestId());
        req.setData(request);
        req.setEvent((byte) 1);
        req.setVersion("0.0.1");
        DefaultFuture future = new DefaultFuture(channel, req, timeout);
        try {
            channel.send(req);
        } catch (RemotingException e) {
            future.cancel();
            throw e;
        }
        return future;
    }

    @Override
    public ExchangeHandler getExchangeHandler() {
        return null;
    }

    @Override
    public void close(int timeout) {

    }
}
