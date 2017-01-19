package com.github.xiaoma.sniper.core.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by machunxiao on 17/1/16.
 */
public enum NetUtils {
    ;

    public static String addressToString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

    public static String getIpByHost(String host) {
        try {
            return InetAddress.getByName(host).getHostAddress();
        } catch (UnknownHostException e) {
            return host;
        }
    }

    public static String filterLocalHost(String host) {
        return host;
    }
}
