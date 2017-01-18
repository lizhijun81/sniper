package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.Codec;

/**
 * Created by machunxiao on 2017/1/18.
 */
final class NettyCodecAdapter {

    private final Codec codec;
    private final URL url;
    private final ChannelListener listener;

    NettyCodecAdapter(Codec codec, URL url, ChannelListener listener) {
        this.codec = codec;
        this.url = url;
        this.listener = listener;
    }

    public NettyEncoder getEncoder() {
        return new NettyEncoder(codec, url, null);
    }

    public NettyDecoder getDecoder() {
        return new NettyDecoder(codec, url, null);
    }
}
