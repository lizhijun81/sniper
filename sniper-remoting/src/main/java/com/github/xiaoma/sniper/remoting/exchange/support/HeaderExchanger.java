package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.exchange.*;

/**
 * Created by machunxiao on 2017/1/10.
 */
public class HeaderExchanger implements Exchanger {

    @Override
    public ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException {
        return new HeaderExchangeServer(Exchangers.bind(url, handler));
    }

    @Override
    public ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException {
        return new HeaderExchangeClient(Exchangers.connect(url, handler));
    }
}
