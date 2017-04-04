package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder;

import android.view.View;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.AdRecordItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class AdRecordHolder extends BaseViewHolder<AdRecordItem> {

    private final TextView mStringTextView;
    private final TextView mArrayTextView;
    private final TextView mTitleTextView;

    public AdRecordHolder(View itemView) {
        super(itemView);

        mStringTextView = (TextView) itemView.findViewById(R.id.data_as_string);
        mArrayTextView = (TextView) itemView.findViewById(R.id.data_as_array);
        mTitleTextView = (TextView) itemView.findViewById(R.id.title);
    }

    public TextView getStringTextView() {
        return mStringTextView;
    }

    public TextView getArrayTextView() {
        return mArrayTextView;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }
}
