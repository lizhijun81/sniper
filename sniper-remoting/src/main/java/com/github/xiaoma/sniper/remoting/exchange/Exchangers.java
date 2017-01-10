package com.github.xiaoma.sniper.remoting.exchange;

import com.github.xiaoma.sniper.core.Constants;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.extension.ExtensionLoader;
import com.github.xiaoma.sniper.remoting.RemotingException;

/**
 * Created by machunxiao on 17/1/10.
 */
public enum Exchangers {
    ;

    public static ExchangeClient connect(URL url, ExchangeHandler handler) throws RemotingException {
        return getExchanger(url).connect(url, handler);
    }

    public static ExchangeServer bind(URL url, ExchangeHandler handler) throws RemotingException {
        return getExchanger(url).bind(url, handler);
    }

    public static Exchanger getExchanger(URL url) {
        String type = url.getParameter(Constants.EXCHANGER_KEY, Constants.DEFAULT_EXCHANGER);
        return getExchanger(type);
    }

    public static Exchanger getExchanger(String type) {
        return ExtensionLoader.getExtensionLoader(Exchanger.class).getExtension(type);
    }

}
