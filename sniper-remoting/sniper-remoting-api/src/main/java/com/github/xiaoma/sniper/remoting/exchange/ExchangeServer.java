package com.github.xiaoma.sniper.remoting.exchange;

import com.github.xiaoma.sniper.remoting.Server;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Created by machunxiao on 16/12/27.
 */
public interface ExchangeServer extends Server {

    Collection<ExchangeChannel> getExchangeChannels();

    ExchangeChannel getExchangeChannel(InetSocketAddress remoteAddress);

}
