package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder;

import android.content.Context;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.DeviceInfoHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.DeviceInfoItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothService;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class DeviceInfoBinder extends BaseViewBinder<DeviceInfoItem> {

    public DeviceInfoBinder(Context context) {
        super(context);
    }

    @Override
    public void bind(BaseViewHolder<DeviceInfoItem> holder, DeviceInfoItem item) {
        final DeviceInfoHolder actualHolder = (DeviceInfoHolder) holder;

        actualHolder.getName().setText(item.getName());
        actualHolder.getAddress().setText(item.getAddress());
        actualHolder.getDeviceClass().setText(item.getBluetoothDeviceClassName());
        actualHolder.getMajorClass().setText(item.getBluetoothDeviceMajorClassName());
        actualHolder.getBondingState().setText(item.getBluetoothDeviceBondState());
        actualHolder.getServices().setText(createSupportedDevicesString(item));
    }


    private String createSupportedDevicesString(DeviceInfoItem item) {
        final String retVal;

        if (item.getBluetoothDeviceKnownSupportedServices().isEmpty()) {
            retVal = getContext().getString(R.string.no_known_services);
        } else {
            final StringBuilder sb = new StringBuilder();

            for (final BluetoothService service : item.getBluetoothDeviceKnownSupportedServices()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }

                sb.append(service);
            }
            retVal = sb.toString();
        }

        return retVal;
    }

    @Override
    public boolean canBind(RecyclerViewItem item) {
        return item instanceof DeviceInfoItem;
    }
}
