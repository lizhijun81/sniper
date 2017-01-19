package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.utils.NetUtils;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.ChannelListener;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by machunxiao on 17/1/19.
 */
public class NettyHandler extends ChannelHandlerAdapter implements ChannelOutboundHandler, ChannelInboundHandler {

    private static Logger logger = LoggerFactory.getLogger(NettyHandler.class);

    private final Map<String, Channel> channels = new ConcurrentHashMap<>(); // <ip:port, channel>

    private final URL url;
    private final ChannelListener listener;

    public NettyHandler(URL url, ChannelListener listener) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener == null");
        }
        this.url = url;
        this.listener = listener;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        NettyChannel channel = NettyChannel.getChannel(ctx.channel(), url, listener);
        if (channel != null) {
            channels.put(NetUtils.addressToString((InetSocketAddress) ctx.channel().remoteAddress()), channel);
        }
        listener.connected(channel);
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyChannel channel = NettyChannel.getChannel(ctx.channel(), url, listener);
        listener.received(channel, msg);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    // ChannelOutboundHandler

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
        NettyChannel channel = NettyChannel.getChannel(ctx.channel(), url, listener);
        listener.connected(channel);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
        NettyChannel channel = NettyChannel.getChannel(ctx.channel(), url, listener);
        listener.disconnected(channel);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.writeAndFlush(msg, promise);
        NettyChannel channel = NettyChannel.getChannel(ctx.channel(), url, listener);
        listener.sent(channel, msg);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        NettyChannel channel = NettyChannel.getChannel(ctx.channel(), url, listener);
        listener.caught(channel, cause);
    }
}
