package com.github.xiaoma.rpc.core.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by chunxiao on 2016/5/14.
 */
public class Reactor implements Runnable {

    static final int MAX_INPUT = 4096;
    static final int MAX_OUTPUT = 4096;
    final Selector selector;
    final ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException {
        this.selector = Selector.open();
        this.serverSocket = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(port);
        this.serverSocket.bind(address);
        this.serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(this.selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor(this.selector, this.serverSocket)); // attach callback object

        /*
        或者使用下面方式注册
        SelectorProvider provider = SelectorProvider.provider();
        this.selector = provider.openSelector();
        this.serverSocket = provider.openServerSocketChannel();
        */
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selected.iterator();
                while (iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    iterator.remove();
                    dispatch(sk);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SelectionKey sk) {
        Runnable r = (Runnable) sk.attachment();
        if (r != null) {
            r.run();
        }
    }

    static class Acceptor implements Runnable {

        final Selector selector;
        final ServerSocketChannel ss;

        Acceptor(Selector selector, ServerSocketChannel ss) {
            this.selector = selector;
            this.ss = ss;
        }

        @Override
        public void run() {
            try {
                SocketChannel c = ss.accept();
                if (c != null) {
                    new Handler(this.selector, c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Handler implements Runnable {

        final Selector selector;
        final SocketChannel sc;
        final SelectionKey sk;


        ByteBuffer input = ByteBuffer.allocate(MAX_INPUT);
        ByteBuffer output = ByteBuffer.allocate(MAX_OUTPUT);
        final static int READING = 0, SENDING = 1;
        int state = READING;

        Handler(Selector selector, SocketChannel sc) throws IOException {
            this.selector = selector;
            this.sc = sc;
            this.sc.configureBlocking(false);
            this.sk = this.sc.register(this.selector, 0);
            this.sk.attach(this);
            this.sk.interestOps(SelectionKey.OP_READ);
            this.selector.wakeup();
        }

        @Override
        public void run() {
            try {
                if (state == READING) read();
                if (state == SENDING) send();
            } catch (Exception ex) {

            }
        }

        private void read() throws IOException {
            this.sc.read(input);
            if (inputIsComplete()) {
                process();
                state = SENDING;
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }


        private void send() throws IOException {
            this.sc.write(output);
            if (outputIsComplete()) {
                sk.cancel();
            }
        }

        private void process() {

        }

        private boolean inputIsComplete() {
            return false;
        }

        private boolean outputIsComplete() {
            return false;
        }
    }
}
