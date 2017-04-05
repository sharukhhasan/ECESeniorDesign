package com.hrl.bluetoothlowenergy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.hrl.bluetoothlowenergy.R;

/**
 * Created by Sharukh Hasan on 2/8/16.
 *
 * Main activity
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
