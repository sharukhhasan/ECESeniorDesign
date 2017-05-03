package com.hrl.bluetoothlowenergy.activity;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.bluetooth.ConnectedBluetooth;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;
import com.hrl.bluetoothlowenergy.bluetooth.service.BluetoothLeService;
import com.hrl.bluetoothlowenergy.utils.Constants;
import com.hrl.bluetoothlowenergy.utils.OrientationUtils;
import com.squareup.haha.perflib.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.hrl.bluetoothlowenergy.activity.MainActivity.bt;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mCaptureImageButton;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mDeviceDetailsButton;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mDisconnectButton;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mRemoteShutdownButton;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.power_off_flag;

/**
 * Created by Sharukh Hasan on 2/8/16.
 *
 * Main activity
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String EXTRA_DEVICE = MainActivity.class.getName() + ".EXTRA_DEVICE";
    private static final String DEVICE_DISCONNECTED = "Disconnected";
    private static final String DEVICE_CONNECTED = "Connected";

    public static final String BATTERY_LEVEL = "batterylevel";

    public static boolean power_off_flag = false;

    public static ConnectedBluetooth bt;

    public static TextView mConnectionStatusTextView;
    public static TextView mDeviceAddressTextView;
    public static TextView mDeviceNameTextView;
    public static Button mCaptureImageButton;
    public static Button mDeviceDetailsButton;
    public static Button mRemoteShutdownButton;
    public static Button mDisconnectButton;

    private InputStream mInputStream;
    private OutputStream mOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OrientationUtils.lockOrientationPortrait(this);

        mConnectionStatusTextView = (TextView) findViewById(R.id.connectedTextView);
        mDeviceAddressTextView = (TextView) findViewById(R.id.deviceAddressTextView);
        mDeviceNameTextView = (TextView) findViewById(R.id.deviceNameTv);
        mCaptureImageButton = (Button) findViewById(R.id.captureImageButton);
        mDeviceDetailsButton = (Button) findViewById(R.id.deviceDetailsButton);
        mRemoteShutdownButton = (Button) findViewById(R.id.remoteShutdownButton);
        mDisconnectButton = (Button) findViewById(R.id.disconnectBluetoothButton);

        try {
            mInputStream = DeviceConnectionActivity.mmSocket.getInputStream();
            mOutputStream = DeviceConnectionActivity.mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("BluetoothService", "ERR: getting IO streams");
        }

        bt = new ConnectedBluetooth(mInputStream, mOutputStream, MainActivity.this);
        //bt.write("A".toString());

        if (shouldAskPermissions()) {
            askPermissions();
        }

        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.disconnect();
                finish();
            }
        });

        mCaptureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceConnectionActivity.mmSocket != null) {
                    bt.write("I");
                    Intent intent = new Intent(MainActivity.this, ImageCaptureActivity.class);
                    startActivity(intent);
                }
            }
        });

        mDeviceDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.write("K");
                //int value = bt.returnBatteryLevel();
                Intent intent = new Intent(MainActivity.this, DeviceDetailsActivity.class);
                intent.putExtra(BATTERY_LEVEL, 97);
                startActivity(intent);
            }
        });

        mRemoteShutdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(power_off_flag) {
                    power_off_flag = false;
                    bt.write("J");
                    mConnectionStatusTextView.setText("Connected");
                    mConnectionStatusTextView.setTextColor(getResources().getColor(R.color.color_green));
                } else {
                    power_off_flag = true;
                    bt.write("H");
                    mConnectionStatusTextView.setText("Disconnected");
                    mConnectionStatusTextView.setTextColor(getResources().getColor(R.color.color_red));
                }
            }
        });

        mDeviceNameTextView.setText(Constants.RPI_NAME);
        mDeviceNameTextView.setTextColor(getResources().getColor(R.color.color_darkergray));
        mDeviceAddressTextView.setText(Constants.RPI_ADDRESS);
        mDeviceAddressTextView.setTextColor(getResources().getColor(R.color.color_darkergray));
        mConnectionStatusTextView.setText(getResources().getString(R.string.connected));
        mConnectionStatusTextView.setTextColor(getResources().getColor(R.color.color_green));

        //uiThread uiThread = new uiThread();
        //uiThread.start();

        bt.start();
    }

    void updateConnectionTextView(final String connectedText, final int textColor) {
        runOnUiThread(new Runnable() {
            public void run() {
                mConnectionStatusTextView.setText(connectedText);
                mConnectionStatusTextView.setTextColor(textColor);
                mConnectionStatusTextView.invalidate();
                mConnectionStatusTextView.postDelayed(this, 50);
            }
        });
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
}

class uiThread extends Thread {

    @Override
    public void run() {

    }
}