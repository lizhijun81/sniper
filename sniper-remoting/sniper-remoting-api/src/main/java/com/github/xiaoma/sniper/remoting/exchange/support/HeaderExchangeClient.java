package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.utils.NamedThreadFactory;
import com.github.xiaoma.sniper.remoting.*;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeChannel;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeClient;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by machunxiao on 2017/1/10.
 */
public class HeaderExchangeClient implements ExchangeClient {

    private static final Logger logger = LoggerFactory.getLogger(HeaderExchangeClient.class);

    private final ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("sniper-remoting-client-heartbeat", true));

    private ScheduledFuture<?> heartbeatTimer;

    private int heartbeat;
    private int heartbeatTimeout;

    private final Client client;
    private final ExchangeChannel channel;

    public HeaderExchangeClient(Client client) {
        this.client = client;
        this.channel = new HeaderExchangeChannel(client);
        this.heartbeat = client.getUrl().getParameter(Constants.HEARTBEAT_KEY, 0);
        this.heartbeatTimeout = client.getUrl().getParameter(Constants.HEARTBEAT_TIMEOUT_KEY, heartbeat * 3);
        if (heartbeatTimeout < heartbeat * 2) {
            throw new IllegalStateException("heartbeatTimeout < heartbeatInterval * 2");
        }
        startHeartbeatTimer();
    }

    @Override
    public void reconnect() throws RemotingException {
        client.reconnect();
    }

    @Override
    public void reset(URL url) {
        client.reset(url);
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return channel.getRemoteAddress();
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
    public InetSocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    @Override
    public void send(Object message) throws RemotingException {
        channel.send(message);
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {
        channel.send(message, sent);
    }

    @Override
    public void close() {
        if (isClosed()) {
            return;
        }
        doClose();
        channel.close();
    }

    @Override
    public void close(int timeout) {
        doClose();
        channel.close(timeout);
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

        return channel.request(request, timeout);
    }

    @Override
    public ExchangeHandler getExchangeHandler() {
        return channel.getExchangeHandler();
    }

    private void doClose() {
        stopHeartbeatTimer();
    }

    private void startHeartbeatTimer() {
        stopHeartbeatTimer();
        if (heartbeat <= 0) {
            return;
        }
        heartbeatTimer = scheduled.scheduleWithFixedDelay(new HeartbeatTask(heartbeat, heartbeatTimeout, Collections.singletonList(HeaderExchangeClient.this)), heartbeat, heartbeatTimeout, TimeUnit.MILLISECONDS);

    }

    private void stopHeartbeatTimer() {
        try {
            ScheduledFuture<?> timer = heartbeatTimer;
            if (timer != null && !timer.isCancelled()) {
                timer.cancel(true);
            }
            scheduled.purge();
        } catch (Throwable e) {
            logger.error("", e);
        } finally {
            heartbeatTimer = null;
        }
    }
}
