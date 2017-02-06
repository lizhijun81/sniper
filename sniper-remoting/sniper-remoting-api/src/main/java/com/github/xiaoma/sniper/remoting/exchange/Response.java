package com.github.xiaoma.sniper.remoting.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Created by machunxiao on 2017/1/10.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private long requestId;
    private Object value;
    private Throwable exception;
    private Map<String, String> attachments;
}
