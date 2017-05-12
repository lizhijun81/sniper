package com.github.xiaoma.sniper.remoting.nio.reactor;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by machunxiao on 2017/5/12.
 */
public class IChannel {

    private final SocketChannel channel;
    private final EventHandler handler;

    private Processor processor;
    private SelectionKey selectionKey;

    public IChannel(SocketChannel channel, EventHandler handler) {
        this.channel = channel;
        this.handler = handler;
    }


    public SocketChannel getChannel() {
        return channel;
    }

    public EventHandler getHandler() {
        return handler;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public void register(Selector selector, int selectionKey, Object attachment) throws ClosedChannelException {
        SelectionKey key = channel.register(selector, selectionKey, attachment);
        setSelectionKey(key);
    }
}
