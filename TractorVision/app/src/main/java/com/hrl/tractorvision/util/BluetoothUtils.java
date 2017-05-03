package com.hrl.tractorvision.util;

import android.bluetooth.BluetoothDevice;

import com.hrl.tractorvision.activity.DiscoveryActivity;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Sharukh Hasan on 5/2/17.
 */

public class BluetoothUtils {

    public static String getDeviceAddress(String devString){
        if(devString!=null && !devString.isEmpty()){
            String[] splitDevInfo = devString.split("\\s+");
            return splitDevInfo[1];
        }
        return null;
    }

    public static String getDeviceName(String devString){
        if(devString!=null && !devString.isEmpty()){
            String[] splitDevInfo = devString.split("\\s+");
            return splitDevInfo[0];
        }
        return null;
    }

    public static ArrayList<String> extractDeviceString(Set<BluetoothDevice> devices){
        ArrayList<String> deviceStrings = new ArrayList<>();

        if(!devices.isEmpty()){
            for(BluetoothDevice device: devices){
                deviceStrings.add(device.getName() + " " + device.getAddress());
            }
            return deviceStrings;
        }

        return deviceStrings;
    }

    public static void fillAdapterFromSet(Set<BluetoothDevice> set, DiscoveryActivity.BtScanAdapter adapter){
        ArrayList<BluetoothDevice> arrayList = null;
        if(set!=null && adapter!=null){
            for(BluetoothDevice device: set){
                adapter.add(device);
            }
        }
    }
}
