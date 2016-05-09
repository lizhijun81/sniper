package com.github.xiaoma.rpc.core;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunxiao on 2016/5/9.
 */
public class Writer extends Thread {
    private static Notifier notifier = Notifier.getInstance();
    private static List<SelectionKey> pool = new ArrayList<>();

    @Override
    public void run() {
        while (true) {
            {
                try {
                    SelectionKey key;
                    synchronized (pool) {
                        while (pool.isEmpty()) {
                            pool.wait();
                        }
                        key = pool.remove(0);
                    }
                    // 向客户端发送数据,然后关闭连接,并分别触发 onWrite,onClosed事件
                    write(key);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 处理向客户发送数据
     *
     * @param key SelectionKey
     */
    public void write(SelectionKey key) {
        try {
            SocketChannel sc = (SocketChannel) key.channel();
            RpcRequest request = (RpcRequest) key.attachment();

            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());

            // 触发onWrite事件
            notifier.fireOnWrite(request, response);

            // 关闭
            sc.finishConnect();
            sc.socket().close();
            sc.close();

            // 触发onClosed事件
            notifier.fireOnClosed(request);
        } catch (Exception e) {
            notifier.fireOnError("Error occurred in Writer: " + e.getMessage());
        }
    }

    /**
     * 处理客户请求,管理用户的联结池,并唤醒队列中的线程进行处理
     */
    public static void processRequest(SelectionKey key) {
        synchronized (pool) {
            pool.add(pool.size(), key);
            pool.notifyAll();
        }
    }
}
