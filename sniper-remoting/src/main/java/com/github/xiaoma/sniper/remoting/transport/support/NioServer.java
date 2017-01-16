package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.utils.NetUtils;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.core.URL;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by machunxiao on 16/12/26.
 */
public class NioServer extends AbstractServer {

    private static final SelectorProvider provider = SelectorProvider.provider();

    private final URL url;
    private final ChannelListener listener;

    private ServerSocketChannel server;
    private Selector selector;
    private InetSocketAddress bindAddress;

    private boolean isBound;

    private Map<String, Channel> channels;


    public NioServer(URL url, ChannelListener listener) throws RemotingException {
        super(url, listener);
        this.url = url;
        this.listener = listener;
        this.channels = new HashMap<>();
    }

    @Override
    protected void doOpen() throws Throwable {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        selector = provider.openSelector();
        server.register(selector, SelectionKey.OP_ACCEPT);
        bindAddress = new InetSocketAddress(url.getHost(), url.getPort());
        server.bind(this.bindAddress);
        isBound = true;

    }

    @Override
    protected void doClose() throws Throwable {
        selector.close();
        server.close();
    }

    @Override
    public boolean isBound() {
        return isBound;
    }

    @Override
    public Collection<Channel> getChannels() {
        return null;
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        return channels.get(NetUtils.addressToString(remoteAddress));
    }
}
