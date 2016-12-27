package com.github.xiaoma.sniper.container;

import com.github.xiaoma.sniper.core.extension.SPI;

/**
 * Created by machunxiao on 16/12/26.
 */
@SPI("spring")
public interface Container {

    void start();

    void stop();
}
