package com.github.xiaoma.sniper.remoting.nio;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.transport.support.AbstractClient;

/**
 * Created by machunxiao on 16/12/26.
 */
public class NioClient extends AbstractClient {

    public NioClient(URL url, ChannelListener listener) throws RemotingException {
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
