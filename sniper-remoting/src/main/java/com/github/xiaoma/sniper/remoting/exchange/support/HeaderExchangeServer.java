package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.utils.NamedThreadFactory;
import com.github.xiaoma.sniper.remoting.*;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeChannel;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by machunxiao on 2017/1/10.
 */
public class HeaderExchangeServer implements ExchangeServer {

    private static final Logger logger = LoggerFactory.getLogger(HeaderExchangeServer.class);

    private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1, new NamedThreadFactory("sniper-remoting-server-heartbeat", true));

    private ScheduledFuture<?> heartbeatTimer;

    private int heartbeat;
    private int heartbeatTimeout;

    private final ExchangeServer server;
    private volatile boolean closed = false;

    public HeaderExchangeServer(ExchangeServer server) {
        if (server == null) {
            throw new IllegalArgumentException("server == null");
        }
        this.server = server;
        this.heartbeat = server.getUrl().getParameter(Constants.HEARTBEAT_KEY, 0);
        this.heartbeatTimeout = server.getUrl().getParameter(Constants.HEARTBEAT_TIMEOUT_KEY, heartbeat * 3);

        if (heartbeatTimeout < heartbeat * 2) {
            throw new IllegalStateException("heartbeatTimeout < heartbeatInterval * 2");
        }
        startHeartbeatTimer();
    }


    @Override
    public void reset(URL url) {

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
    public void close(int timeout) {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public Collection<ExchangeChannel> getExchangeChannels() {
        return null;
    }

    @Override
    public ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress) {
        return null;
    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public Collection<Channel> getChannels() {
        return null;
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        return null;
    }

    private void startHeartbeatTimer() {
        stopHeartbeatTimer();
        if (heartbeat <= 0) {
            return;
        }
        heartbeatTimer = scheduled.scheduleWithFixedDelay(new HeartbeatTask(heartbeat, heartbeatTimeout, getChannels()), heartbeat, heartbeatTimeout, TimeUnit.MILLISECONDS);
    }

    private void stopHeartbeatTimer() {
        try {
            ScheduledFuture<?> timer = heartbeatTimer;
            if (timer != null && !timer.isCancelled()) {
                timer.cancel(true);
            }
        } catch (Throwable e) {
            logger.error("", e);
        } finally {
            heartbeatTimer = null;
        }
    }
}
