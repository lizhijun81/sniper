package com.github.xiaoma.sniper.rpc;

/**
 * Created by machunxiao on 17/1/6.
 */
public class RpcException extends RuntimeException {

    public RpcException() {
        super();
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
