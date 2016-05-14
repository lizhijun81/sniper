package com.github.xiaoma.rpc.core;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by chunxiao on 2016/5/9.
 */
public class Server implements Runnable {

    private static final AtomicLong requestId = new AtomicLong(1);
    private static int MAX_THREAD = 4;
    private ServerSocketChannel sschannel;
    private Selector selector;
    private InetSocketAddress address;
    private int port;

    private Notifier notifier = Notifier.getInstance();

    public Server(int port) throws Exception {
        this.selector = Selector.open();
        this.sschannel = ServerSocketChannel.open();
        this.sschannel.configureBlocking(false);
        this.port = port;
        this.address = new InetSocketAddress(port);
        ServerSocket ss = this.sschannel.socket();
        ss.bind(this.address);
        this.sschannel.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        System.out.println("server started ...");
        System.out.println("server listening on port:" + this.port);

        while (true) {
            try {
                int num = selector.select();
                if (num > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        // 处理I/O 事件
                        if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                            // 触发连接事件
                            notifier.fireOnAccept();
                            SocketChannel client = ssc.accept();
                            client.configureBlocking(false);


                            RpcRequest request = new RpcRequest();
                            request.setRequestId(requestId.getAndIncrement());


                            // 触发接收事件
                            notifier.fireOnAccepted(request);
                            // 注册读操作,以进行下一步的读操作
                            client.register(selector, SelectionKey.OP_READ, request);
                        } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                            // 提交读服务线程读取客户端数据
                            Reader.processRequest(key);
                            key.cancel();
                        } else if ((key.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE) {
                            // 提交写服务线程向客户端发送回应数据
                            Writer.processRequest(key);
                            key.cancel();
                        } else {
                            // 在selector中注册新的写通道
                            addRegister(key);
                        }
                    }
                }
            } catch (Exception e) {
                notifier.fireOnError("Error occurred in Server: " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
    }

    private void addRegister(SelectionKey key) {

    }
}
