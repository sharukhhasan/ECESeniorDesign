package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder;

import android.view.View;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.model.IBeaconItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class IBeaconHolder extends BaseViewHolder<IBeaconItem> {

    private final TextView mCompanyId;
    private final TextView mAdvert;
    private final TextView mUuid;
    private final TextView mMajor;
    private final TextView mMinor;
    private final TextView mTxPower;

    public IBeaconHolder(View itemView) {
        super(itemView);

        mCompanyId = (TextView) itemView.findViewById(R.id.companyId);
        mAdvert = (TextView) itemView.findViewById(R.id.advertisement);
        mUuid = (TextView) itemView.findViewById(R.id.uuid);
        mMajor = (TextView) itemView.findViewById(R.id.major);
        mMinor = (TextView) itemView.findViewById(R.id.minor);
        mTxPower = (TextView) itemView.findViewById(R.id.txpower);
    }

    public TextView getCompanyId() {
        return mCompanyId;
    }

    public TextView getAdvert() {
        return mAdvert;
    }

    public TextView getUuid() {
        return mUuid;
    }

    public TextView getMajor() {
        return mMajor;
    }

    public TextView getMinor() {
        return mMinor;
    }

    public TextView getTxPower() {
        return mTxPower;
    }
}
