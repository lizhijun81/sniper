package com.github.xiaoma.sniper.remoting;

/**
 * Created by machunxiao on 16/12/26.
 */
public class RemotingException extends Exception {
    private static final long serialVersionUID = -8811478161339274658L;

    public RemotingException(Channel channel, String msg) {
        super(msg);
    }
}
