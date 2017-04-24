package com.hrl.bluetoothlowenergy.blue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hrl.bluetoothlowenergy.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Sharukh Hasan on 4/21/17.
 */
public class DeviceConnectActivity extends AppCompatActivity {
    private static final String TAG = "ConnectActivity";
    private static final String DEVICE_ADDRESS = "B8:27:EB:C7:C4:11";
    private static final UUID DEVICE_UUID = UUID.fromString("00001111-0000-1000-8000-00805f9b34fb");

    public static int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothSocket mBluetoothSocket;
    private OutputStream mOutputStream;

    private Set<BluetoothDevice> mPairedDevices;


    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String device_name = device.getName();
                String device_address = device.getAddress();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connect);

        initalizeBluetooth();

        connectBluetooth();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBluetoothReceiver, filter);
    }

    @Override


    private void initalizeBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth not supported.");
        } else {
            if(mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "Bluetooth is enabled.");
            } else {
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);

                mPairedDevices = mBluetoothAdapter.getBondedDevices();
                if(mPairedDevices.size() > 0) {
                    for(BluetoothDevice device : mPairedDevices) {
                        String device_name = device.getName();
                        String device_address = device.getAddress();
                    }
                }
            }
        }
    }

    private void connectBluetooth() {
        try {
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(DEVICE_UUID);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        mBluetoothAdapter.cancelDiscovery();
        try {
            mBluetoothSocket.connect();
            mOutputStream = mBluetoothSocket.getOutputStream();

            Log.d(TAG, "Connection established.");
        } catch (Exception e) {
            Log.e(TAG, "Connection failed.");

            try {
                mBluetoothSocket.close();
            } catch (IOException ex) {
                Log.e(TAG, ex.toString());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBluetoothReceiver);
    }
}
