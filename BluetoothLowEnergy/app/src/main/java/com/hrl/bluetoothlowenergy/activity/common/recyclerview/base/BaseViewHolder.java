package com.hrl.bluetoothlowenergy.activity.common.recyclerview.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sharukh Hasan on 3/23/17.
 *
 */
public abstract class BaseViewHolder<T extends RecyclerViewItem> extends RecyclerView.ViewHolder {

    private final View mItemView;

    public BaseViewHolder(final View itemView) {
        super(itemView);
        mItemView = itemView;
    }

    public View getView() {
        return mItemView;
    }
}
