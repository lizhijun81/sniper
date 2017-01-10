package com.github.xiaoma.sniper.remoting;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by machunxiao on 2017/1/10.
 */
@Setter
@Getter
public class Response {

    private long reqId = 0;
    private String version = null;
    private Object result = null;
    private byte status = 10;

    public long getReqId() {
        return reqId;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
