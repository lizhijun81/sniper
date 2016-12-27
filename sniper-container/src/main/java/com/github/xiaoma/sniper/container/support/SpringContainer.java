package com.github.xiaoma.sniper.container.support;

import com.github.xiaoma.sniper.container.Container;

/**
 * Created by machunxiao on 16/12/26.
 */
public class SpringContainer implements Container {

    @Override
    public void start() {
        System.out.println("spring start");
    }

    @Override
    public void stop() {
        System.out.println("spring stop");
    }
}
