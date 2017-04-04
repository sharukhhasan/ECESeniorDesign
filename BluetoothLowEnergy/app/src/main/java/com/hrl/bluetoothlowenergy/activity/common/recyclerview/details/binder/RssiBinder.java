package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder;

import android.content.Context;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.RssiInfoHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.RssiItem;
import com.hrl.bluetoothlowenergy.utils.TimeFormatter;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class RssiBinder extends BaseViewBinder<RssiItem> {

    public RssiBinder(Context context) {
        super(context);
    }

    private static String formatTime(final long time) {
        return TimeFormatter.getIsoDateTime(time);
    }

    @Override
    public void bind(BaseViewHolder<RssiItem> holder, RssiItem item) {
        final RssiInfoHolder actualHolder = (RssiInfoHolder) holder;

        actualHolder.getFirstTimestamp().setText(formatTime(item.getFirstTimestamp()));
        actualHolder.getFirstRssi().setText(formatRssi(item.getFirstRssi()));
        actualHolder.getLastTimestamp().setText(formatTime(item.getTimestamp()));
        actualHolder.getLastRssi().setText(formatRssi(item.getRssi()));
        actualHolder.getRunningAverageRssi().setText(formatRssi(item.getRunningAverageRssi()));
    }

    @Override
    public boolean canBind(RecyclerViewItem item) {
        return item instanceof RssiItem;
    }

    private String formatRssi(final double rssi) {
        return getContext().getString(R.string.formatter_db, String.valueOf(rssi));
    }

    private String formatRssi(final int rssi) {
        return getContext().getString(R.string.formatter_db, String.valueOf(rssi));
    }
}
