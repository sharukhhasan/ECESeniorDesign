package com.hrl.bluetoothlowenergy.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.hrl.bluetoothlowenergy.activity.DiscoveryActivity;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Sharukh Hasan on 3/24/17.
 *
 */
public final class BluetoothUtils {
    public final static int REQUEST_ENABLE_BT = 2001;
    private final Activity mActivity;
    private final BluetoothAdapter mBluetoothAdapter;

    public BluetoothUtils(final Activity activity) {
        mActivity = activity;
        final BluetoothManager btManager = (BluetoothManager) mActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = btManager.getAdapter();
    }

    public void askUserToEnableBluetoothIfNeeded() {
        if (isBluetoothLeSupported() && (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())) {
            final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public boolean isBluetoothLeSupported() {
        return mActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public boolean isBluetoothOn() {
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            return mBluetoothAdapter.isEnabled();
        }
    }

    public static String getDeviceAddress(String devString){
        if(devString!=null && !devString.isEmpty()){
            String[] splitDevInfo = devString.split("\\s+");
            return splitDevInfo[1];
        }
        return null;
    }

    /**
     * takes a device description string in form of
     * device name followed by space followed by device address:
     * "DeviceName 00:11:22:33:44:55"
     * splits the name and address and returns the address
     * @param devString
     * @return device name
     */
    public static String getDeviceName(String devString){
        if(devString!=null && !devString.isEmpty()){
            String[] splitDevInfo = devString.split("\\s+");
            return splitDevInfo[0];
        }
        return null;
    }

    /**
     * takes a set of bluetooth devices, extracts name and address
     * of each device and packages it into ArrayList<String>
     * each string has a format of:
     * "DeviceName 00:11:22:33:44:55"
     * @param devices
     * @return
     */
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