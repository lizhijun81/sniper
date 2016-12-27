package com.github.xiaoma.sniper.core;

import org.junit.Test;

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
 * Created by machunxiao on 16/12/27.
 */
public class NIOLearn {

    static class Reactor implements Runnable {

        final Selector selector;
        final ServerSocketChannel ssc;

        Reactor(int port) throws IOException {
            this.selector = Selector.open();
            this.ssc = ServerSocketChannel.open();
            this.ssc.socket().bind(new InetSocketAddress(port));
            this.ssc.configureBlocking(false);

            SelectionKey sk = this.ssc.register(this.selector, SelectionKey.OP_ACCEPT);
            sk.attach(new Acceptor());
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        dispatch(key);
                    }
                    keys.clear();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void dispatch(SelectionKey key) {
            Runnable r = (Runnable) key.attachment();
            if (r != null) {
                r.run();
            }
        }

        class Acceptor implements Runnable {
            @Override
            public void run() {
                try {
                    SocketChannel sc = ssc.accept();
                    if (sc != null) {
                        new Handler(selector, sc);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    static class Handler implements Runnable {

        final Selector selector;
        final SocketChannel sc;
        final SelectionKey sk;

        final ByteBuffer input = ByteBuffer.allocate(1024);
        final ByteBuffer output = ByteBuffer.allocate(1024);

        static final int READING = 0, SENDING = 1;
        int state = READING;

        Handler(Selector selector, SocketChannel sc) throws IOException {
            this.selector = selector;
            this.sc = sc;
            this.sc.configureBlocking(false);
            this.sk = this.sc.register(this.selector, SelectionKey.OP_READ);
            this.sk.attach(this);
            this.selector.wakeup();
        }

        @Override
        public void run() {
            try {
                if (state == READING) read();
                else if (state == SENDING) send();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void read() throws IOException {
            sc.read(input);
            if (inputIsComplete()) {
                process();
                state = SENDING;
                sc.register(selector, SelectionKey.OP_WRITE);
//                或者使用这种
//                sk.attach(new Sender());
//                sk.interestOps(SelectionKey.OP_WRITE);
//                sk.selector().wakeup();
            }
        }

        private void send() throws IOException {
            sc.write(output);
            if (outputIsComplete()) {
                sk.cancel();
            }
        }

        boolean inputIsComplete() { /* ... */
            return true;
        }

        boolean outputIsComplete() { /* ... */
            return true;
        }

        void process() { /* ... */ }

        class Sender implements Runnable {

            @Override
            public void run() {
                try {
                    sc.write(output);
                    if (outputIsComplete()) {
                        sk.cancel();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testServer() {
        try {
            Reactor reactor = new Reactor(9000);
            new Thread(reactor).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClient() {
        try {
            ByteBuffer write = ByteBuffer.allocate(1024);
            ByteBuffer read = ByteBuffer.allocate(1024);
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            client.connect(new InetSocketAddress(9000));
            Selector selector = Selector.open();
            client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isWritable()) {
                    write.clear();
                    write.put("hello world".getBytes());
                    client.write(write);
                } else if (key.isReadable()) {
                    client.read(read);
                    System.out.println(read);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
