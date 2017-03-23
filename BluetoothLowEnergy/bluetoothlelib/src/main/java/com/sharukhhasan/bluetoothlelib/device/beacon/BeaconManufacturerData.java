package com.sharukhhasan.bluetoothlelib.device.beacon;

import java.util.Arrays;

/**
 * Created by Sharukh Hasan on 3/23/17.
 *
 */
public abstract class BeaconManufacturerData {
    private final BeaconType mBeaconType;
    private final byte[] mData;

    protected BeaconManufacturerData(final BeaconType expectedType, final byte[] data){
        if (BeaconUtils.getBeaconType(data) != expectedType) {
            throw new IllegalArgumentException(
                    "Manufacturer record '"
                            + Arrays.toString(data)
                            + "' is not from a " + expectedType);
        }

        this.mData = data;
        this.mBeaconType = expectedType;
    }

    public BeaconType getBeaconType(){
        return mBeaconType;
    }

    public byte[] getData(){
        return mData;
    }
}
