package com.github.xiaoma.rpc.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by chunxiao on 2016/4/30.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 9031353861505771002L;
    private long requestId;
    private Object result;
    private int resultCode;
    private Throwable error;
}
