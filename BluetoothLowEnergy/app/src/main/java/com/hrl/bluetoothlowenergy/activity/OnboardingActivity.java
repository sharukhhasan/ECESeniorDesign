package com.hrl.bluetoothlowenergy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.hrl.bluetoothlowenergy.R;

import butterknife.BindView;
import butterknife.OnClick;

public class OnboardingActivity extends AppCompatActivity {
    private static final String TAG = "OnboardingActivity";


    @BindView(R.id.deviceConnectionBtn)
    Button mConnectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.deviceConnectionBtn)
    private void onConnectClick() {
        Intent intent = new Intent(this, DeviceConnectionActivity.class);
        startActivity(intent);
        finish();
    }

}
