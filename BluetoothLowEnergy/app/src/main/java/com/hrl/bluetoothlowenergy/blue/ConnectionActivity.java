package com.hrl.bluetoothlowenergy.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.hrl.bluetoothlowenergy.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ConnectionActivity extends AppCompatActivity {
    private static final String TAG = "ConnectionActivity";

    public static final String EXTRA_DEVICE_ADDRESS = "device_address";

    private final byte mDelimiter = 33;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothDevice mBluetoothDevice;

    private ArrayAdapter<String> mDevicesArrayAdapter;
    private int mBufferPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        final Handler handler = new Handler();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final class WorkerThread implements Runnable {
            private String blue_msg;

            public WorkerThread(String msg) {
                blue_msg = msg;
            }

            public void run() {
                sendBluetoothMessage(blue_msg);

                while(!Thread.currentThread().isInterrupted()) {
                    int bytesAvailable;
                    boolean workComplete = false;

                    try {
                        final InputStream input_stream;
                        input_stream = mBluetoothSocket.getInputStream();
                        bytesAvailable = input_stream.available();

                        if(bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            byte[] readBuffer = new byte[1024];
                            Log.e(TAG, "Bytes available.");
                            input_stream.read(packetBytes);

                            for(int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];

                                if(b == mDelimiter) {
                                    byte[] encodedBytes = new byte[mBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    mBufferPosition = 0;

                                    //The variable data now contains our full command
                                    handler.post(new Runnable() {
                                        public void run() {
                                            //myLabel.setText(data);
                                        }
                                    });

                                    workComplete = true;
                                    break;
                                } else {
                                    readBuffer[mBufferPosition++] = b;
                                }
                            }

                            if(workComplete) {
                                mBluetoothSocket.close();
                                break;
                            }
                        }

                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        };

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for(BluetoothDevice device : pairedDevices) {
                if(device.getName().equals("orange")) { //Note, you will need to change this to match the name of your device
                    Log.e(TAG, device.getName());
                    mBluetoothDevice = device;
                    break;
                }
            }
        }
    }

    public void sendBluetoothMessage(String msg) {
        UUID uuid = UUID.fromString("00001111-0000-1000-8000-00805F9B34FB");

        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mBluetoothSocket.isConnected()) {
                mBluetoothSocket.connect();
            }

            OutputStream mOutputStream = mBluetoothSocket.getOutputStream();
            mOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

