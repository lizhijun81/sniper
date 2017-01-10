package com.github.xiaoma.sniper.core.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by machunxiao on 2017/1/10.
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    private final AtomicInteger THREAD_SEQ = new AtomicInteger(1);

    private final String prefix;
    private final boolean daemon;
    private final ThreadGroup group;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this.prefix = prefix;
        this.daemon = daemon;
        SecurityManager s = System.getSecurityManager();
        this.group = s == null ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }


    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + THREAD_SEQ.getAndIncrement();
        Thread t = new Thread(group, name);
        t.setDaemon(daemon);
        return t;
    }
}
