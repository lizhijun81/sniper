package com.github.xiaoma.sniper.remoting.transport;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.ExtensionLoader;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.Client;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.Server;

/**
 * Created by machunxiao on 17/1/10.
 */
public enum Transporters {
    ;

    public static Server bind(URL url, ChannelListener listener) throws RemotingException {
        return getTransporter().bind(url, listener);
    }

    public static Client connect(URL url, ChannelListener listener) throws RemotingException {
        return getTransporter().connect(url, listener);
    }

    public static Transporter getTransporter() {
        return ExtensionLoader.getExtensionLoader(Transporter.class).getAdaptiveExtension();
    }
}
