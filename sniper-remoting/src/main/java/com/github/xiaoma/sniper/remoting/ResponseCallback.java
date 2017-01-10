package com.github.xiaoma.sniper.remoting;

/**
 * Created by machunxiao on 2017/1/10.
 */
public interface ResponseCallback {

    void done(Object response);

    void caught(Throwable exception);

}
