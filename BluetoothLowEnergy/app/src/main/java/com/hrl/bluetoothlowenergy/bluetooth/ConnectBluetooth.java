package com.hrl.bluetoothlowenergy.bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hrl.bluetoothlowenergy.activity.DeviceConnectionActivity;
import com.hrl.bluetoothlowenergy.activity.MainActivity;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Sharukh Hasan on 5/3/17.
 */

public class ConnectBluetooth extends AsyncTask<Void, Void, Void> {
    private BluetoothDevice bluetoothDevice;
    private Activity activity;
    private boolean connectSuccess = false;
    ProgressDialog progressDialog;

    public ConnectBluetooth(Activity activity, BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity, "Connecting...", "Please Wait...");
    }

    @Override
    protected Void doInBackground(Void... params) {
        final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        BluetoothSocket tmp;
        try {
            tmp = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            DeviceConnectionActivity.mmSocket = tmp;
            DeviceConnectionActivity.mmSocket.connect();
            connectSuccess = true;
        } catch (IOException e) {
            Log.d("Main", "doInBackground failed");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        if(!connectSuccess){
            Toast.makeText(activity, "Connection failed", Toast.LENGTH_SHORT).show();
            activity.finish();
        } else {
            Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
