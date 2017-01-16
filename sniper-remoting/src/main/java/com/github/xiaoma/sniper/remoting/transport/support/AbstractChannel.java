package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.core.URL;

import java.net.InetSocketAddress;

/**
 * Created by machunxiao on 16/12/27.
 */
public abstract class AbstractChannel extends AbstractPeer implements Channel {


    public AbstractChannel(URL url, ChannelListener listener) {
        super(url, listener);
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {
        if (isClosed()) {
            throw new RemotingException(this, "Fail to send message ,cause:Channel closed. channel: " + getLocalAddress() + " -> " + getRemoteAddress());
        }
    }

    @Override
    public String toString() {
        return getLocalAddress() + "->" + getRemoteAddress();
    }
}
