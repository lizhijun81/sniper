package com.github.xiaoma.sniper.remoting.nio.reactor;

/**
 * Created by machunxiao on 2017/5/11.
 */
public class Event {

    private final EventType type;
    private final EventHandler handler;

    public Event(EventType type, EventHandler handler) {
        this.type = type;
        this.handler = handler;
    }

    public void fire() {
        switch (type) {
            case OPEN:
                handler.onOpen();
                break;
            case CONNECT:
                handler.onConnect();
                break;
            case READ:
                handler.onRead();
                break;
            case WRITE:
                handler.onWrite();
                break;
            case FLUSH:
                handler.onFlush();
                break;
            case CLOSE:
                handler.onClose();
                break;
            case THROW:
                handler.onThrow();
                break;
            default:
                throw new IllegalArgumentException("Unsupported event type:" + type);
        }
    }
}
