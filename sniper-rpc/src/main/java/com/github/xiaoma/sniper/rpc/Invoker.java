package com.github.xiaoma.sniper.rpc;

import com.github.xiaoma.sniper.core.Node;

/**
 * Created by machunxiao on 17/1/6.
 */
public interface Invoker<T> extends Node {

    /**
     * get service interface
     *
     * @return
     */
    Class<T> getInterface();

    /**
     * @param invocation
     * @return
     * @throws RpcException
     */
    Result invoke(Invocation invocation) throws RpcException;
}
