package com.hrl.bluetoothlowenergy.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.hrl.bluetoothlowenergy.R;
import com.hrl.bluetoothlowenergy.activity.common.Navigation;
import com.hrl.bluetoothlowenergy.bluetooth.containers.BluetoothLeDeviceStore;
import com.hrl.bluetoothlowenergy.bluetooth.device.BluetoothLeDevice;
import com.hrl.bluetoothlowenergy.bluetooth.device.beacon.BeaconType;
import com.hrl.bluetoothlowenergy.bluetooth.device.beacon.BeaconUtils;
import com.hrl.bluetoothlowenergy.bluetooth.device.beacon.ibeacon.IBeaconDevice;
import com.hrl.bluetoothlowenergy.bluetooth.util.ByteUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sharukh Hasan on 4/4/17.
 *
 */
public class Sharer {

    private static final String CSV_FILENAME_PREFIX = "bluetooth_le_%d";
    private static final String CSV_FILENAME_SUFFIX = ".csv";

    private static File getExternalCacheDir(final Context context) {
        final File[] files = ContextCompat.getExternalCacheDirs(context);
        final File retVal;

        if (files == null || files.length == 0 || files[0] == null) {
            retVal = null;
        } else {
            retVal = files[0];
        }

        return retVal;
    }

    private static String getListAsCsv(List<BluetoothLeDevice> deviceList) {

        final StringBuilder sb = new StringBuilder();
        sb.append(CsvWriterHelper.addStuff("mac"));
        sb.append(CsvWriterHelper.addStuff("name"));
        sb.append(CsvWriterHelper.addStuff("firstTimestamp"));
        sb.append(CsvWriterHelper.addStuff("firstRssi"));
        sb.append(CsvWriterHelper.addStuff("currentTimestamp"));
        sb.append(CsvWriterHelper.addStuff("currentRssi"));
        sb.append(CsvWriterHelper.addStuff("adRecord"));
        sb.append(CsvWriterHelper.addStuff("iBeacon"));
        sb.append(CsvWriterHelper.addStuff("uuid"));
        sb.append(CsvWriterHelper.addStuff("major"));
        sb.append(CsvWriterHelper.addStuff("minor"));
        sb.append(CsvWriterHelper.addStuff("txPower"));
        sb.append(CsvWriterHelper.addStuff("distance"));
        sb.append(CsvWriterHelper.addStuff("accuracy"));
        sb.append('\n');

        for (final BluetoothLeDevice device : deviceList) {
            sb.append(CsvWriterHelper.addStuff(device.getAddress()));
            sb.append(CsvWriterHelper.addStuff(device.getName()));
            sb.append(CsvWriterHelper.addStuff(TimeFormatter.getIsoDateTime(device.getFirstTimestamp())));
            sb.append(CsvWriterHelper.addStuff(device.getFirstRssi()));
            sb.append(CsvWriterHelper.addStuff(TimeFormatter.getIsoDateTime(device.getTimestamp())));
            sb.append(CsvWriterHelper.addStuff(device.getRssi()));
            sb.append(CsvWriterHelper.addStuff(ByteUtils.byteArrayToHexString(device.getScanRecord())));
            final boolean isIBeacon = BeaconUtils.getBeaconType(device) == BeaconType.IBEACON;
            final String uuid;
            final String minor;
            final String major;
            final String txPower;
            final String distance;
            final String accuracy;

            if (isIBeacon) {
                final IBeaconDevice beacon = new IBeaconDevice(device);
                uuid = String.valueOf(beacon.getUUID());
                minor = String.valueOf(beacon.getMinor());
                major = String.valueOf(beacon.getMajor());
                txPower = String.valueOf(beacon.getCalibratedTxPower());
                distance = beacon.getDistanceDescriptor().toString().toLowerCase(Locale.US);
                accuracy = String.valueOf(beacon.getAccuracy());
            } else {
                uuid = "";
                minor = "";
                major = "";
                txPower = "";
                distance = "";
                accuracy = "";
            }

            sb.append(CsvWriterHelper.addStuff(isIBeacon));
            sb.append(CsvWriterHelper.addStuff(uuid));
            sb.append(CsvWriterHelper.addStuff(minor));
            sb.append(CsvWriterHelper.addStuff(major));
            sb.append(CsvWriterHelper.addStuff(txPower));
            sb.append(CsvWriterHelper.addStuff(distance));
            sb.append(CsvWriterHelper.addStuff(accuracy));

            sb.append('\n');
        }

        return sb.toString();
    }

    private static FileWriter saveToFile(final File file, final String contents) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.append(contents);
            writer.flush();

        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return writer;
    }

    public void shareDataAsEmail(final Activity activity, final BluetoothLeDeviceStore store) {
        final long timeInMillis = System.currentTimeMillis();
        final String filename = String.format(Locale.US, CSV_FILENAME_PREFIX, timeInMillis);
        final String to = null;
        final String subject = activity.getString(R.string.exporter_email_device_list_subject, TimeFormatter.getIsoDateTime(timeInMillis));
        final String message = activity.getString(R.string.exporter_email_device_list_body);
        final String contents = getListAsCsv(store.getDeviceList());
        final File outputDir = getExternalCacheDir(activity);

        if (outputDir == null) {
            Toast.makeText(activity, R.string.error_unable_to_access_external_storage, Toast.LENGTH_SHORT).show();
        } else {
            try {
                final File outputFile = File.createTempFile(filename, CSV_FILENAME_SUFFIX, outputDir);
                saveToFile(outputFile, contents);

                final Uri uri = Uri.fromFile(outputFile);
                new Navigation(activity).shareFileViaEmail(uri, new String[]{to}, subject, message);

            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
