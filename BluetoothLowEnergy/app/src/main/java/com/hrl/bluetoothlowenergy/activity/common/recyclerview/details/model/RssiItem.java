package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class RssiItem implements RecyclerViewItem {

    private final BluetoothLeDevice mDevice;

    public RssiItem(BluetoothLeDevice device) {
        mDevice = device;
    }

    public int getRssi() {
        return mDevice.getRssi();
    }

    public double getRunningAverageRssi() {
        return mDevice.getRunningAverageRssi();
    }

    public int getFirstRssi() {
        return mDevice.getFirstRssi();
    }

    public long getFirstTimestamp() {
        return mDevice.getFirstTimestamp();
    }

    public long getTimestamp() {
        return mDevice.getTimestamp();
    }
}
