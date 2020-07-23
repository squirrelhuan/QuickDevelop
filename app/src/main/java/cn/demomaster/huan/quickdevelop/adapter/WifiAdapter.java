package cn.demomaster.huan.quickdevelop.adapter;


import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.text.TextUtils;
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
import cn.demomaster.huan.quickdevelop.activity.sample.utils.WifiUtil;
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
            String ssid = data.get(position).getScanResult().SSID;
            tv_title.setText(ssid);

            itemView.setTag(position);
            String wifiPassType="";
            WifiUtil.WifiCipherType wifiCipherType = data.get(position).getPasswordType();
            if(wifiCipherType==WifiUtil.WifiCipherType.WIFICIPHER_NOPASS){
                wifiPassType="";
            }else if(wifiCipherType==WifiUtil.WifiCipherType.WIFICIPHER_WEP){
                wifiPassType="WEP";
            }else if(wifiCipherType==WifiUtil.WifiCipherType.WIFICIPHER_WPA){
                wifiPassType="WPA";
            }
            if(!TextUtils.isEmpty(currentSSid)&&currentSSid.equals(ssid)){
                wifiPassType="已连接,"+WifiUtil.getInstance().getIpAddress();
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