package com.github.xiaoma.sniper.remoting.transport;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.SPI;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.Client;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.Server;

/**
 * Created by machunxiao on 16/12/26.
 */
@SPI("nio")
public interface Transporter {

    /**
     * Bind a server.
     *
     * @param url      server url
     * @param listener
     * @return server
     * @throws RemotingException
     */
    Server bind(URL url, ChannelListener listener) throws RemotingException;

    /**
     * Connect to a server.
     *
     * @param url      server url
     * @param listener
     * @return client
     * @throws RemotingException
     */
    Client connect(URL url, ChannelListener listener) throws RemotingException;

}
