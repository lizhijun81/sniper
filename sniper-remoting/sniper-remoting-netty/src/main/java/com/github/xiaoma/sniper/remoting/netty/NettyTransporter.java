package com.github.xiaoma.sniper.remoting.netty;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.Client;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.Server;
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
        return new NettyClient(url, listener);
    }
}
