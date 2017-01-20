package com.github.xiaoma.sniper.remoting.exchange;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.ResponseFuture;

/**
 * Created by machunxiao on 17/1/10.
 */
public interface ExchangeChannel extends Channel {

    ResponseFuture request(Object request) throws RemotingException;

    ResponseFuture request(Object request, int timeout) throws RemotingException;

    ExchangeHandler getExchangeHandler();

    @Override
    void close(int timeout);
}
