package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder;

import android.view.View;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.DeviceInfoItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class DeviceInfoHolder extends BaseViewHolder<DeviceInfoItem> {

    private final TextView mName;
    private final TextView mAddress;
    private final TextView mClass;
    private final TextView mMajorClass;
    private final TextView mServices;
    private final TextView mBondingState;

    public DeviceInfoHolder(View itemView) {
        super(itemView);

        mName = (TextView) itemView.findViewById(R.id.deviceName);
        mAddress = (TextView) itemView.findViewById(R.id.deviceAddress);
        mClass = (TextView) itemView.findViewById(R.id.deviceClass);
        mMajorClass = (TextView) itemView.findViewById(R.id.deviceMajorClass);
        mServices = (TextView) itemView.findViewById(R.id.deviceServiceList);
        mBondingState = (TextView) itemView.findViewById(R.id.deviceBondingState);
    }

    public TextView getName() {
        return mName;
    }

    public TextView getAddress() {
        return mAddress;
    }

    public TextView getDeviceClass() {
        return mClass;
    }

    public TextView getMajorClass() {
        return mMajorClass;
    }

    public TextView getServices() {
        return mServices;
    }

    public TextView getBondingState() {
        return mBondingState;
    }
}
