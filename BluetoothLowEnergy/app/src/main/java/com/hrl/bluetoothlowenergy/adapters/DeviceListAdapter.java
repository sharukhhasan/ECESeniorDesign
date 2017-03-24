package com.hrl.bluetoothlowenergy.adapters;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.hrl.bluetoothlowenergy.R;

/**
 * Created by Sharukh Hasan on 7/20/16.
 * Copyright © 2016 Coapt Engineering. All rights reserved.
 */
public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {
    private Context context;
    private final List<BluetoothDevice> deviceItems;
    private BluetoothAdapter bTAdapter;


    public DeviceListAdapter(Context context, List<BluetoothDevice> deviceItems, BluetoothAdapter bTAdapter) {
        super(context, android.R.layout.simple_list_item_1, deviceItems);
        this.bTAdapter = bTAdapter;
        this.deviceItems = deviceItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return deviceItems.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return deviceItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView titleText;
        TextView macText;
        TextView rssiText;

        private ViewHolder(View view){
            titleText = (TextView) view.findViewById(R.id.deviceName);
            macText = (TextView) view.findViewById(R.id.deviceAddress);
            rssiText = (TextView) view.findViewById(R.id.deviceRssi);
        }
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;
        ViewHolder holder;

        if(view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.device_view, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        BluetoothDevice device = deviceItems.get(position);
        holder.titleText.setText(device.getName());
        holder.macText.setText(device.getAddress());

        return view;
    }
}