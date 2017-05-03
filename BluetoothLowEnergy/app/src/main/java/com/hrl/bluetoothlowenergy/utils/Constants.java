package com.hrl.bluetoothlowenergy.utils;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * Created by Sharukh Hasan on 3/24/17.
 *
 */
public class Constants {

    public static final DecimalFormat DOUBLE_TWO_DIGIT_ACCURACY = new DecimalFormat("#.##");
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String RPI_ADDRESS = "B8:27:EB:C7:C4:11";
    public static final UUID RPI_UUID = UUID.fromString("B8:27:EB:C7:C4:11");
    public static final String RPI_NAME = "raspberrypi";

    public static final int MESSAGE_CON_STATE_CHANGE= 1;
    public static final int MESSAGE_WRITE = 2;
    public static final int MESSAGE_READ = 3;
    public static final int MESSAGE_TOAST = 4;
    public static final int MESSAGE_FROM_REMOTE_DEVICE = 5;

    public static final String TOAST_STR = "TOAST";
    public static final String STATE_STR = "STATE";
    public static final String DEV_INFO_STR = "DEV_INFO";


    //state constants
    public static final int ST_ERROR = -1;
    public static final int ST_NONE = 0;
    public static final int ST_LISTEN = 1;
    public static final int ST_CONNECTING = 2;
    public static final int ST_CONNECTED = 3;
    public static final int ST_DISCONNECTED = 4;
    public static final int ST_DISCONNECTED_BY_USR = 5;


    public static final String STR_ERROR = "Error";
    public static final String STR_NONE = "None";
    public static final String STR_LISTEN = "Listen";
    public static final String STR_CONNECTING = "Connecting";
    public static final String STR_CONNECTED = "Connected";
    public static final String STR_DISCONNECTED = "Disconnected";
    public static final String STR_DISCONNECTED_BY_USR = "Disconnected by user";
}
