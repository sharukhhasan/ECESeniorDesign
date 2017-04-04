package com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.binder;

import android.content.Context;
import android.view.View;

import com.hrl.bluetoothlowenergy.activity.common.Navigation;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.holder.LeDeviceHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.model.LeDeviceItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class LeDeviceBinder extends BaseViewBinder<LeDeviceItem> {

    private final Navigation navigation;

    public LeDeviceBinder(Context context, Navigation navigation) {
        super(context);
        this.navigation = navigation;
    }

    @Override
    public void bind(BaseViewHolder<LeDeviceItem> holder, LeDeviceItem item) {

        final LeDeviceHolder actualHolder = (LeDeviceHolder) holder;
        final BluetoothLeDevice device = item.getDevice();

        CommonBinding.bind(getContext(), actualHolder, device);
        actualHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation.openDetailsActivity(device);
            }
        });
    }

    @Override
    public boolean canBind(RecyclerViewItem item) {
        return item instanceof LeDeviceItem;
    }
}
