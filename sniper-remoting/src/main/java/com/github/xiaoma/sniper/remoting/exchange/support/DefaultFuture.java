package com.github.xiaoma.sniper.remoting.exchange.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.remoting.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by machunxiao on 17/1/19.
 */
public class DefaultFuture implements ResponseFuture {

    private final Channel               channel;
    private final Object                request;
    private final int                   timeout;

    private final Lock                  lock = new ReentrantLock();
    private final Condition             done = lock.newCondition();

    private volatile Response           response;
    private volatile ResponseCallback   callback;

    public DefaultFuture(Channel channel, Object request, int timeout) {
        this.channel = channel;
        this.request = request;
        this.timeout = timeout;
    }

    @Override
    public Object get() throws RemotingException {
        return get(timeout);
    }

    @Override
    public Object get(int timeout) throws RemotingException {
        if (timeout <= 0) {
            timeout = Constants.DEFAULT_TIMEOUT;
        }
        if (!isDone()) {
            long start = System.currentTimeMillis();
            lock.lock();
            try {
                while (!isDone()) {
                    done.await(timeout, TimeUnit.MILLISECONDS);
                    if (isDone() || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            if (!isDone()) {
                throw new TimeoutException(channel, "");
            }
        }

        return returnFromResponse();
    }

    @Override
    public void setCallback(ResponseCallback callback) {

    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    public void cancel() {

    }

    private Object returnFromResponse() {
        if (response != null) {
            return response.getValue();
        }
        return null;
    }
}
