package cn.demomaster.huan.quickdevelop.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;

/**
 * Created by amitshekhar on 14/01/17.
 */

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder> {

    private List<String> appList;
    private Context context;

    public ComponentAdapter(Context context) {
        this.context = context;
        appList = new ArrayList<>();
    }

    public void updateList(List<String> appList) {
        this.appList = appList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item_layout, parent, false);
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
            }
        }
    }

}