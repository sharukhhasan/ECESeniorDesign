package com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.holder;

import android.view.View;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.model.IBeaconItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class IBeaconHolder extends BaseViewHolder<IBeaconItem> implements CommonDeviceHolder {

    private final TextView ibeaconUUID;
    private final TextView ibeaconMajor;
    private final TextView ibeaconMinor;
    private final TextView ibeaconTxPower;
    private final TextView ibeaconDistance;
    private final TextView ibeaconDistanceDescriptor;

    private final TextView deviceName;
    private final TextView deviceAddress;
    private final TextView deviceRssi;
    private final TextView deviceLastUpdated;

    public IBeaconHolder(View itemView) {
        super(itemView);

        this.deviceAddress = (TextView) itemView.findViewById(R.id.device_address);
        this.deviceName = (TextView) itemView.findViewById(R.id.device_name);
        this.deviceRssi = (TextView) itemView.findViewById(R.id.device_rssi);
        this.deviceLastUpdated = (TextView) itemView.findViewById(R.id.device_last_update);

        this.ibeaconMajor = (TextView) itemView.findViewById(R.id.ibeacon_major);
        this.ibeaconMinor = (TextView) itemView.findViewById(R.id.ibeacon_minor);
        this.ibeaconDistance = (TextView) itemView.findViewById(R.id.ibeacon_distance);
        this.ibeaconUUID = (TextView) itemView.findViewById(R.id.ibeacon_uuid);
        this.ibeaconTxPower = (TextView) itemView.findViewById(R.id.ibeacon_tx_power);
        this.ibeaconDistanceDescriptor = (TextView) itemView.findViewById(R.id.ibeacon_distance_descriptor);
    }

    @Override
    public TextView getDeviceName() {
        return deviceName;
    }

    @Override
    public TextView getDeviceAddress() {
        return deviceAddress;
    }

    @Override
    public TextView getDeviceRssi() {
        return deviceRssi;
    }

    @Override
    public TextView getDeviceLastUpdated() {
        return deviceLastUpdated;
    }

    public TextView getIbeaconUUID() {
        return ibeaconUUID;
    }

    public TextView getIbeaconMajor() {
        return ibeaconMajor;
    }

    public TextView getIbeaconMinor() {
        return ibeaconMinor;
    }

    public TextView getIbeaconTxPower() {
        return ibeaconTxPower;
    }

    public TextView getIbeaconDistance() {
        return ibeaconDistance;
    }

    public TextView getIbeaconDistanceDescriptor() {
        return ibeaconDistanceDescriptor;
    }
}
