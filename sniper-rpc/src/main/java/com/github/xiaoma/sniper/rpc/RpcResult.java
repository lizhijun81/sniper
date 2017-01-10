package com.github.xiaoma.sniper.rpc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by machunxiao on 17/1/10.
 */
public class RpcResult implements Result, Serializable {

    private static final long serialVersionUID = 2006104822464689220L;

    private Object              result;

    private Throwable           exception;

    private Map<String, String> attachments = new HashMap<String, String>();

    public RpcResult() {
    }

    public RpcResult(Object result) {
        this.result = result;
    }

    public RpcResult(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public Object getValue() {
        return result;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public boolean hasException() {
        return exception != null;
    }

    @Override
    public Object recreate() throws Throwable {
        if (exception != null) {
            throw exception;
        }
        return result;
    }

    @Override
    public Map<String, String> getAttachments() {
        return attachments;
    }

    @Override
    public String getAttachment(String key) {
        return attachments.get(key);
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        String value = attachments.get(key);
        return value == null || value.trim().length() == 0 ? defaultValue : value;
    }

    @Override
    public String toString() {
        return "RpcResult{" +
                "result=" + result +
                ", exception=" + exception +
                ", attachments=" + attachments +
                '}';
    }

    public void setAttachment(String key, String value) {
        attachments.put(key, value);
    }
}
