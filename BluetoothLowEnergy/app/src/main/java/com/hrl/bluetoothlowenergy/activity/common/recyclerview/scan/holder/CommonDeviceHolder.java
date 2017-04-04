package com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.holder;

import android.widget.TextView;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public interface CommonDeviceHolder {
    TextView getDeviceName();

    TextView getDeviceAddress();

    TextView getDeviceRssi();

    TextView getDeviceLastUpdated();
}
