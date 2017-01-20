package com.github.xiaoma.sniper.remoting;

import com.github.xiaoma.sniper.core.Resettable;

/**
 * Created by machunxiao on 16/12/26.
 */
public interface Client extends Channel, Endpoint, Resettable {
    /**
     * reconnect.
     */
    void reconnect() throws RemotingException;

}
