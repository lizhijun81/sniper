package com.github.xiaoma.sniper.remoting.netty;

import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.transport.support.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Created by machunxiao on 16/12/26.
 */
public class NettyServer extends AbstractServer {

    private ServerBootstrap serverBootstrap;
    private final URL url;
    private final ChannelListener listener;

    public NettyServer(URL url, ChannelListener listener) throws RemotingException {
        super(url, listener);
        this.url = url;
        this.listener = listener;
    }

    @Override
    protected void doOpen() throws Throwable {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ChannelListener listener = NettyServer.this;
                            NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), url, listener);
                            ch.pipeline()
                                    .addLast("encoder", adapter.getEncoder())
                                    .addLast("handler", new NettyHandler(url, listener))
                                    .addLast("decoder", adapter.getDecoder());
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);

            ChannelFuture f = b.bind(getBindAddress()).sync();

            f.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }

    @Override
    protected void doClose() throws Throwable {

    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public Collection<Channel> getChannels() {
        return null;
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        return null;
    }
}
