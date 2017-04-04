package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model;

import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.RecyclerViewItem;
import com.hrl.bluetoothlowenergy.bluetooth.device.adrecord.AdRecord;
import com.hrl.bluetoothlowenergy.bluetooth.util.AdRecordUtils;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class AdRecordItem implements RecyclerViewItem {

    private final String mTitle;
    private final byte[] mData;
    private final String mDataAsString;

    public AdRecordItem(final String title, final AdRecord record) {
        mTitle = title;
        mData = record.getData();
        mDataAsString = AdRecordUtils.getRecordDataAsString(record);
    }

    public String getTitle() {
        return mTitle;
    }

    public byte[] getData() {
        return mData;
    }

    public String getDataAsString() {
        return mDataAsString;
    }
}
