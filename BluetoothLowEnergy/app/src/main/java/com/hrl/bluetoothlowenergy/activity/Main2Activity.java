package com.hrl.bluetoothlowenergy.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.blue.BluetoothService;
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


        //mConnectionTextView.setTextColor(getResources().getColor(R.color.color_green));

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.empty, menu);
        menu.findItem(R.id.about_menu).setVisible(true);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
