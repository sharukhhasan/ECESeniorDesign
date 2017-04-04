package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder;

import android.content.Context;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.AdRecordHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.AdRecordItem;
import com.hrl.bluetoothlowenergy.bluetooth.util.ByteUtils;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class AdRecordBinder extends BaseViewBinder<AdRecordItem> {

    public AdRecordBinder(Context context) {
        super(context);
    }

    @Override
    public void bind(BaseViewHolder<AdRecordItem> holder, AdRecordItem item) {
        final AdRecordHolder actualHolder = (AdRecordHolder) holder;

        actualHolder.getTitleTextView().setText(item.getTitle());

        actualHolder.getStringTextView().setText(getContext().getString(R.string.formatter_single_quoted_string, item.getDataAsString()));

        actualHolder.getArrayTextView().setText(getContext().getString(R.string.formatter_single_quoted_string, ByteUtils.byteArrayToHexString(item.getData())));
    }

    @Override
    public boolean canBind(RecyclerViewItem item) {
        return item instanceof AdRecordItem;
    }
}
