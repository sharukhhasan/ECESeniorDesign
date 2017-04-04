package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder;

import android.content.Context;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.TextHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.TextItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class TextBinder extends BaseViewBinder<TextItem> {

    public TextBinder(Context context) {
        super(context);
    }

    @Override
    public void bind(BaseViewHolder<TextItem> holder, TextItem item) {
        final TextHolder actualHolder = (TextHolder) holder;
        actualHolder.getTextView().setText(item.getText());
    }

    @Override
    public boolean canBind(RecyclerViewItem item) {
        return item instanceof TextItem;
    }
}
