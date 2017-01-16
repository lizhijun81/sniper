package com.github.xiaoma.sniper.core.utils;

import java.net.InetSocketAddress;

/**
 * Created by machunxiao on 17/1/16.
 */
public class NetUtils {

    public static String addressToString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

}
