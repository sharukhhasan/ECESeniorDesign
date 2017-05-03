package com.hrl.bluetoothlowenergy.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Sharukh Hasan on 5/3/17.
 */

public class GetPairedDevices {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> bluetoothDevices;
    ArrayList<BluetoothDevice> devices;

    public ArrayList<BluetoothDevice> showPairedDevices() {
        devices = new ArrayList<>();
        bluetoothDevices = bluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device: bluetoothDevices){
            devices.add(device);
        }

        return devices;
    }
}
