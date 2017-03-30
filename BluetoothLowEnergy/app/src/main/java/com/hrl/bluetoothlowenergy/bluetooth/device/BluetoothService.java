package com.hrl.bluetoothlowenergy.bluetooth.device;

import android.bluetooth.BluetoothClass;

/**
 * Created by Sharukh Hasan on 3/23/17.
 *
 */
public enum BluetoothService {
    AUDIO(BluetoothClass.Service.AUDIO),
    CAPTURE(BluetoothClass.Service.CAPTURE),
    INFORMATION(BluetoothClass.Service.INFORMATION),
    LIMITED_DISCOVERABILITY(BluetoothClass.Service.LIMITED_DISCOVERABILITY),
    NETWORKING(BluetoothClass.Service.NETWORKING),
    OBJECT_TRANSFER(BluetoothClass.Service.OBJECT_TRANSFER),
    POSITIONING(BluetoothClass.Service.POSITIONING),
    RENDER(BluetoothClass.Service.RENDER),
    TELEPHONY(BluetoothClass.Service.TELEPHONY);

    private final int mAndroidConstant;

    BluetoothService(final int androidCode){
        mAndroidConstant = androidCode;
    }

    public int getAndroidConstant(){
        return mAndroidConstant;
    }
}
