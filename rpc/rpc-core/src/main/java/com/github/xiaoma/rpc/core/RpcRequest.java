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
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 8622539377394841306L;
    private long requestId;
    private String serviceClass; // http://192.168.1.1:80/xxxServer/xxxService-1.0.0.RELEASE
    private String methodName;
    private Object[] args;

    private String clientIp;
    private String clientAppName;
    private long requestTime;

}
