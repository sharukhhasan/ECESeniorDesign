package com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.model;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.beacon.ibeacon.IBeaconDevice;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class IBeaconItem implements RecyclerViewItem {

    private final IBeaconDevice device;

    public IBeaconItem(final IBeaconDevice device) {
        this.device = device;
    }

    public IBeaconDevice getDevice() {
        return device;
    }

}
