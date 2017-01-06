package com.github.xiaoma.sniper.rpc;

/**
 * Created by machunxiao on 17/1/6.
 */
public interface Filter {

    Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException;
}
