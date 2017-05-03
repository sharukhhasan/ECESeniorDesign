package com.hrl.bluetoothlowenergy.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.UUID;

import static com.hrl.bluetoothlowenergy.utils.Constants.ST_NONE;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_CONNECTED;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_CONNECTING;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_DISCONNECTED;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_ERROR;
import static com.hrl.bluetoothlowenergy.utils.Constants.DEV_INFO_STR;
import static com.hrl.bluetoothlowenergy.utils.Constants.MESSAGE_CON_STATE_CHANGE;


/**
 * this class sets up and facilitates communication
 * with a remote Bluetooth device
 */

public class BluetoothConnectionService {
    private final String LOG_TAG = "BTConnectionService";
    private final UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ConnectThread mConnectThread;
    private Handler mHandler;
    private int mState;
    private boolean mIsConnected;
    private BluetoothAdapter mBtAdapter;
    private Context mParentContext;
    private BluetoothDevice mBtDevice;

    public BluetoothConnectionService(Context context, Handler handler) {
        mHandler = handler;
        mParentContext = context;
        mState = ST_NONE;
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    public synchronized boolean isConnected(){
        if(mConnectThread!=null && mConnectThread.isConnected()){
            return true;
        }
        return false;
    }

    public synchronized boolean isConnectedToDevice(String mac){
        if(mConnectThread!=null){
            return mConnectThread.isConnectedToDevice(mac);
        }
        return false;
    }


    //TODO delete boolean secure, or implement insecure option
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        if (mConnectThread == null) {
            mConnectThread = new ConnectThread(device, mHandler);
        } else {
            disconnect();
            //mConnectThread.cancel();
            mConnectThread = new ConnectThread(device, mHandler);

        }
        mConnectThread.start();
    }

    public synchronized void disconnect() {
        if (mConnectThread != null) {
            //TODO delete logging
            mConnectThread.interrupt();
            if(mConnectThread!=null){
                mConnectThread.cancel();
            }
            Log.v(LOG_TAG, " called disconnect()");
            mBtDevice = null;
            handleToUI(MESSAGE_CON_STATE_CHANGE, null);
        }
    }

    public synchronized BluetoothDevice getDevice(){
        if(mConnectThread!=null){
            return mConnectThread.geBtDevice();
        }
        return null;
    }

    /**
     * @return returns connection state
     */
    public int getState() {
        return mState;
    }

