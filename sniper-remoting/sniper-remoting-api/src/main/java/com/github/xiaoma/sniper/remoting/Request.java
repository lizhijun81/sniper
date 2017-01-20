package com.github.xiaoma.sniper.remoting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by machunxiao on 2017/1/10.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request implements Serializable {

    private static final long serialVersionUID = 1393951513915969562L;

    private long requestId;
    private byte event;     // 0:heartbeat,1:request
    private byte type;      // 0:async,1:sync,2:oneWay
    private Object data;    // invocation

}
