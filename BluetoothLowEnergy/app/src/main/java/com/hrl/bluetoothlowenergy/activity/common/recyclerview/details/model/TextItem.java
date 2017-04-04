package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class TextItem implements RecyclerViewItem {
    private final CharSequence mText;

    public TextItem(CharSequence text) {
        mText = text;
    }

    public CharSequence getText() {
        return mText;
    }
}
