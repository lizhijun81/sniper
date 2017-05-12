package com.github.xiaoma.sniper.remoting.nio.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * Created by machunxiao on 2017/5/11.
 */
public class Acceptor extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(Acceptor.class);

    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    private SelectorProvider provider;
    private ServerSocketChannel server;
    private Selector selector;
    private ProcessorPool pool;
    private EventHandler handler;
    private EventDispatcher dispatcher;

    public Acceptor() throws IOException {

        provider = SelectorProvider.provider();
        server = provider.openServerSocketChannel();
        selector = provider.openSelector();

        handler = new EventHandler() {

            @Override
            public void onOpen() {
                logger.info("fire Accept open");
            }

            @Override
            public void onConnect() {
                logger.info("fire Accept connect");
            }

            @Override
            public void onRead() {
                logger.info("fire Accept read");
            }

            @Override
            public void onWrite() {
                logger.info("fire Accept write");
            }

            @Override
            public void onFlush() {
                logger.info("fire Accept flush");
            }

            @Override
            public void onClose() {
                logger.info("fire Accept close");
            }

            @Override
            public void onThrow() {
                logger.info("fire Accept throw");
            }
        };

        dispatcher = new EventDispatcher();

        pool = new ProcessorPool(CPU_NUM, dispatcher, handler);
    }

    @Override
    public void run() {
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isValid() && key.isAcceptable()) {
                        doAccept(key);
                    } else {
                        key.cancel();
                    }
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    public void bind(String host, int port) throws IOException {

        server.bind(new InetSocketAddress(host, port));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    private IChannel doAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();

        client.configureBlocking(false);

        ExecuteContext ctx = new ExecuteContext();
        Processor processor = pool.getNextProcessor();
        IChannel ic = new IChannel(client, handler);
        ic.setProcessor(processor);


        ctx.setChannel(ic);
        ctx.setProcessor(processor);
        logger.info("Accept a new client[id:{},address:{}]", ctx.getContextId(), client.getRemoteAddress());
        processor.newContext(ctx);
        return ic;
    }
}
