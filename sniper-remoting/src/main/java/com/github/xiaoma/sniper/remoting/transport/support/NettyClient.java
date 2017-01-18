package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.core.URL;

/**
 * Created by machunxiao on 16/12/26.
 */
public class NettyClient extends AbstractClient {

    public NettyClient(URL url, ChannelListener listener) {
        super(url, listener);
    }

    @Override
    protected void doOpen() throws Throwable {

    }

    @Override
    protected void doClose() throws Throwable {

    }

    @Override
    protected void doConnect() throws Throwable {

    }

    @Override
    protected void doDisconnect() throws Throwable {

    }

    @Override
    protected Channel getChannel() {
        return null;
    }


}
