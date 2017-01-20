package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.Resettable;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.ExtensionLoader;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.Codec;

/**
 * Created by machunxiao on 2017/1/18.
 */
public abstract class AbstractEndpoint extends AbstractPeer implements Resettable {

    private final Codec codec;
    private int timeout;
    private int connectTimeout;

    public AbstractEndpoint(URL url, ChannelListener listener) {
        super(url, listener);
        this.codec = getChannelCodec(url);
        this.timeout = url.getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
        this.connectTimeout = url.getPositiveParameter(Constants.CONNECT_TIMEOUT_KEY, Constants.DEFAULT_CONNECT_TIMEOUT);
    }

    @Override
    public void reset(URL url) {

    }

    protected Codec getChannelCodec(URL url) {
        String codecName = url.getParameter(Constants.CODEC_KEY, Constants.CODEC_DEFAULT_KEY);
        return ExtensionLoader.getExtensionLoader(Codec.class).getExtension(codecName);
    }

    protected Codec getCodec() {
        return codec;
    }

    protected int getTimeout() {
        return timeout;
    }

    protected int getConnectTimeout() {
        return connectTimeout;
    }
}
