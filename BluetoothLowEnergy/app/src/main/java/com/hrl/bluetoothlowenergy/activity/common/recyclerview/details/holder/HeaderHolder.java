package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder;

import android.view.View;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.HeaderItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class HeaderHolder extends BaseViewHolder<HeaderItem> {

    private final TextView mText;

    public HeaderHolder(View itemView) {
        super(itemView);

        mText = (TextView) itemView.findViewById(R.id.text);
    }

    public TextView getTextView() {
        return mText;
    }
}
