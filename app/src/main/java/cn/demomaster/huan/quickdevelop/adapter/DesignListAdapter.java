package cn.demomaster.huan.quickdevelop.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.main.DesignPatternFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.ui.fragment.WebViewFragment;
import cn.demomaster.qdrouter_library.view.ImageTextView;

/**
 * 设计模式
 */
public class DesignListAdapter extends RecyclerView.Adapter<DesignListAdapter.ViewHolder> {

    private List<DesignPatternFragment.URLBean> data;
    private Context context;
    public DesignListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void updateList(List<DesignPatternFragment.URLBean> data) {
        this.data = data;
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
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewGroup rl_preview;
        ImageTextView item_icon;
        TextView item_name;
        public ViewHolder(View itemView) {
            super(itemView);
            rl_preview = itemView.findViewById(R.id.rl_preview);
            item_name = itemView.findViewById(R.id.item_name);
        }

        public void onBind(int position) {
            DesignPatternFragment.URLBean urlBean = data.get(position);
            item_name.setText(urlBean.getName());
            itemView.setOnClickListener(v -> {
                // (Fragment) WebViewFragment.class.newInstance()
                WebViewFragment webViewFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("URL", urlBean.getUrl());
                webViewFragment.setArguments(bundle);
                ((QDActivity)v.getContext()).startFragment(webViewFragment,R.id.container1,null);
            });
        }
    }

}