package com.github.xiaoma.sniper.remoting.exchange;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.SPI;
import com.github.xiaoma.sniper.remoting.RemotingException;

/**
 * Created by machunxiao on 16/12/27.
 */
@SPI("header")
public interface Exchanger {
    /**
     * Bind a server.
     *
     * @param url      server url
     * @param listener
     * @return server
     * @throws RemotingException
     */
    ExchangeServer bind(URL url, ExchangeListener listener) throws RemotingException;

    /**
     * Connect to a server.
     *
     * @param url      server url
     * @param listener
     * @return client
     * @throws RemotingException
     */
    ExchangeClient connect(URL url, ExchangeListener listener) throws RemotingException;
}
