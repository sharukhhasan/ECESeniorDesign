package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder;

import android.view.View;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.RssiItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class RssiInfoHolder extends BaseViewHolder<RssiItem> {

    private final TextView mTvFirstTimestamp;
    private final TextView mTvFirstRssi;
    private final TextView mTvLastTimestamp;
    private final TextView mTvLastRssi;
    private final TextView mTvRunningAverageRssi;

    public RssiInfoHolder(View itemView) {
        super(itemView);

        mTvFirstTimestamp = (TextView) itemView.findViewById(R.id.firstTimestamp);
        mTvFirstRssi = (TextView) itemView.findViewById(R.id.firstRssi);
        mTvLastTimestamp = (TextView) itemView.findViewById(R.id.lastTimestamp);
        mTvLastRssi = (TextView) itemView.findViewById(R.id.lastRssi);
        mTvRunningAverageRssi = (TextView) itemView.findViewById(R.id.runningAverageRssi);
    }

    public TextView getFirstTimestamp() {
        return mTvFirstTimestamp;
    }

    public TextView getFirstRssi() {
        return mTvFirstRssi;
    }

    public TextView getLastTimestamp() {
        return mTvLastTimestamp;
    }

    public TextView getLastRssi() {
        return mTvLastRssi;
    }

    public TextView getRunningAverageRssi() {
        return mTvRunningAverageRssi;
    }
}
