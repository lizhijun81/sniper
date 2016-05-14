package com.github.xiaoma.rpc.core;

/**
 * 事件处理器
 * 对感兴趣的事件进行响应处理,实现业务处理.
 * Created by chunxiao on 2016/5/9.
 */
public class ServerHandler extends EventAdapter {

    @Override
    public void onRead(RpcRequest request) throws Exception {
        System.out.println("Received: " + "data");
    }
}
