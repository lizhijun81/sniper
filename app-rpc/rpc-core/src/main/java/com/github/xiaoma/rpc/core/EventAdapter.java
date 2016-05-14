package com.github.xiaoma.rpc.core;

/**
 * Created by chunxiao on 2016/5/9.
 */
public abstract class EventAdapter implements ServerListener {
    public EventAdapter() {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onAccept() throws Exception {

    }

    @Override
    public void onAccepted(RpcRequest request) throws Exception {

    }

    @Override
    public void onRead(RpcRequest request) throws Exception {

    }

    @Override
    public void onWrite(RpcRequest request, RpcResponse response) throws Exception {

    }

    @Override
    public void onClosed(RpcRequest request) throws Exception {

    }
}
