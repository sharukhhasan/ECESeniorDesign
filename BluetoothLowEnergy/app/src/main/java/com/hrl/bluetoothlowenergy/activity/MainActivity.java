package com.hrl.bluetoothlowenergy.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.bluetooth.ConnectedBluetooth;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;
import com.hrl.bluetoothlowenergy.bluetooth.service.BluetoothLeService;
import com.hrl.bluetoothlowenergy.utils.OrientationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.hrl.bluetoothlowenergy.activity.MainActivity.bt;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mCaptureImageButton;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mConnectionStatusTextView;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mDeviceAddressTextView;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mDeviceDetailsButton;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mDisconnectButton;
import static com.hrl.bluetoothlowenergy.activity.MainActivity.mRemoteShutdownButton;

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

    public static ConnectedBluetooth bt;

    public static TextView mConnectionStatusTextView;
    public static TextView mDeviceAddressTextView;
    public static Button mCaptureImageButton;
    public static Button mDeviceDetailsButton;
    public static Button mRemoteShutdownButton;
    public static Button mDisconnectButton;

    BluetoothAdapter bluetoothAdapter;
    ListView lvPairedDevices;
    ArrayAdapter<String> adapter;
    ArrayList<BluetoothDevice> bluetoothDevices;
    ArrayList<String> deviceNames;

    private InputStream mInputStream;
    private OutputStream mOutputStream;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothLeDevice mDevice;
    private boolean mBound = false;

    private String mDeviceName;
    private String mDeviceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OrientationUtils.lockOrientationPortrait(this);

        mConnectionStatusTextView = (TextView) findViewById(R.id.connectedTextView);
        mDeviceAddressTextView = (TextView) findViewById(R.id.deviceTextView);
        mCaptureImageButton = (Button) findViewById(R.id.captureImageButton);
        mDeviceDetailsButton = (Button) findViewById(R.id.deviceDetailsButton);
        mRemoteShutdownButton = (Button) findViewById(R.id.remoteShutdownButton);
        mDisconnectButton = (Button) findViewById(R.id.disconnectButton);

        try {
            mInputStream = DeviceConnectionActivity.mmSocket.getInputStream();
            mOutputStream = DeviceConnectionActivity.mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("BluetoothService", "ERR: getting IO streams");
        }

        bt = new ConnectedBluetooth(mInputStream, mOutputStream, MainActivity.this);
        bt.write("A".toString());

        mDeviceAddressTextView.setText(mDeviceName);
        mDeviceAddressTextView.setTextColor(getResources().getColor(R.color.color_darkergray));
        mConnectionStatusTextView.setText("Connected");
        mConnectionStatusTextView.setTextColor(getResources().getColor(R.color.color_green));

        uiThread uiThread = new uiThread();
        uiThread.start();

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
}

class uiThread extends Thread {

    @Override
    public void run() {
        mCaptureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceConnectionActivity.mmSocket != null) {
                    bt.write("I");
                }
            }
        });
        mDeviceDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.write("B");
            }
        });
        mRemoteShutdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.write("h");
            }
        });
    }
}