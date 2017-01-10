package com.github.xiaoma.sniper.remoting.exchange;

import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;

/**
 * Created by machunxiao on 17/1/10.
 */
public interface ExchangeHandler extends ChannelListener {

    Object reply(ExchangeChannel channel, Object request) throws RemotingException;

}
