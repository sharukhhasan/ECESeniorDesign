package com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.holder;

import android.view.View;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.base.BaseViewHolder;
import com.hrl.bluetoothlowenergy.activity.common.recyclerview.details.model.TextItem;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class TextHolder extends BaseViewHolder<TextItem> {

    private final TextView mText;

    public TextHolder(View itemView) {
        super(itemView);

        mText = (TextView) itemView.findViewById(R.id.text);
    }

    public TextView getTextView() {
        return mText;
    }
}
