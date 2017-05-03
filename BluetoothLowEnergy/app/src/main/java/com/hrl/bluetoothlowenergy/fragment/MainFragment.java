package com.hrl.bluetoothlowenergy.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.DiscoveryActivity;
import com.hrl.bluetoothlowenergy.activity.HomeActivity;
import com.hrl.bluetoothlowenergy.service.BluetoothConnectionService;
import com.hrl.bluetoothlowenergy.utils.Constants;

import static com.hrl.bluetoothlowenergy.utils.Constants.ST_NONE;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_CONNECTED;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_CONNECTING;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_DISCONNECTED;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_DISCONNECTED_BY_USR;
import static com.hrl.bluetoothlowenergy.utils.Constants.ST_ERROR;
import static com.hrl.bluetoothlowenergy.utils.Constants.STR_CONNECTED;
import static com.hrl.bluetoothlowenergy.utils.Constants.STR_CONNECTING;
import static com.hrl.bluetoothlowenergy.utils.Constants.STR_DISCONNECTED;
import static com.hrl.bluetoothlowenergy.utils.Constants.STR_DISCONNECTED_BY_USR;
import static com.hrl.bluetoothlowenergy.utils.Constants.STR_ERROR;
import static com.hrl.bluetoothlowenergy.utils.Constants.STR_NONE;
import static com.hrl.bluetoothlowenergy.utils.Constants.DEV_INFO_STR;

/**
 * Created by Sharukh Hasan on 5/1/17
 */

public class MainFragment extends Fragment {
    private static final String LOG_TAG = "MainFragment";

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int ACTION_STATE_CHAGED = 2;
    private static final int ACTION_FOUND = 3;
    private static final int ACTION_DISCOVERY = 4;

    private TextView mConnectionStatusTextView;
    private TextView mDeviceAddressTextView;
    private Button mCaptureImageButton;
    private Button mDeviceDetailsButton;
    private Button mRemoteShutdownButton;
    private Button mDisconnectButton;

    private ImageFragment mImageFragment;
    private DeviceDetailsFragment mDeviceDetailsFragment;

    //private EditText mEditText;
    //private String mBuffer;

    //bluetooth stuff
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothConnectionService mBluetoothService;
    private BluetoothDevice mBluetoothDevice;
    private boolean mOn;
    private int mConState;
    private String mDevInfo;


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_CON_STATE_CHANGE:
                    mConState = msg.arg1;
                    Log.v(LOG_TAG, "contains devinfo: " + msg.getData().containsKey(DEV_INFO_STR));
                    if (mConState == ST_CONNECTED && msg.getData().containsKey(DEV_INFO_STR)) {
                        mDevInfo = msg.getData().getString(DEV_INFO_STR);
                        updateStatusIndicator(mConState, mDevInfo);
                    } else {
                        mDevInfo = null;
                        updateStatusIndicator(mConState, mDevInfo);
                    }

