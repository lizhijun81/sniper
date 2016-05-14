package com.github.xiaoma.rpc.core;

/**
 * Created by chunxiao on 2016/5/9.
 */
public interface ServerListener {
    void onError(String error);

    void onAccept() throws Exception;

    void onAccepted(RpcRequest request) throws Exception;

    void onRead(RpcRequest request) throws Exception;

    void onWrite(RpcRequest request, RpcResponse response) throws Exception;

    void onClosed(RpcRequest request) throws Exception;
}
