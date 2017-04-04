package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothService;

import java.util.Set;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class DeviceInfoItem implements RecyclerViewItem {

    private final BluetoothLeDevice mDevice;

    public DeviceInfoItem(BluetoothLeDevice device) {
        mDevice = device;
    }

    public Set<BluetoothService> getBluetoothDeviceKnownSupportedServices() {
        return mDevice.getBluetoothDeviceKnownSupportedServices();
    }

    public String getBluetoothDeviceBondState() {
        return mDevice.getBluetoothDeviceBondState();
    }

    public String getBluetoothDeviceMajorClassName() {
        return mDevice.getBluetoothDeviceMajorClassName();
    }

    public String getBluetoothDeviceClassName() {
        return mDevice.getBluetoothDeviceClassName();
    }

    public String getAddress() {
        return mDevice.getAddress();
    }

    public String getName() {
        return mDevice.getName();
    }
}
