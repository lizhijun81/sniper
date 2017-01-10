package com.github.xiaoma.sniper.remoting;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by machunxiao on 2017/1/10.
 */
public class Request {

    private static final AtomicLong invoke_id = new AtomicLong(0);


    private final long reqId;
    private String version;
    private Object data;

    public Request() {
        this.reqId = invoke_id.getAndIncrement();
    }

    public Request(long reqId) {
        this.reqId = reqId;
    }

    public long getReqId() {
        return reqId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "reqId=" + reqId +
                ", version='" + version + '\'' +
                ", data=" + data +
                '}';
    }
}
