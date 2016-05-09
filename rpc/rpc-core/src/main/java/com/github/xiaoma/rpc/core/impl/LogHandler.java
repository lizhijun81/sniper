package com.github.xiaoma.rpc.core.impl;

import com.github.xiaoma.rpc.core.EventAdapter;
import com.github.xiaoma.rpc.core.RpcRequest;

import java.util.Date;

/**
 * Created by chunxiao on 2016/5/9.
 */
public class LogHandler extends EventAdapter {

    @Override
    public void onClosed(RpcRequest request) throws Exception {
        String log = new Date().toString() + " from " + request.getClientIp();
        System.out.println(log);
    }

    @Override
    public void onError(String error) {
        System.out.println("Error: " + error);
    }
}
