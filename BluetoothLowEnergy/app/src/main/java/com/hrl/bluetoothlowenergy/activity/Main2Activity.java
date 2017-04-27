package com.hrl.bluetoothlowenergy.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.blue.BluetoothService;
import com.hrl.bluetoothlowenergy.utils.Constants;
import com.hrl.bluetoothlowenergy.utils.OrientationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = Main2Activity.class.getSimpleName();

    // Device state codes
    private static final String DEVICE_DISCONNECTED = "Disconnected";
    private static final String DEVICE_CONNECTED = "Connected";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothService mBluetoothService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mDevice;

    private StringBuffer mOutStringBuffer;
    private String mDeviceName;
    private String mDeviceAddress;

    @BindView(R.id.deviceTextView) TextView mDeviceTextView;
    @BindView(R.id.macTextView) TextView mMacTextView;
    @BindView(R.id.connectedTextView) TextView mConnectionTextView;
    @BindView(R.id.captureImageButton) Button mCaptureImageBtn;
    @BindView(R.id.deviceDetailsButton) Button mDeviceDetailsBtn;
    @BindView(R.id.remoteShutdownButton) Button mRemoteShutdownBtn;
    @BindView(R.id.disconnectButton) Button mDisconnectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        OrientationUtils.lockOrientationPortrait(this);

        final Intent intent = getIntent();
        mDeviceName = intent.getParcelableExtra(ConnectionActivity.EXTRA_DEVICE_NAME);
        mDeviceAddress = intent.getParcelableExtra(ConnectionActivity.EXTRA_DEVICE_ADDRESS);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Main Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDeviceTextView.setText(mDeviceName);
        mDeviceTextView.setTextColor(getResources().getColor(R.color.color_darkergray));

        mMacTextView.setText(mDeviceAddress);
        mMacTextView.setTextColor(getResources().getColor(R.color.color_gray));

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if bluetooth is on, if not request that it be enabled.
        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if(mBluetoothService == null) {
            setupConnection();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check connection again in case it was not enabled during onStart()
        if(mBluetoothService == null) {
            if(mBluetoothService.getState() == BluetoothService.STATE_NONE) {
                mBluetoothService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Kill BluetoothService
        if(mBluetoothService != null) {
            mBluetoothService.stop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.empty, menu);
        menu.findItem(R.id.about_menu).setVisible(true);

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When ConnectionActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When ConnectionActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupConnection();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    this.finish();
                }
        }
    }

    // Initializing BluetoothService
    private void setupConnection() {
        Log.d(TAG, "setupConnection()");

        // Initialize the BluetoothService for connection
        mBluetoothService = new BluetoothService(this, mHandler);

        // Initialize the buffer for outgoing data
        mOutStringBuffer = new StringBuffer("");
    }

    // Make connection via BluetoothService
    private void connectDevice(Intent data, boolean secure) {
        // Get device MAC address
        String address = data.getExtras().getString(ConnectionActivity.EXTRA_DEVICE_ADDRESS);

        // Get device's BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        // Attempt to connect to device
        mBluetoothService.connect(device, secure);

    }

    // Make this device discoverable for 300 seconds
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    // Update connection TextView on the UI thread
    private void updateConnectionTextView(final String connectedText, final int textColor) {
        runOnUiThread(new Runnable() {
            public void run() {
                mConnectionTextView.setText(connectedText);
                mConnectionTextView.setTextColor(textColor);
                mConnectionTextView.invalidate();
                mConnectionTextView.postDelayed(this, 50);
            }
        });
    }

    // Handler which receives information back from BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppCompatActivity activity = Main2Activity.this;
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            updateConnectionTextView(getString(R.string.connected), getResources().getColor(R.color.color_green));
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            updateConnectionTextView(getString(R.string.title_connecting), getResources().getColor(R.color.bluetooth_blue));
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            updateConnectionTextView(getString(R.string.title_not_connected), getResources().getColor(R.color.color_red));
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    //mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to " + mDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