                    if (getActivity() != null) {
                        Toast.makeText(getContext(), pickState(mConState), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    if (getActivity() != null) {
                        Toast.makeText(getContext(), "just Wrote something", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (msg.getData().containsKey(Constants.TOAST_STR)) {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), msg.getData().getString(Constants.TOAST_STR), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case Constants.MESSAGE_FROM_REMOTE_DEVICE: {
                    break;
                }
            }

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "_in_onCreate()");
        setRetainInstance(true);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            Toast.makeText(homeActivity, getString(R.string.bt_admin_not_available), Toast.LENGTH_SHORT).show();
            homeActivity.finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mConnectionStatusTextView = (TextView) rootView.findViewById(R.id.connectedTextView);
        mDeviceAddressTextView = (TextView) rootView.findViewById(R.id.deviceTextView);
        mCaptureImageButton = (Button) rootView.findViewById(R.id.captureImageButton);
        mDeviceDetailsButton = (Button) rootView.findViewById(R.id.deviceDetailsButton);
        mRemoteShutdownButton = (Button) rootView.findViewById(R.id.remoteShutdownButton);
        mDisconnectButton = (Button) rootView.findViewById(R.id.disconnectButton);

        mCaptureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.gui_container, new ImageFragment(), "ImageFragmentTag");
                ft.commit();
            }
        });

        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
            }
        });

        /*mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement send message
                mBuffer = mEditText.getText().toString();

                if (mBluetoothService != null) {
                    mBluetoothService.sendToRemoteBt(mBuffer);
                    //TODO delete when done
                    Log.v(LOG_TAG, "data passed to thread");
                } else {
                    Toast.makeText(getContext(), "Service not started.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //mDevInfo = savedInstanceState.getString(DEV_INFO_STR);

        }
    }

    @Override
    public void onStart() {
        Log.v(LOG_TAG, "_in_onStart()");
        super.onStart();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(btEnableIntent, REQUEST_ENABLE_BT);
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "_in_onResume()" + mConState + " " + mDevInfo);
        mOn = mBluetoothAdapter.isEnabled();
    }

    @Override
    public void onStop() {
        Log.v(LOG_TAG, "_in_onStop()");
        super.onStop();

    }

    @Override
    public void onDestroy() {
        Log.v(LOG_TAG, "_in_onDestroy()");

        if (mBluetoothService != null) {
            mBluetoothService.disconnect();
            mConState = ST_DISCONNECTED_BY_USR;
            mDevInfo = null;
        }
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG, "_in onSaveInstanceState");
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(LOG_TAG, "_in onActivityResult");

        if (data != null) {
            switch (requestCode) {
                case REQUEST_ENABLE_BT: {
                    Toast.makeText(getContext(), String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
                    break;
                }
                case ACTION_DISCOVERY: {
                    String deviceMAC = null;
                    if (data.hasExtra(DiscoveryActivity.DEVICE_STRING)) {
                        deviceMAC = data.getStringExtra(DiscoveryActivity.DEVICE_STRING);
                    } else {
                        break;
                    }

                    Toast.makeText(getContext(), "Action Discover: device Selected: " + deviceMAC, Toast.LENGTH_SHORT).show();
                    if (data.hasExtra(BluetoothDevice.EXTRA_DEVICE)) {
                        BluetoothDevice btDev = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                        if (btDev != null) {
                            Log.v(LOG_TAG, "intent has device data for: " + btDev.getAddress());
                        } else {
                            Log.v(LOG_TAG, "intent's btDev is null ");
                        }

                        if (mBluetoothService != null) {
                            if (mBluetoothService.isConnected()) {
                                BluetoothDevice existingDevice = mBluetoothService.getDevice();
                                Log.v(LOG_TAG, "existing device: " + existingDevice.getAddress());

                                if (existingDevice != null && mBluetoothService.isConnectedToDevice(deviceMAC)) {
                                    Toast.makeText(getContext(), getString(R.string.bt_error_already_connected_same_dev), Toast.LENGTH_SHORT).show();
                                } else if (existingDevice != null) {
                                    Toast.makeText(getContext(), getString(R.string.bt_error_already_connected), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.v(LOG_TAG, "reconnecting to" + btDev.getAddress());
                                mBluetoothService.connect(btDev, true);
                            }

                        } else {
                            Log.v(LOG_TAG, "_staring service for" + btDev.getAddress());
                            mBluetoothService = new BluetoothConnectionService(getContext(), mHandler);
                            mBluetoothService.connect(btDev, true);
                        }

                    }
                    break;
                }
            }
        }
    }

    public void btOff(View v) {
        if (mBluetoothAdapter.isEnabled()) {
            Log.v(LOG_TAG, "inside btOff:");
            if (mBluetoothService != null) {
                mBluetoothService.disconnect();
                Log.v(LOG_TAG, "inside btOff: called mBluetoothService.disconnect()");
            }
            mBluetoothAdapter.disable();
            Toast.makeText(getContext(), getString(R.string.bt_admin_off), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.bt_admin_already_off), Toast.LENGTH_SHORT).show();
        }
    }

    public void btDiscover(View v) {
        Intent discoveryIntent = new Intent(getContext(), DiscoveryActivity.class);
        startActivityForResult(discoveryIntent, ACTION_DISCOVERY);
    }

    public void sendMessage(String message) {
        if (mBluetoothService != null && message != null) {
            mBluetoothService.sendToRemoteBt(message);
        } else {
            Log.v(LOG_TAG, "cant sent message: " + message + " " + String.valueOf(mBluetoothService != null));
        }
    }

    public void disconnect() {
        if (mBluetoothService != null) {
            mBluetoothService.disconnect();
            mConState = ST_DISCONNECTED_BY_USR;
            updateStatusIndicator(mConState, null);
        }
    }

    private String pickState(int code) {
        switch (code) {
            case ST_ERROR:
                return STR_ERROR;
            case ST_NONE:
                return STR_NONE;
            case ST_CONNECTING:
                return STR_CONNECTING;
            case ST_CONNECTED:
                return STR_CONNECTED;
            case ST_DISCONNECTED:
                return STR_DISCONNECTED;
            case ST_DISCONNECTED_BY_USR:
                return STR_DISCONNECTED_BY_USR;
        }
        return STR_NONE;
    }


    private void updateStatusIndicator(int statusCode, @Nullable String devinfo) {
        switch (statusCode) {
            case ST_CONNECTED: {
                if (devinfo != null) {
                    mDevInfo = devinfo;

                    mConnectionStatusTextView.setText(mDevInfo);
                } else {
                    mConnectionStatusTextView.setText(STR_CONNECTED);
                    mDevInfo = null;
                }
                break;
            }
            case ST_DISCONNECTED_BY_USR:
                mConnectionStatusTextView.setText(STR_DISCONNECTED_BY_USR);
                mDevInfo = null;
                break;

            case ST_ERROR:
                mConnectionStatusTextView.setText(STR_ERROR);
                break;
            case ST_NONE:
                mDevInfo = null;
                mConnectionStatusTextView.setText(getString(R.string.status_default));
                break;
            case ST_DISCONNECTED:
                mConnectionStatusTextView.setText(STR_DISCONNECTED);
                mDevInfo = null;
                break;
        }
    }

}