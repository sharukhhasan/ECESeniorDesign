package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.binder;

import android.content.Context;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewBinder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder.IBeaconHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.IBeaconItem;
import com.hrl.bluetoothlowenergy.bluetooth.resolvers.CompanyIdentifierResolver;
import com.hrl.bluetoothlowenergy.utils.TimeFormatter;

import java.util.Locale;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class IBeaconBinder extends BaseViewBinder<IBeaconItem> {
    private static final String STRING_FORMAT = "%s (%s)";

    public IBeaconBinder(Context context) {
        super(context);
    }

    private static String formatTime(final long time) {
        return TimeFormatter.getIsoDateTime(time);
    }

    private static String getWithHexEncode(final String first, final int value) {
        return createLine(first, hexEncode(value));
    }

    private static String getWithHexEncode(final int value) {
        return createLine(String.valueOf(value), hexEncode(value));
    }

    private static String createLine(final String first, final String second) {
        return String.format(Locale.US, STRING_FORMAT, first, second);
    }

    private static String hexEncode(final int integer) {
        return "0x" + Integer.toHexString(integer).toUpperCase(Locale.US);
    }

    @Override
    public void bind(BaseViewHolder<IBeaconItem> holder, IBeaconItem item) {
        /*final IBeaconHolder actualHolder = (IBeaconHolder) holder;

        final String companyName = CompanyIdentifierResolver.getCompanyName(item.getCompanyIdentifier(), getContext().getString(R.string.unknown));

        actualHolder.getCompanyId().setText(getWithHexEncode(companyName, item.getCompanyIdentifier()));
        actualHolder.getAdvert().setText(getWithHexEncode(item.getIBeaconAdvertisement()));
        actualHolder.getUuid().setText(item.getUuid());
        actualHolder.getMajor().setText(getWithHexEncode(item.getMajor()));
        actualHolder.getMinor().setText(getWithHexEncode(item.getMinor()));
        actualHolder.getTxPower().setText(getWithHexEncode(item.getCalibratedTxPower()));*/
    }

    @Override
    public boolean canBind(RecyclerViewItem item) {
        return item instanceof IBeaconItem;
    }
}
