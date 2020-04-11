package com.dx168.patchserver.core.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by tong on 16/10/31.
 */
public class CacheEntry<T> {
    private T entry;
    private long expireMillis;

    public CacheEntry(T entry, TimeUnit timeUnit, long duration) {
        this.entry = entry;
        expireMillis = System.currentTimeMillis() + timeUnit.toMillis(duration);
    }

    public T getEntry() {
        return getEntry(false);
    }

    public T getEntry(boolean ignoreExpire) {
        if (!ignoreExpire && System.currentTimeMillis() - expireMillis > 0) {
            return null;
        }
        return entry;
    }
}
