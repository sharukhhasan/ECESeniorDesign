package com.hrl.bluetoothlowenergy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hrl.bluetoothlowenergy.R;

public class OnboardingActivity extends AppCompatActivity {
    private static final String TAG = "OnboardingActivity";


    private Button mConnectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mConnectBtn = (Button) findViewById(R.id.deviceConnectionBtn);
        mConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingActivity.this, DeviceConnectionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
