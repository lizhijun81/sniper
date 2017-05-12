package com.github.xiaoma.sniper.remoting.nio.reactor;

/**
 * Created by machunxiao on 2017/5/12.
 */
public class EventDispatcher {


    public void dispatch(Event event) {
        preDispatch(event);
        try {
            event.fire();
        } finally {
            afterDispatch(event);
        }
    }

    private void preDispatch(Event event) {

    }

    private void afterDispatch(Event event) {

    }
}
