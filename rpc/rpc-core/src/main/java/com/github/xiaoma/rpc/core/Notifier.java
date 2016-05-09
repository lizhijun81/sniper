package com.github.xiaoma.rpc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件触发器,用于在适当的时候通过触发服务器事件,通知注册的事件处理器对事件做出响应.
 * <p>
 * 触发器以单例模式实现,统一控制整个服务器端的事件,避免造成混乱
 * Created by chunxiao on 2016/5/9.
 */
public class Notifier {
    private static List<ServerListener> listeners;
    private static Notifier instance = null;

    private Notifier() {
        listeners = new ArrayList<>();
    }

    public static synchronized Notifier getInstance() {
        if (instance == null) {
            instance = new Notifier();
            return instance;
        }
        return instance;
    }

    public void addListener(ServerListener listener) {
        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    public void fireOnAccept() throws Exception {
        for (ServerListener listener : listeners) {
            listener.onAccept();
        }
    }

    public void fireOnAccepted(RpcRequest request) throws Exception {
        for (ServerListener listener : listeners) {
            listener.onAccepted(request);
        }
    }

    public void fireOnRead(RpcRequest request) throws Exception {
        for (ServerListener listener : listeners) {
            listener.onRead(request);
        }
    }

    public void fireOnWrite(RpcRequest request, RpcResponse response) throws Exception {
        for (ServerListener listener : listeners) {
            listener.onWrite(request, response);
        }
    }

    public void fireOnError(String errorMsg) {
        for (ServerListener listener : listeners) {
            listener.onError(errorMsg);
        }
    }

    public void fireOnClosed(RpcRequest request) throws Exception {
        for (ServerListener listener : listeners) {
            listener.onClosed(request);
        }
    }
}
