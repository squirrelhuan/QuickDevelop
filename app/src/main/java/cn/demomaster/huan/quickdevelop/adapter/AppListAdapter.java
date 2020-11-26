package cn.demomaster.huan.quickdevelop.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;

/**
 * Created by amitshekhar on 14/01/17.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private List<String> appList;
    private Context context;

    public AppListAdapter(Context context) {
        this.context = context;
        appList = new ArrayList<>();
    }

    public void updateList(List<String> appList) {
        this.appList = appList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_warp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewName =itemView.findViewById(R.id.tv_tab_name);
        }

        public void onBind(final int position) {
            if(appList!=null&&position<appList.size()) {
                textViewName.setText(appList.get(position));
            }
        }
    }

}