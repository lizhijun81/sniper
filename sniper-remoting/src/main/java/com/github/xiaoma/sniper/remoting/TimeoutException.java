package com.github.xiaoma.sniper.remoting;

/**
 * Created by machunxiao on 17/1/19.
 */
public class TimeoutException extends RemotingException {

    private static final long serialVersionUID = -2565703181617458920L;

    public TimeoutException(Channel channel, String msg) {
        super(channel, msg);
    }
}
