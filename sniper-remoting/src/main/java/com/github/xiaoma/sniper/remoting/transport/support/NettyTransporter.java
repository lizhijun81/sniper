package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.*;
import com.github.xiaoma.sniper.remoting.*;
import com.github.xiaoma.sniper.remoting.transport.Transporter;

/**
 * Created by machunxiao on 16/12/26.
 */
public class NettyTransporter implements Transporter {

    @Override
    public Server bind(URL url, ChannelListener listener) throws RemotingException {
        return new NettyServer(url, listener);
    }

    @Override
    public Client connect(URL url, ChannelListener listener) throws RemotingException {
        return new NioClient(url, listener);
    }
}
