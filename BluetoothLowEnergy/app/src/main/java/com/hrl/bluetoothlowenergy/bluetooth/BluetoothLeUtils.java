package com.hrl.bluetoothlowenergy.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/**
 * Created by Sharukh Hasan on 1/26/17.
 *
 * Bluetooth Low Energy utilities
 */
public class BluetoothLeUtils {
    private final static String TAG = BluetoothLeUtils.class.getSimpleName();

    public static final int STATUS_BLE_ENABLED = 0;
    public static final int STATUS_BLUETOOTH_NOT_AVAILABLE = 1;
    public static final int STATUS_BLE_NOT_AVAILABLE = 2;
    public static final int STATUS_BLUETOOTH_DISABLED = 3;

    // Use this check to determine whether BLE is supported on the device
    public static int getBleStatus(Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return STATUS_BLE_NOT_AVAILABLE;
        }

        final BluetoothManager manager = getBluetoothManager(context);
        final BluetoothAdapter adapter;
        if (manager != null) {
            adapter = manager.getAdapter();
        } else {
            return STATUS_BLUETOOTH_NOT_AVAILABLE;
        }

        if (!adapter.isEnabled()) {
            return STATUS_BLUETOOTH_DISABLED;
        }

        return STATUS_BLE_ENABLED;
    }

    // Initializes a Bluetooth adapter
    public static BluetoothManager getBluetoothManager(Context context) {
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            return null;
        } else {
            return bluetoothManager;
        }
    }

    public static void enableWifi(boolean enable, Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        if (bytes != null) {
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
        else return null;
    }

    public static String byteToHex(byte value) {
        if (value>0x0f) {
            char[] hexChars = new char[2];
            hexChars[0] = hexArray[value >>> 4];
            hexChars[1] = hexArray[value & 0x0F];
            return new String(hexChars);
        }
        else {
            return "" + hexArray[value & 0x0F];
        }
    }

    public static String stringToHex(String string) {
        return bytesToHex(string.getBytes());
    }

    public static String bytesToHexWithSpaces(byte[] bytes) {
        StringBuilder newString = new StringBuilder();
        for (byte aByte : bytes) {
            String byteHex = String.format("0x%02X", (byte) aByte);
            newString.append(byteHex).append(" ");

        }
        return newString.toString().trim();

    }


    public static String getUuidStringFromByteArray(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (byte aByte : bytes) {
            buffer.append(String.format("%02x", aByte));
        }
        return buffer.toString();
    }

    public static UUID getUuidFromByteArrayBigEndian(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }

    public static UUID getUuidFromByteArraLittleEndian(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(low, high);
    }
}