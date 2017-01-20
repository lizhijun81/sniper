package com.github.xiaoma.sniper.rpc;

/**
 * Created by machunxiao on 17/1/10.
 */
public interface InvokerListener {

    void referred(Invoker<?> invoker) throws RpcException;

    void destroyed(Invoker<?> invoker) throws RpcException;

}
