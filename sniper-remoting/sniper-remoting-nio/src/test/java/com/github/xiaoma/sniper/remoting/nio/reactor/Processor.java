package com.github.xiaoma.sniper.remoting.nio.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by machunxiao on 2017/5/11.
 */
public class Processor {

    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    private static final int SELECT_TIMEOUT = 500;

    private final Queue<ExecuteContext> newContexts = new ConcurrentLinkedQueue<>();

    private final AtomicReference<ProcessTask> pt = new AtomicReference<>();
    private final String rwTaskName = "rw-task";
    private final AtomicBoolean wakeupCalled = new AtomicBoolean(false);

    private final ByteBuffer input = ByteBuffer.allocate(1024);
    private final ByteBuffer output = ByteBuffer.allocate(1024);


    private final EventDispatcher dispatcher;
    private final EventHandler handler;


    private volatile Selector selector;
    private volatile boolean shutdown = false;

    public Processor(EventDispatcher dispatcher, EventHandler handler) {
        this.dispatcher = dispatcher;
        this.handler = handler;
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException("unable to start a processor", e);
        }
    }

    public void newContext(ExecuteContext ctx) {
        newContexts.add(ctx);
        startup();
        wakeup();
    }


    private void startup() {
        ProcessTask task = pt.get();
        if (task == null) {
            task = new ProcessTask();
            if (pt.compareAndSet(null, task)) {
                new Thread(task, rwTaskName).start();
            }
        }
    }

    private void wakeup() {
        wakeupCalled.getAndSet(true);
        selector.wakeup();
    }

    private final class ProcessTask implements Runnable {

        @Override
        public void run() {
            while (!shutdown) {
                try {
                    register();

                    int select = select();

                    flush();

                    if (select > 0) {
                        process();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void register() throws IOException {
        for (ExecuteContext ctx = newContexts.poll(); ctx != null; ctx = newContexts.poll()) {
            IChannel channel = ctx.getChannel();

            channel.register(selector, SelectionKey.OP_READ, ctx);

            // add to timing-wheel to schedule

            fireOpen(ctx);
        }
    }

    private int select() throws IOException {
        long t1 = System.currentTimeMillis();
        int selected = selector.select(SELECT_TIMEOUT);
        long t2 = System.currentTimeMillis();
        long delta = t2 - t1;
        if (selected == 0 && !wakeupCalled.get() && delta < 100) {
            if (isBrokenConnection()) {

            } else {
                registerNewSelector();
            }
            wakeupCalled.getAndSet(false);
        }
        return selected;
    }

    private boolean isBrokenConnection() throws IOException {
        boolean broken = false;

        synchronized (selector) {
            Set<SelectionKey> keys = selector.keys();
            for (SelectionKey key : keys) {
                SocketChannel channel = (SocketChannel) key.channel();
                if (!channel.isConnected()) {
                    key.cancel();
                    broken = true;
                }
            }
        }
        return broken;
    }

    private void registerNewSelector() throws IOException {
        synchronized (this) {
            Set<SelectionKey> keys = selector.keys();
            Selector newSelector = Selector.open();
            for (SelectionKey key : keys) {
                SelectableChannel channel = key.channel();

                channel.register(newSelector, key.interestOps(), key.attachment());

            }
            selector.close();
            selector = newSelector;
        }
    }

    private void flush() throws IOException {
        // TODO: 2017/5/13  add schedule task
    }

    private void process() throws IOException {
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();
            if (key.isValid() && key.isReadable()) {
                read(key);
            } else if (key.isValid() && key.isWritable()) {
                write(key);
            } else {
                key.cancel();
            }
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ExecuteContext ctx = (ExecuteContext) key.attachment();
        client.read(input);
        input.flip();
        String request = new String(input.array(), 0, input.position());
        ctx.setRequest(request);
        String response = "Hi,I'm " + Math.random();
        ctx.setResponse(response);
        input.clear();
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void write(SelectionKey key) throws IOException {
        ExecuteContext ctx = (ExecuteContext) key.attachment();
        String response = (String) ctx.getResponse();
        SocketChannel client = (SocketChannel) key.channel();
        output.put(response.getBytes());
        output.flip();
        client.write(output);
        output.clear();
        ctx.setFinish(System.currentTimeMillis());

        logger.info("process request[id:{}] success[cost:{} ms]", ctx.getContextId(), ctx.getFinish() - ctx.getStart());
        key.cancel();
    }


    private void fireOpen(ExecuteContext ctx) {
        dispatcher.dispatch(new Event(EventType.OPEN, handler));
    }

    private void fireConnect(ExecuteContext ctx) {
        dispatcher.dispatch(new Event(EventType.CONNECT, handler));
    }

    private void fireRead(ExecuteContext ctx) {
        dispatcher.dispatch(new Event(EventType.READ, handler));
    }

    private void fireWrite(ExecuteContext ctx) {
        dispatcher.dispatch(new Event(EventType.WRITE, handler));
    }

    private void fireFlush(ExecuteContext ctx) {
        dispatcher.dispatch(new Event(EventType.FLUSH, handler));
    }

    private void fireClose(ExecuteContext ctx) {
        dispatcher.dispatch(new Event(EventType.CLOSE, handler));
    }

    private void fireThrow(ExecuteContext ctx) {
        dispatcher.dispatch(new Event(EventType.THROW, handler));
    }
}
