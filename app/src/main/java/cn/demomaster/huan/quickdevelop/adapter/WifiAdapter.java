package cn.demomaster.huan.quickdevelop.adapter;


import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.activity.sample.model.QDScanResult;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuAdapter;

/**
 * Created by amitshekhar on 14/01/17.
 */

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {

    private List<QDScanResult> data;
    private Context context;

    public WifiAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void updateList(List<QDScanResult> data) {
        this.data = data;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_wifi_type;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_wifi_type = itemView.findViewById(R.id.tv_wifi_type);
        }

        public void onBind(final int position) {
            String clazz = data.get(position).getScanResult().SSID;
            tv_title.setText(clazz);

            itemView.setTag(position);
            String wifiPassType="";
            switch (data.get(position).getPasswordType()){
                case 0:
                    wifiPassType="";
                    break;
                case 1://WEP
                    wifiPassType="WEP";
                    break;
                case 2://WPA
                    wifiPassType="WPA";
                    break;
            }
            tv_wifi_type.setText(wifiPassType);

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