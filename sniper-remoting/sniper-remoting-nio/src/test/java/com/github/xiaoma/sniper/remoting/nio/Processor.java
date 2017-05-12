package com.github.xiaoma.sniper.remoting.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class was Deprecated
 * Please use {@link com.github.xiaoma.sniper.remoting.nio.reactor.Processor}
 * <p>
 * Created by machunxiao on 2017/3/21.
 */
@Deprecated
public class Processor {

    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    private static final AtomicLong read_idx = new AtomicLong(1);
    private static final AtomicLong write_idx = new AtomicLong(1);
    private static final int n_cpu = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService service = Executors.newFixedThreadPool(n_cpu * 2);

    private Selector selector;

    public Processor() throws IOException {
        selector = SelectorProvider.provider().openSelector();
        start();
    }

    public void addChannel(SocketChannel channel) throws IOException {
        channel.register(selector, SelectionKey.OP_READ);
        selector.wakeup();
    }

    public void start() {
        service.submit(() -> {
            while (true) {
                if (selector.select(1000L) == 0) {
                    continue;
                }
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    if (key.isValid() && key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String req = new String(buffer.array(), 0, buffer.limit());
                        String resp = "I'm response:" + Math.random();
                        key.attach(resp);
                        key.interestOps(SelectionKey.OP_WRITE);
                        key.selector().wakeup();
                        logger.info("server process idx:{},read :{}.", read_idx.getAndIncrement(), req);
                    } else if (key.isValid() && key.isWritable()) {
                        String resp = (String) key.attachment();
                        SocketChannel sc = (SocketChannel) key.channel();
                        sc.write(ByteBuffer.wrap(resp.getBytes()));
                        logger.info("server process idx:{},write:{}.", write_idx.getAndIncrement(), resp);
                        key.cancel();   // 一次req-resp 已经完成，此处需要取消
                    }
                    it.remove();
                }
            }
        });
    }
}