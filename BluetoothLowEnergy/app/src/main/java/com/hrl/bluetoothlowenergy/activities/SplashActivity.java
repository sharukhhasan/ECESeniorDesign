package com.hrl.bluetoothlowenergy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.hrl.bluetoothlowenergy.R;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Sharukh Hasan on 2/8/17.
 *
 * Splash activity
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
    }
}
