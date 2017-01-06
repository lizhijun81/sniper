package com.github.xiaoma.sniper.core;

/**
 * Created by machunxiao on 17/1/6.
 */
public interface Node {

    URL getUrl();

    boolean isAvailable();

    void destroy();

}
