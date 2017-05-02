package com.hrl.tractorvision.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.hrl.tractorvision.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @BindView(R.id.deviceConnectionBtn) Button mDeviceConnectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.deviceConnectionBtn)
    public void onDeviceConnectClick() {
        Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }
}
