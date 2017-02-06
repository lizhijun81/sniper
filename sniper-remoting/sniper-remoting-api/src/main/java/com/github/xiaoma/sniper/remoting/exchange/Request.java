package com.github.xiaoma.sniper.remoting.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by machunxiao on 2017/1/10.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    private long requestId;
    private byte event;     // 0:heartbeat,1:request
    private Object data;    // invocation
    private String version; // version

}
