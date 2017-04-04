package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details;

import android.content.Context;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewBinderCore;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder.AdRecordBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder.DeviceInfoBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder.HeaderBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder.RssiBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder.TextBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.AdRecordHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.DeviceInfoHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.HeaderHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.RssiInfoHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.TextHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder.IBeaconBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.IBeaconHolder;


public class RecyclerViewCoreFactory {

    public static RecyclerViewBinderCore create(final Context context) {
        final RecyclerViewBinderCore core = new RecyclerViewBinderCore();

        core.add(new TextBinder(context), TextHolder.class, R.layout.list_item_view_textview);
        core.add(new HeaderBinder(context), HeaderHolder.class, R.layout.list_item_view_header);
        core.add(new AdRecordBinder(context), AdRecordHolder.class, R.layout.list_item_view_adrecord);
        core.add(new RssiBinder(context), RssiInfoHolder.class, R.layout.list_item_view_rssi_info);
        core.add(new DeviceInfoBinder(context), DeviceInfoHolder.class, R.layout.list_item_view_device_info);
        core.add(new IBeaconBinder(context), IBeaconHolder.class, R.layout.list_item_view_ibeacon_details);

        return core;
    }

}
