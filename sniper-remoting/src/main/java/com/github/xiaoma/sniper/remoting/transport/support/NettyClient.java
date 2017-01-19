package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * Created by machunxiao on 16/12/26.
 */
public class NettyClient extends AbstractClient {

    private final URL url;
    private final ChannelListener listener;

    private Bootstrap client;
    private volatile Channel channel;

    public NettyClient(URL url, ChannelListener listener) throws RemotingException {
        super(url, listener);
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected void doOpen() throws Throwable {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        client = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout())
                .handler(new ChannelInitializer<ServerChannel>() {
                    @Override
                    protected void initChannel(ServerChannel ch) throws Exception {
                        ChannelListener listener = NettyClient.this;
                        NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), url, listener);
                        ch.pipeline()
                                .addLast("encoder", adapter.getEncoder())
                                .addLast("handler", new NettyHandler(url, listener))
                                .addLast("decoder", adapter.getDecoder())
                        ;
                    }
                });
    }

    @Override
    protected void doClose() throws Throwable {

    }

    @Override
    protected void doConnect() throws Throwable {
        long start = System.currentTimeMillis();
        ChannelFuture future = client.connect(getConnectAddress());
        try {
            boolean ret = future.awaitUninterruptibly(1L, TimeUnit.MILLISECONDS);
            if (ret && future.isSuccess()) {
                Channel newChannel = future.channel();
                try {
                    Channel oldChannel = this.channel;
                    if (oldChannel != null) {
                        try {
                            oldChannel.close().syncUninterruptibly();
                        } finally {
                            NettyChannel.removeChannel(oldChannel);
                        }
                    }
                } finally {
                    this.channel = newChannel;
                }
            }
        } finally {
            if (!isConnected()) {
                future.cancel(true);
            }
        }
    }

    @Override
    protected void doDisconnect() throws Throwable {

    }

    @Override
    protected com.github.xiaoma.sniper.remoting.Channel getChannel() {
        Channel ch = channel;
        if (ch == null) {
            return null;
        }
        return NettyChannel.getChannel(ch, url, listener);
    }


}
