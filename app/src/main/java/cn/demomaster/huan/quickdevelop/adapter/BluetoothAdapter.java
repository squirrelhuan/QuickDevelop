package cn.demomaster.huan.quickdevelop.adapter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuAdapter;

/**
 *
 */
public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.ViewHolder> {

    private Context context;

    public BluetoothAdapter(Context context) {
        this.context = context;
    }
    List<BluetoothDevice> data = new ArrayList<>();

    public List<BluetoothDevice> getData() {
        return data;
    }

    public void updateList(Set<BluetoothDevice> bluetoothDevices) {
        data.clear();
        if(bluetoothDevices!=null) {
            data.addAll(bluetoothDevices);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_wifi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    String currentSSid;
    public void setCurrentWifiName(String ssid) {
        currentSSid = ssid;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_wifi_type;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_wifi_type = itemView.findViewById(R.id.tv_wifi_type);
        }

        public void onBind(final int position) {
            BluetoothDevice device  = data.get(position);
            tv_title.setText(device.getName());
            tv_wifi_type.setText(device.getAddress());
            itemView.setTag(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = getAdapterPosition();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v,  p);
                    }
                }
            });
        }
    }

    TabMenuAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(TabMenuAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}