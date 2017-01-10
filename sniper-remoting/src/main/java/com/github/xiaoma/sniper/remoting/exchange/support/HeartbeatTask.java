package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.Client;
import com.github.xiaoma.sniper.remoting.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by machunxiao on 2017/1/10.
 */
final class HeartbeatTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatTask.class);

    private final int heartbeat;
    private final int heartbeatTimeout;
    private final Collection<Channel> channels;

    HeartbeatTask(int heartbeat, int heartbeatTimeout, Collection<Channel> channels) {
        this.heartbeat = heartbeat;
        this.heartbeatTimeout = heartbeatTimeout;
        this.channels = channels;
    }

    @Override
    public void run() {
        try {
            long now = System.currentTimeMillis();
            for (Channel channel : channels) {
                if (channel.isClosed()) {
                    continue;
                }
                try {
                    Long lastRead = (Long) channel.getAttribute(Constants.KEY_READ_TIMESTAMP);
                    Long lastWrite = (Long) channel.getAttribute(Constants.KEY_WRITE_TIMESTAMP);
                    if ((lastRead != null && now - lastWrite > heartbeat)
                            || (lastWrite != null && now - lastWrite > heartbeat)) {

                        Request request = new Request();
                        request.setVersion("1.0.0");
                        request.setData("heartbeat");

                        channel.send(request);

                        if (logger.isDebugEnabled()) {
                            logger.debug("Send heartbeat to remote channel " + channel.getRemoteAddress()
                                    + ", cause: The channel has no data-transmission exceeds a heartbeat period: " + heartbeat + "ms");
                        }
                    }
                    if (lastRead != null && now - lastRead > heartbeatTimeout) {
                        logger.warn("Close channel" + channel
                                + ", because heartbeat read idle time out: " + heartbeatTimeout + "ms");
                        if (channel instanceof Client) {
                            try {
                                ((Client) channel).reconnect();
                            } catch (Exception ex) {

                            }
                        } else {
                            channel.close();
                        }
                    }


                } catch (Throwable ex) {

                }
            }
        } catch (Throwable ex) {

        }

    }
}
