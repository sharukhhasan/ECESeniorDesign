package com.hrl.bluetoothlowenergy.activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.Navigation;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;
import com.hrl.bluetoothlowenergy.bluetooth.resolvers.GattAttributeResolver;
import com.hrl.bluetoothlowenergy.bluetooth.service.BluetoothLeService;
import com.hrl.bluetoothlowenergy.bluetooth.device.GattDataAdapterFactory;
import com.hrl.bluetoothlowenergy.bluetooth.util.ByteUtils;
import com.hrl.bluetoothlowenergy.utils.Exporter;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends AppCompatActivity {
    private static final String EXTRA_DEVICE = DeviceControlActivity.class.getName() + ".EXTRA_DEVICE";
    private static final String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "devicename";
    public static final String EXTRAS_DEVICE_ADDRESS = "deviceaddress";

    @BindView(R.id.gatt_services_list)
    protected ExpandableListView mGattServicesList;
    @BindView(R.id.connection_state)
    protected TextView mConnectionState;
    @BindView(R.id.uuid)
    protected TextView mGattUUID;
    @BindView(R.id.description)
    protected TextView mGattUUIDDesc;
    @BindView(R.id.data_as_string)
    protected TextView mDataAsString;
    @BindView(R.id.data_as_array)

    protected TextView mDataAsArray;
    private Exporter mExporter;

    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothLeService mBluetoothLeService;

    private final ExpandableListView.OnChildClickListener servicesListClickListner = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id) {
            final GattDataAdapterFactory.GattDataAdapter adapter = (GattDataAdapterFactory.GattDataAdapter) parent.getExpandableListAdapter();
            final BluetoothGattCharacteristic characteristic = adapter.getBluetoothGattCharacteristic(groupPosition, childPosition);
            final int charaProp = characteristic.getProperties();

            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(characteristic, true);
            }
            return true;
        }
    };

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

    private BluetoothLeDevice mDevice;
    private State mCurrentState = State.DISCONNECTED;

    private String mExportString;
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.
    //					      this can be a result of read or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                updateConnectionState(State.CONNECTED);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                clearUI();
                updateConnectionState(State.DISCONNECTED);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_CONNECTING.equals(action)) {
                clearUI();
                updateConnectionState(State.CONNECTING);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                final String noData = getString(R.string.no_data);
                final String uuid = intent.getStringExtra(BluetoothLeService.EXTRA_UUID_CHAR);
                final String dataArr = intent.getStringExtra(BluetoothLeService.EXTRA_DATA_RAW);

                goToMainActivity(uuid, dataArr);

                Log.d(TAG, tryString(uuid,noData));
                Log.d(TAG, tryString(dataArr, noData));

                //mGattUUID.setText(tryString(uuid, noData));
                //mGattUUIDDesc.setText(GattAttributeResolver.getAttributeName(uuid, getString(R.string.unknown)));
                //mDataAsArray.setText(tryString(dataArr, noData));
                //mDataAsString.setText(tryString(dataArr, noData));
            }
        }
    };

    public void goToMainActivity(String uuid, String data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRAS_DEVICE_NAME, uuid);
        intent.putExtra(EXTRAS_DEVICE_NAME, data);
        startActivity(intent);
        finish();
    }

    private void clearUI() {
        mExportString = null;
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mGattUUID.setText(R.string.no_data);
        mGattUUIDDesc.setText(R.string.no_data);
        mDataAsArray.setText(R.string.no_data);
        mDataAsString.setText(R.string.no_data);
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(final List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        mExportString = mExporter.generateExportString(mDevice.getName(), mDevice.getAddress(), gattServices);

        final GattDataAdapterFactory.GattDataAdapter adapter = GattDataAdapterFactory.createAdapter(this, gattServices);
        mGattServicesList.setAdapter(adapter);
        invalidateOptionsMenu();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatt_services);

        final Intent intent = getIntent();
        mDevice = intent.getParcelableExtra(EXTRA_DEVICE);
        ButterKnife.bind(this);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDevice.getAddress());
        mGattServicesList.setOnChildClickListener(servicesListClickListner);

        getSupportActionBar().setTitle(mDevice.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mExporter = new Exporter(this);

        final Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);

        switch (mCurrentState) {

            case DISCONNECTED:
                menu.findItem(R.id.menu_advance_forward).setVisible(false);
                menu.findItem(R.id.menu_connect).setVisible(true);
                menu.findItem(R.id.menu_disconnect).setVisible(false);
                menu.findItem(R.id.menu_refresh).setActionView(null);
                break;
            case CONNECTING:
                menu.findItem(R.id.menu_advance_forward).setVisible(false);
                menu.findItem(R.id.menu_connect).setVisible(false);
                menu.findItem(R.id.menu_disconnect).setVisible(false);
                menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_indeterminate_progress);
                break;
            case CONNECTED:
                menu.findItem(R.id.menu_advance_forward).setVisible(true);
                menu.findItem(R.id.menu_connect).setVisible(false);
                menu.findItem(R.id.menu_disconnect).setVisible(true);
                menu.findItem(R.id.menu_refresh).setActionView(null);
                break;
            default:
                throw new IllegalStateException("Don't know how to handle: " + mCurrentState);
        }

        if (mExportString == null) {
            menu.findItem(R.id.menu_share).setVisible(false);
        } else {
            menu.findItem(R.id.menu_share).setVisible(true);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDevice.getAddress());
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_advance_forward:
                return true;
            case R.id.menu_share:
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                final String subject = getString(
                        R.string.exporter_email_device_services_subject,
                        mDevice.getName(),
                        mDevice.getAddress());

                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, mExportString);

                startActivity(Intent.createChooser(intent, getString(R.string.exporter_email_device_list_picker_text)));

                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void updateConnectionState(final State state) {
        mCurrentState = state;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int colourId;
                final int resId;

                switch (state) {
                    case CONNECTED:
                        colourId = android.R.color.holo_green_dark;
                        resId = R.string.connected;
                        break;
                    case DISCONNECTED:
                        colourId = android.R.color.holo_red_dark;
                        resId = R.string.disconnected;
                        break;
                    case CONNECTING:
                        colourId = android.R.color.black;
                        resId = R.string.connecting;
                        break;
                    default:
                        colourId = android.R.color.black;
                        resId = 0;
                        break;
                }

                mConnectionState.setText(resId);
                mConnectionState.setTextColor(ContextCompat.getColor(DeviceControlActivity.this, colourId));
            }
        });
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

    private static String tryString(final String string, final String fallback) {
        if (string == null) {
            return fallback;
        } else {
            return string;
        }
    }

    public static Intent createIntent(final Context context, final BluetoothLeDevice device) {
        final Intent intent = new Intent(context, DeviceControlActivity.class);
        intent.putExtra(DeviceControlActivity.EXTRA_DEVICE, device);
        return intent;
    }

    private enum State {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }
}
