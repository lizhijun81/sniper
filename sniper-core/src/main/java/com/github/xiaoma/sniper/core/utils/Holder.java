package com.github.xiaoma.sniper.core.utils;

/**
 * Created by machunxiao on 2016/12/28.
 */
public class Holder<T> {
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
