package com.github.xiaoma.sniper.remoting.nio.reactor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by machunxiao on 2017/5/12.
 */
public class ProcessorPool {

    private static final AtomicInteger lbIndex = new AtomicInteger();
    private final int poolSize;
    private final Processor[] pool;

    public ProcessorPool(int poolSize, EventDispatcher dispatcher, EventHandler handler) {
        this.poolSize = poolSize;
        pool = new Processor[this.poolSize];
        for (int i = 0; i < this.poolSize; i++) {
            pool[i] = new Processor(dispatcher, handler);
        }
    }

    public Processor getNextProcessor() {
        int index = lbIndex.incrementAndGet();
        if (lbIndex.get() == Integer.MAX_VALUE) {
            lbIndex.set(0);
        }
        return pool[index % poolSize];
    }

}
