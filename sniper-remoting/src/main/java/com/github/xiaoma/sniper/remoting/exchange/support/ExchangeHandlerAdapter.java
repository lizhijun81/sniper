package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeChannel;
import com.github.xiaoma.sniper.remoting.exchange.ExchangeHandler;

/**
 * Created by machunxiao on 17/1/10.
 */
public class ExchangeHandlerAdapter implements ExchangeHandler {

    @Override
    public void connected(Channel channel) throws RemotingException {

    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {

    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {

    }

    @Override
    public void received(Channel channel, Object message) throws RemotingException {

    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {

    }

    @Override
    public Object reply(ExchangeChannel channel, Object request) throws RemotingException {
        return null;
    }
}
