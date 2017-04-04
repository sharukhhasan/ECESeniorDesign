package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder;

import android.content.Context;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.HeaderHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.HeaderItem;


/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class HeaderBinder extends BaseViewBinder<HeaderItem> {

    public HeaderBinder(Context context) {
        super(context);
    }

    @Override
    public void bind(BaseViewHolder<HeaderItem> holder, HeaderItem item) {
        final HeaderHolder actualHolder = (HeaderHolder) holder;
        actualHolder.getTextView().setText(item.getText());
    }

    @Override
    public boolean canBind(RecyclerViewItem item) {
        return item instanceof HeaderItem;
    }
}
