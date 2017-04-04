package com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.binder;

import android.content.Context;
import android.view.View;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.Navigation;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.holder.IBeaconHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.scan.model.IBeaconItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.beacon.ibeacon.IBeaconDevice;
import com.hrl.bluetoothlowenergy.utils.Constants;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class IBeaconBinder extends BaseViewBinder<IBeaconItem> {

    private final Navigation navigation;

    public IBeaconBinder(Context context, Navigation navigation) {
        super(context);
        this.navigation = navigation;
    }

    @Override
    public void bind(BaseViewHolder<IBeaconItem> holder, IBeaconItem item) {

        final IBeaconHolder actualHolder = (IBeaconHolder) holder;
        final IBeaconDevice device = item.getDevice();

        final String accuracy = Constants.DOUBLE_TWO_DIGIT_ACCURACY.format(device.getAccuracy());

        actualHolder.getIbeaconMajor().setText(String.valueOf(device.getMajor()));
        actualHolder.getIbeaconMinor().setText(String.valueOf(device.getMinor()));
        actualHolder.getIbeaconTxPower().setText(String.valueOf(device.getCalibratedTxPower()));
        actualHolder.getIbeaconUUID().setText(device.getUUID());
        actualHolder.getIbeaconDistance().setText(
                getContext().getString(R.string.formatter_meters, accuracy));
        actualHolder.getIbeaconDistanceDescriptor().setText(device.getDistanceDescriptor().toString());

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
        return item instanceof IBeaconItem;
    }
}
