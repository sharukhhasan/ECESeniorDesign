package com.hrl.bluetoothlowenergy.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;
import com.hrl.bluetoothlowenergy.bluetooth.service.BluetoothLeService;
import com.hrl.bluetoothlowenergy.utils.DialogFactory;
import com.hrl.bluetoothlowenergy.utils.OrientationUtils;
import com.hrl.bluetoothlowenergy.utils.Sharer;
import com.squareup.haha.perflib.Main;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sharukh Hasan on 2/8/16.
 *
 * Main activity
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String EXTRA_DEVICE = MainActivity.class.getName() + ".EXTRA_DEVICE";
    private static final String DEVICE_DISCONNECTED = "Disconnected";
    private static final String DEVICE_CONNECTED = "Connected";

    @BindView(R.id.deviceTextView) TextView mDeviceTextView;
    @BindView(R.id.connectedTextView) TextView mConnectionTextView;
    @BindView(R.id.captureImageButton) Button mCaptureImageBtn;
    @BindView(R.id.deviceDetailsButton) Button mDeviceDetailsBtn;
    @BindView(R.id.remoteShutdownButton) Button mRemoteShutdownBtn;
    @BindView(R.id.disconnectButton) Button mDisconnectBtn;

    private BluetoothLeService mBluetoothLeService;
    private BluetoothLeDevice mDevice;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName componentName, final IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDevice.getAddress());
        }

        @Override
        public void onServiceDisconnected(final ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                // updateConnectionTextView(DEVICE_CONNECTED, R.color.color_green);
                Log.d(TAG, "Connected");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                // updateConnectionTextView(DEVICE_DISCONNECTED, R.color.color_red);
                Log.d(TAG, "Disconnected");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                Log.d(TAG, "ACTION_GATT_SERVICES_DISCOVERED");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "ACTION_DATA_AVAILABLE");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OrientationUtils.lockOrientationPortrait(this);

        final Intent intent = getIntent();
        mDevice = intent.getParcelableExtra(EXTRA_DEVICE);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Main Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDeviceTextView.setText("Sharukh's Macbook Pro");
        mDeviceTextView.setTextColor(getResources().getColor(R.color.color_darkergray));
        mConnectionTextView.setText("Connected");
        mConnectionTextView.setTextColor(getResources().getColor(R.color.color_green));

        final Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.empty, menu);
        menu.findItem(R.id.about_menu).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                Log.d(TAG, "About btn clicked");
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDevice.getAddress());
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    public static Intent createIntent(final Context context, final BluetoothLeDevice device) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_DEVICE, device);
        return intent;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTING);
        return intentFilter;
    }

    void updateConnectionTextView(final String connectedText, final int textColor) {
        runOnUiThread(new Runnable() {
            public void run() {
                mConnectionTextView.setText(connectedText);
                mConnectionTextView.setTextColor(textColor);
                mConnectionTextView.invalidate();
                mConnectionTextView.postDelayed(this, 50);
            }
        });
    }
}
