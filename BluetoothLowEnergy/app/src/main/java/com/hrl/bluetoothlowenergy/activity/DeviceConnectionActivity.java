package com.hrl.bluetoothlowenergy.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.bluetooth.ConnectBluetooth;
import com.hrl.bluetoothlowenergy.bluetooth.GetPairedDevices;

import java.util.ArrayList;

public class DeviceConnectionActivity extends AppCompatActivity {
    private static final String TAG = "DeviceConnectionActivity";

    public static BluetoothSocket mmSocket;
    BluetoothAdapter bluetoothAdapter;

    ListView lvPairedDevices;
    ListView lvRemoteDevices;

    Button mScanButton;

    ArrayAdapter<String> adapter;
    ArrayList<BluetoothDevice> bluetoothDevices;
    ArrayList<String> deviceNames;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        lvPairedDevices = (ListView) findViewById(R.id.devices_paired_lv);
        mScanButton = (Button) findViewById(R.id.scanBtn);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new ArrayList<>();
        deviceNames = new ArrayList<>();

        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }

        lvPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConnectBluetooth connectBT = new ConnectBluetooth(DeviceConnectionActivity.this, bluetoothDevices.get(position));
                connectBT.execute();
            }
        });

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discovery();
            }
        });

        scanForPairedDevices();
    }

    public void discovery() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();
    }

    private void scanForPairedDevices() {
        if(!deviceNames.isEmpty()){
            deviceNames.clear();
        }

        GetPairedDevices getPairedDevices = new GetPairedDevices();
        bluetoothDevices = getPairedDevices.showPairedDevices();
        deviceNames = new ArrayList<String>();

        for(BluetoothDevice device: bluetoothDevices){
            if(device.getName().contains("HC")){
                deviceNames.add("  "+device.getName()+"\n"+"  "+device.getAddress());
            }
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_black, deviceNames);
        lvPairedDevices.setAdapter(adapter);
    }
}
