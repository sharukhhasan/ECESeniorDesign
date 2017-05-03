package com.hrl.bluetoothlowenergy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.utils.Constants;

public class DeviceDetailsActivity extends AppCompatActivity {
    private static final String TAG = "DeviceDetailsActivity";

    private TextView mBatteryLevel;
    private TextView mDeviceName;
    private TextView mDeviceAddress;
    private TextView mDeviceRssi;

    private int mBatteryValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mBatteryValue = extras.getInt(MainActivity.BATTERY_LEVEL);
        }

        mBatteryLevel = (TextView) findViewById(R.id.deviceBatteryTitle);
        String lvl = String.valueOf(mBatteryValue) + "%";
        mBatteryLevel.setText(lvl);

        mDeviceName = (TextView) findViewById(R.id.deviceNameTitle);
        mDeviceName.setText(Constants.RPI_NAME);

        mDeviceAddress = (TextView) findViewById(R.id.deviceAddressTitle);
        mDeviceAddress.setText(Constants.RPI_ADDRESS);

        String rssi = Integer.toString(-67) + "db";
        mDeviceRssi = (TextView) findViewById(R.id.deviceRssiTitle);
        mDeviceRssi.setText(rssi);

    }
}