    /**
     * this method send a toast message to the UI thread
     *
     * @param toast
     */
    private void sendToastToUi(String toast) {
        if (toast != null & !toast.isEmpty()) {
            Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TOAST_STR, toast);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }

    }

    /**
     * send a message to a remote bluetooth device
     *
     * @param message message in string form;
     */
    public void sendToRemoteBt(String message) {
        ConnectThread thread;

        synchronized (this) {
            if (mState != ST_CONNECTED) {
                return;
            }
            thread = mConnectThread;
        }

        if (message != null & !message.isEmpty()) {
            //TODO delete logging
            Log.v(LOG_TAG, "sending message");
            thread.write(message.getBytes());
        }
    }


    /**
     * this method send message to UI handler
     */
    private void handleToUI(int msgWhat, @Nullable BluetoothDevice device) {
        switch (msgWhat) {
            case MESSAGE_CON_STATE_CHANGE: {
                if (mState == ST_CONNECTED && device != null) {
                    String devinfo = device.getName() + " " + device.getAddress();
                    Message msg = mHandler.obtainMessage(msgWhat, mState, -1);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DEV_INFO_STR, devinfo);
                    msg.setData(bundle);
                    //TODO delete logging
                    Log.v(LOG_TAG, "sending msg with: " + msg.getData().getString(DEV_INFO_STR));
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = mHandler.obtainMessage(msgWhat, mState, -1);
                    mHandler.sendMessage(msg);
                }
                break;
            }
        }

    }


    /**
     * since we are connecting only to one device this thread
     * will manage connection and communication with the
     * remote bluetooth device
     */
    private class ConnectThread extends Thread {

        private final BluetoothDevice mmBtDevice;
        private final BluetoothSocket mmSocket;
        private InputStream mmInputStream;
        private OutputStream mmOutputStream;
        private BluetoothAdapter mmAdapter = BluetoothAdapter.getDefaultAdapter();


        private byte[] mmATComand = "AT".getBytes();
        private String mmOkResponse = "OK";

        byte[] mmInArray = new byte[64];
        byte[] mmOutArray;
        int mmBytesAvailable;
        int mmBytesReceived;
        Handler mmHancler;
        Message mmMessage;


        public ConnectThread(BluetoothDevice device, Handler handler) {

            mmBtDevice = device;
            mmInputStream = null;
            mmOutputStream = null;
            mmBytesReceived = 0;
            mmHancler = handler;


            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(APP_UUID);
                //tmp = device.createInsecureRfcommSocketToServiceRecord(APP_UUID);
                mState = ST_CONNECTING;
            } catch (IOException ioe) {
                String errorMessage = "Filed to establishe RFCOMM channel.";
                Log.e(LOG_TAG, errorMessage, ioe);
                sendToastToUi(errorMessage);

            }

            mmSocket = tmp;

        }


        @Override
        public void run() {
            //TODO delete logging
            Log.v(LOG_TAG, "starting ConntectThread");

            //making sure discovery is disabled
            mBtAdapter.cancelDiscovery();

            //TODO handle error for already connected socket
            try {
                if(interrupted()){
                    cancel();
                    return;
                }

                if (!mmSocket.isConnected()) {
                    mmSocket.connect();
                }
                mState = ST_CONNECTED;
            } catch (IOException ioe1) {
                try {
                    mmSocket.close();
                    mState = ST_ERROR;
                } catch (IOException ioe2) {
                    Log.e(LOG_TAG, "closing socket failed", ioe2);
                    mState = ST_ERROR;
                }
                Log.e(LOG_TAG, "failed to connect", ioe1);
                sendToastToUi("failed to connect");
                mmInputStream = switchInputStream(mmInputStream, mmSocket, false);
                mmOutputStream = switchOutputStream(mmOutputStream, mmSocket, false);
            }


            //check input and output streams
            if (mmInputStream == null) {
                mmInputStream = switchInputStream(mmInputStream, mmSocket, true);
            }
            if (mmOutputStream == null) {
                mmOutputStream = switchOutputStream(mmOutputStream, mmSocket, true);
            }

            //TODO refactor to implement isConnected()
            if (mmInputStream != null && mmOutputStream != null) {
                mState = ST_CONNECTED;
                handleToUI(MESSAGE_CON_STATE_CHANGE, mmBtDevice);
            } else {
                mState = ST_DISCONNECTED;
                handleToUI(MESSAGE_CON_STATE_CHANGE, mmBtDevice);
            }


            //TODO add reconnection attempts
            while (mState == ST_CONNECTED) {

                if(interrupted()){
                    cancel();
                    return;
                }

                try {
                    //FIX for data fragmentation: read a
                    // sacrificial character and discart it
                    mmInputStream.read();

                    //read input into buffer
                    mmBytesReceived = mmInputStream.read(mmInArray);

                    String input = new String(mmInArray);
                    //TODO delete logging

                    Log.v(LOG_TAG, input);
                    sendToastToUi(input);

                    //zero out the array
                    Arrays.fill(mmInArray, 0, mmInArray.length, (byte)0);

                    //TODO delete logging
                    Log.v(LOG_TAG, new String(mmInArray));
                } catch (IOException ioe) {
                    Log.v(LOG_TAG, "starting error reading InputStream");
                    mState = ST_ERROR;

                }
            }

            cancel();

        }

        /**
         * this smehod terminates the connectionn by
         * closing input and output streams,
         * closing bluettooth socket,
         * and upates the conection status
         */
        public void cancel() {

            mmOutputStream = switchOutputStream(mmOutputStream, mmSocket, false);
            mmInputStream = switchInputStream(mmInputStream, mmSocket, false);

            //close socket
            try {
                mmSocket.close();
                mState = ST_DISCONNECTED;
            } catch (IOException ioe2) {
                Log.e(LOG_TAG, "closing socket failed", ioe2);
                mState = ST_ERROR;
            }

            handleToUI(MESSAGE_CON_STATE_CHANGE, null);

            synchronized (BluetoothConnectionService.this) {
                mConnectThread = null;
            }

        }

        public void write(byte[] writeBytes) {
            try {
                mmOutputStream.write(writeBytes);
                Log.v(LOG_TAG, "data sent");
            } catch (IOException ioe) {
                Log.e(LOG_TAG, "failed to write to socked", ioe);
                mState = ST_ERROR;
            }
        }


        /**
         * this method opens or closes InputStream
         *
         * @param stream stream to open or close
         * @param socket socket to get the stream from
         * @param open   action true for open, false for close
         */
        public InputStream switchInputStream(InputStream stream, BluetoothSocket socket, boolean open) {
            if (socket != null && open) {
                try {
                    if (socket.isConnected()) {
                        return socket.getInputStream();
                    }else{
                        //we should not be here
                        Log.d(LOG_TAG, "socket is closed: can't get InputStream");
                        mState = ST_ERROR;
                    }
                } catch (IOException ioe) {
                    Log.e(LOG_TAG, "failed to get InputStream.", ioe);
                    mState = ST_ERROR;
                }
            } else if (!open) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ioe) {
                        Log.e(LOG_TAG, "failed to close InputStream.", ioe);
                        mState = ST_ERROR;
                    }
                }
            }
            return null;
        }


        /**
         * this method opens or closes OutputStream
         *
         * @param stream stream to open or close
         * @param socket socket to get the stream from
         * @param open   action true for open, false for close
         */
        public OutputStream switchOutputStream(OutputStream stream, BluetoothSocket socket, boolean open) {
            if (socket != null && open) {
                try {
                    if (socket.isConnected()) {
                        return socket.getOutputStream();
                    }else{
                        //we should not be here
                        Log.d(LOG_TAG, "socket is closed: can't get OutputStream");
                        mState = ST_ERROR;
                    }
                } catch (IOException ioe) {
                    Log.e(LOG_TAG, "failed to get OutputStream.", ioe);
                    mState = ST_ERROR;
                }
            } else if (!open) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ioe) {
                        Log.e(LOG_TAG, "failed to close OutputStream.", ioe);
                        mState = ST_ERROR;
                    }
                }
            }
            return null;

        }


        /**
         * this method validates connection with a remote bluetooth device
         *
         * @return
         */
        public boolean isConnected() {
            String LOG_TAG = "_isConnected";
            byte[] response = null;

            //TODO validate mState
            boolean connected = false;
            if (mmSocket.isConnected() && mState == ST_CONNECTED) {
                if (mmInputStream != null && mmOutputStream != null) {
                    return true;
                }

            }
            return connected;
        }
        public boolean isConnectedToDevice(String macAddreass) {
            if (isConnected()) {
                if (mmBtDevice != null && mmBtDevice.getAddress().equals(macAddreass)) {
                    //TODO delete logging
                    Log.v(LOG_TAG, " in isConnectedToDevice: addresses match");
                    return true;
                }
            }
            return false;
        }


        public BluetoothDevice geBtDevice(){
            return mmBtDevice;
        }

    }


}