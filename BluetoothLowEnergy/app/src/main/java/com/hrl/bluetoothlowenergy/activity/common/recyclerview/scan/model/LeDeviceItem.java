package com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.model;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class LeDeviceItem implements RecyclerViewItem {

    private final BluetoothLeDevice device;

    public LeDeviceItem(final BluetoothLeDevice device) {
        this.device = device;
    }

    public BluetoothLeDevice getDevice() {
        return device;
    }
}
