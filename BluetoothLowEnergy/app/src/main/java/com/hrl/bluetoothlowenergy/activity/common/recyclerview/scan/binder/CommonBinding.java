package com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.binder;

import android.content.Context;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.holder.CommonDeviceHolder;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;
import com.hrl.bluetoothlowenergy.utils.Constants;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class CommonBinding {

    public static void bind(final Context context, final CommonDeviceHolder holder, final BluetoothLeDevice device) {

        final String deviceName = device.getName();
        final double rssi = device.getRssi();

        if (deviceName != null && deviceName.length() > 0) {
            holder.getDeviceName().setText(deviceName);
        } else {
            holder.getDeviceName().setText(R.string.unknown_device);
        }

        final String rssiString = context.getString(R.string.formatter_db, String.valueOf(rssi));
        final String runningAverageRssiString = context.getString(R.string.formatter_db, String.valueOf(device.getRunningAverageRssi()));

        holder.getDeviceLastUpdated().setText(android.text.format.DateFormat.format(Constants.TIME_FORMAT, new java.util.Date(device.getTimestamp())));
        holder.getDeviceAddress().setText(device.getAddress());
        holder.getDeviceRssi().setText(rssiString + " / " + runningAverageRssiString);
    }
}
