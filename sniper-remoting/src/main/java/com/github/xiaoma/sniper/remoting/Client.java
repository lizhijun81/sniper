package com.github.xiaoma.sniper.remoting;

/**
 * Created by machunxiao on 16/12/26.
 */
public interface Client extends Channel, Endpoint {
    /**
     * reconnect.
     */
    void reconnect() throws RemotingException;

}
