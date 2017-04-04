package com.hrl.bluetoothlowenergy.bluetooth.util;

/**
 * Created by Sharukh Hasan on 4/3/17.
 */
public class BluetoothConstants {

    // UART Configuration
    public static final int BAUDRATE = 115200;
    public static final int DATA_BITS = 8;
    public static final int PARITY = 0;
    public static final int STOP_BITS = 1;
    public static final int FLOW_CONTROL = 0;

    // Set/Get Commands
    public static final String GET_SERIALIZED_NAME = "S-";
    public static final String SET_UART_BAUD_RATE = "SB";
    public static final String SET_MODEL_NAME = "SDM";
    public static final String SET_MANUFACTURER_NAME = "SDN";
    public static final String SET_SERIAL_NUMBER = "SDS";
    public static final String FACTORY_DEFAULT = "SF";
    public static final String SET_TIMERS_IN_MICROSECONDS = "SM";
    public static final String SET_NAME = "SN";
    public static final String SET_TRANSMISSION_POWER = "SP";
    public static final String SET_FEATURES = "SR";
    public static final String SET_SERVER_SERVICES = "SS";
    public static final String SET_CONNECTION_PARAMETERS = "ST";

    // Action commands
    public static final char ACTION_ECHO = '+';
    public static final char ACTION_ADVERTISE = 'A';
    public static final char ACTION_BOND = 'B';
    public static final char ACTION_DUMP_CONFIGURATION = 'D';
    public static final char ACTION_ESTABLISH_CONNECTION = 'E';
    public static final char ACTION_START_SCAN = 'F';
    public static final char ACTION_OBSERVER_ROLE = 'J';
    public static final char ACTION_DISCONNECT = 'K';
    public static final char ACTION_GET_PEER_RSSI = 'M';
    public static final char ACTION_ENTER_BROADCAST_INFO = 'N';
    public static final char ACTION_DORMANT_STATE = 'O';
    public static final char ACTION_CONNECTION_STATUS = 'Q';
    public static final char ACTION_REBOOT = 'R';
    public static final char ACTION_CHANGE_PARAMETER = 'T';
    public static final char ACTION_UNBOND = 'U';
    public static final char ACTION_FIRMWARE_VERSION = 'V';
    public static final char ACTION_STOP_SCAN = 'X';
    public static final char ACTION_STOP_ADVERTISEMENT = 'Y';
    public static final char ACTION_STOP_CONNECTING = 'Z';

    // GATT service commands
    public static final String LIST_CLIENT_SERVICES = "LC";
    public static final String LIST_SERVER_SERVICES = "LS";
    public static final String READ_CLIENT_HANDLE_VALUE = "CHR";
    public static final String WRITE_CLIENT_HANDLE_VALUE = "CHW";
    public static final String READ_CLIENT_UUID_CONFIG = "CURC";
    public static final String READ_CLIENT_UUID_VALUE = "CURV";
    public static final String NOTIFY_START_CLIENT_UUID = "CUWC";
    public static final String WRITE_CLIENT_UUID_VALUE = "CUWV";
    public static final String READ_SERVER_HANDLE_VALUE = "SHR";
    public static final String WRITE_SERVER_HANDLE_VALUE = "SHW";
    public static final String READ_SERVER_UUID_VALUE = "SUR";
    public static final String WRITE_SERVER_UUID_VALUE = "SUW";

    // Private services commands
    public static final String SET_PRIVATE_UUID_CHARACTERISTIC = "PC";
    public static final String SET_PRIMARY_SERVICE_UUID_FILTER = "PF";
    public static final String SET_PRIVATE_SERVICE_UUID = "PS";
    public static final String CLEAR_PRIVATE_SERVICE = "PZ";

    // MLDP commands
    public static final String SET_MLDP_SECURITY_MODE = "SE";
    public static final String ENABLE_MLDP_MODE = "I";

    // Scripting commands
    public static final String SHOW_SCRIPT = "LW";
    public static final String CLEAR_SCRIPT = "WC";
    public static final String PAUSE_SCRIPT = "WP";
    public static final String RUN_SCRIPT = "WR";
    public static final String WRITE_SCRIPT = "WW";

    // Enable remote mode command
    public static final char ENABLE_REMOTE_MODE = '!';

}
