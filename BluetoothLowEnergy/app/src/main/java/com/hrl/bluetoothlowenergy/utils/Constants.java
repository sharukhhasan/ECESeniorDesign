package com.hrl.bluetoothlowenergy.utils;

import android.os.ParcelUuid;

import java.text.DecimalFormat;

/**
 * Created by Sharukh Hasan on 3/24/17.
 *
 */
public class Constants {

    public static final ParcelUuid OBEX_OBJECT_PUSH = ParcelUuid.fromString("00001105-0000-1000-8000-00805f9b34fb");

    public static final DecimalFormat DOUBLE_TWO_DIGIT_ACCURACY = new DecimalFormat("#.##");
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
}
