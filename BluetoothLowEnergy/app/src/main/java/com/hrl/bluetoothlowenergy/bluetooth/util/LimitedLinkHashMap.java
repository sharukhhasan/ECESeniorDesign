package com.hrl.bluetoothlowenergy.bluetooth.util;

import java.util.LinkedHashMap;

/**
 * Created by Sharukh Hasan on 3/23/17.
 *
 */
public class LimitedLinkHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -5375660288461724925L;

    private final int mMaxSize;

    public LimitedLinkHashMap(final int maxSize) {
        super(maxSize + 1, 1, false);
        mMaxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(final Entry<K, V> eldest) {
        return this.size() > mMaxSize;
    }
}
