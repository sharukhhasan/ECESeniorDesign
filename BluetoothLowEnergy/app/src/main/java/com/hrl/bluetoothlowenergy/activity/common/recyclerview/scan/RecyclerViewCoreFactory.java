package com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan;

import android.content.Context;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.Navigation;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewBinderCore;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.binder.LeDeviceBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.holder.LeDeviceHolder;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class RecyclerViewCoreFactory {

    public static RecyclerViewBinderCore create(final Context context, final Navigation navigation) {
        final RecyclerViewBinderCore core = new RecyclerViewBinderCore();

        // core.add(new IBeaconBinder(context, navigation), IBeaconHolder.class, R.layout.list_item_device_ibeacon);
        core.add(new LeDeviceBinder(context, navigation), LeDeviceHolder.class, R.layout.list_item_device_le);

        return core;
    }

}
