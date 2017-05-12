package com.github.xiaoma.sniper.remoting.nio.reactor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by machunxiao on 2017/5/12.
 */
public final class ExecuteContext {

    private static final AtomicLong idx = new AtomicLong();

    private final long contextId;
    private final long start;

    private long finish;

    private Object request;
    private Object response;
    private Throwable exception;

    private IChannel channel;
    private Processor processor;

    public ExecuteContext() {
        this.contextId = idx.incrementAndGet();
        this.start = System.currentTimeMillis();
    }

    public long getContextId() {
        return contextId;
    }

    public long getStart() {
        return start;
    }

    public long getFinish() {
        return finish;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public IChannel getChannel() {
        return channel;
    }

    public void setChannel(IChannel channel) {
        this.channel = channel;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public String toString() {
        return "ExecuteContext{" +
                "contextId=" + contextId +
                ", start=" + start +
                ", finish=" + finish +
                ", request=" + request +
                ", response=" + response +
                ", exception=" + exception +
                ", channel=" + channel +
                ", processor=" + processor +
                '}';
    }
}
