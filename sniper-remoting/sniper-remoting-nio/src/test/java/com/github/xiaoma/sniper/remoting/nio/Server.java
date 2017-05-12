package com.github.xiaoma.sniper.remoting.nio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class was Deprecated
 * Please use {@link com.github.xiaoma.sniper.remoting.nio.reactor.ServerBootstrap}
 * <p>
 * Created by machunxiao on 2017/3/21.
 */
@Deprecated
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static final AtomicLong idx = new AtomicLong(1);

    private final int coreNum = Runtime.getRuntime().availableProcessors();
    private final Processor[] processors = new Processor[coreNum];

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
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        for (int i = 0; i < processors.length; i++) {
            processors[i] = new Processor();
        }

        new Thread(new AcceptTask()).start();

    }

    private class AcceptTask implements Runnable {

        @Override
        public void run() {
            try {

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
                        } else {
                            key.cancel();
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
