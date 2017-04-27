package com.github.xiaoma.sniper.remoting.nio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by machunxiao on 2017/3/21.
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static final AtomicLong idx = new AtomicLong(1);

    private Selector selector;
    private ServerSocketChannel ssc;

    private String host;
    private int port;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 7788;
        Server server = new Server(host, port);
        server.open();

    }

    public void open() throws IOException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(host, port));
        ssc.configureBlocking(false);

        new Thread(new ProcessTask()).start();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ProcessTask implements Runnable {

        @Override
        public void run() {
            try {
                ssc.register(selector, SelectionKey.OP_ACCEPT);

                int coreNum = Runtime.getRuntime().availableProcessors();
                Processor[] processors = new Processor[coreNum];
                for (int i = 0; i < processors.length; i++) {
                    processors[i] = new Processor();
                }
                int index = 0;
                while (selector.select() > 0) {
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        if (key.isValid() && key.isAcceptable()) {
                            SocketChannel client = ssc.accept();
                            client.configureBlocking(false);
                            logger.info("accept a new client:{},idx:{}", client, idx.getAndIncrement());
                            Processor processor = processors[(index++) % coreNum];
                            processor.addChannel(client);
                        }
                        it.remove();
                    }
                }
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
