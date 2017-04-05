package com.hrl.bluetoothlowenergy.activity.common.banner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.MainActivity;

/**
 * Created by Sharukh Hasan on 4/5/17.
 * Copyright © 2017 HyperCryptic Solutions, LLC. All rights reserved.
 */
public class ConnectionBanner extends RelativeLayout {
    @BindView(R.id.banner_network_container)
    ViewGroup container;
    @BindView(R.id.banner_network_disconnected)
    ViewGroup bannerNetworkDisconnected;
    @BindView(R.id.banner_network_connected)
    TextView bannerNetworkConnected;
    @BindView(R.id.flipper)
    ViewFlipper flipper;

    public ConnectionBanner(Context context) {
        super(context);
        init(context);
    }

    public ConnectionBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ConnectionBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.connection_banner, this);
        ButterKnife.bind(this);
    }

    public void networkStateChange(boolean connected) {
        Log.i(MainActivity.TAG, "MainActivity.networkStateChange() " + connected);

        if (bannerNetworkDisconnected != null && bannerNetworkConnected != null) {
            bannerNetworkDisconnected.clearAnimation();
            bannerNetworkConnected.clearAnimation();

            if (!connected) {
                appearFromTop();
            } else if (connected && bannerNetworkDisconnected.getVisibility() == View.VISIBLE) {
                crossfade();
            } else {
                container.setVisibility(View.GONE);
                bannerNetworkConnected.setVisibility(View.GONE);
                bannerNetworkDisconnected.setVisibility(View.GONE);
                flipper.stopFlipping();
            }
        }
    }

    private void appearFromTop() {
        int height = 64;
        bannerNetworkDisconnected.setTranslationY(-height);
        bannerNetworkDisconnected.setAlpha(1f);

        bannerNetworkConnected.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
        bannerNetworkDisconnected.setVisibility(View.VISIBLE);

        bannerNetworkDisconnected.animate()
                .translationYBy(height)
                .setDuration(300)
                .setListener(null);

        flipper.startFlipping();
        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));

    }

    private void crossfade() {
        final int animationDuration = 600;

        // Set the view to 0% opacity but visible, so that it is visible (but fully transparent) during the animation.
        bannerNetworkConnected.setAlpha(0f);
        bannerNetworkConnected.setVisibility(View.VISIBLE);

        bannerNetworkDisconnected.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(null);

        bannerNetworkConnected.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dissapearFadding();
                    }
                });

        flipper.stopFlipping();
    }

    private void dissapearFadding() {

        bannerNetworkConnected.animate()
                .alpha(0f)
                .setDuration(300)
                .setStartDelay(1500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        bannerNetworkDisconnected.setVisibility(View.GONE);
                        container.setVisibility(View.GONE);
                        bannerNetworkConnected.setVisibility(View.GONE);
                    }
                });
    }

}
