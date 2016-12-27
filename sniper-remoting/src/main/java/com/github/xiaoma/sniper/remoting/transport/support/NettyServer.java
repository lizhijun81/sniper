package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.core.URL;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Created by machunxiao on 16/12/26.
 */
public class NettyServer extends AbstractServer {

    public NettyServer(URL url, ChannelListener listener) throws RemotingException {
        super(url, listener);
    }

    @Override
    protected void doOpen() throws Throwable {

    }

    @Override
    protected void doClose() throws Throwable {

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
}
