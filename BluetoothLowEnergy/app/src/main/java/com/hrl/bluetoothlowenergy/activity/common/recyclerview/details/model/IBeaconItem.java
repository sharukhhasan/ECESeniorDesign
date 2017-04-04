package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.beacon.ibeacon.IBeaconManufacturerData;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class IBeaconItem implements RecyclerViewItem {

    private final int mMajor;
    private final int mMinor;
    private final String mUuid;
    private final int mCompanyIdentifier = 0x00CD;
    private final int mIBeaconAdvertisement;
    private final int mCalibratedTxPower;

    public IBeaconItem(final IBeaconManufacturerData iBeaconData) {
        mMajor = iBeaconData.getMajor();
        mMinor = iBeaconData.getMinor();
        mUuid = iBeaconData.getUUID();
        mIBeaconAdvertisement = iBeaconData.getIBeaconAdvertisement();
        mCalibratedTxPower = iBeaconData.getCalibratedTxPower();
    }

    public int getCompanyIdentifier() {
        return mCompanyIdentifier;
    }

    public int getMajor() {
        return mMajor;
    }

    public int getMinor() {
        return mMinor;
    }

    public String getUuid() {
        return mUuid;
    }

    public int getIBeaconAdvertisement() {
        return mIBeaconAdvertisement;
    }

    public int getCalibratedTxPower() {
        return mCalibratedTxPower;
    }
}
