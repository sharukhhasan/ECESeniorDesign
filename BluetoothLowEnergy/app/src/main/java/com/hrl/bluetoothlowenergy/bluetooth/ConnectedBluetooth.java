package com.hrl.bluetoothlowenergy.bluetooth;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.hrl.bluetoothlowenergy.activity.DeviceConnectionActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Sharukh Hasan on 5/3/17.
 */
public class ConnectedBluetooth extends Thread {
    private InputStream inputStream;
    private OutputStream outputStream;
    Activity activity;

    public ConnectedBluetooth(InputStream inputStream, OutputStream outputStream, Activity activity) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.activity = activity;
    }

    public void write(String message){
        Log.d("ConnectectBT", "inside write");
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final byte[] mmBuffer = new byte[1];
        while (true) {
            try {
                // Read from the InputStream.
                inputStream.read(mmBuffer);

                final String mess = new String(mmBuffer);
                activity.runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        Log.d("ConnectectBT", "inside read");
                        if (mess.compareTo("V") == 0){
                            Log.d("BluetoothThread", "string mess?");
                        }
                    }
                });
                Log.d("Service", "String: "+ mess);
                mmBuffer[0] = '\0';
            } catch (IOException e) {
                Log.d("Service", "Input stream was disconnected", e);
                break;
            }
        }
    }

    public void disconnect(){
        Log.d("ConnectectBT", "disconnect");
        if(DeviceConnectionActivity.mmSocket != null){
            try {
                DeviceConnectionActivity.mmSocket.close();
                activity.finish();
            } catch (IOException e) {
                Log.d("Service", "Socket could not be closed");
                e.printStackTrace();
            }
        }
    }
}
