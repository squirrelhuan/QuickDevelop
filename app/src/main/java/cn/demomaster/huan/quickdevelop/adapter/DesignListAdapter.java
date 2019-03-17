package cn.demomaster.huan.quickdevelop.adapter;


import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.designer.WebViewFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

/**
 * Created by amitshekhar on 14/01/17.
 */

public class DesignListAdapter extends RecyclerView.Adapter<DesignListAdapter.ViewHolder> {

    private List<String> data;
    private Context context;

    public DesignListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void updateList(List<String> data) {
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

        RelativeLayout rl_preview;
        ImageTextView item_icon;
        TextView item_name;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_preview = itemView.findViewById(R.id.rl_preview);
            item_name = itemView.findViewById(R.id.item_name);
        }

        public void onBind(int position) {
            String string = data.get(position);
            JSONObject jsonObject = JSON.parseObject(string);
            item_name.setText((CharSequence) jsonObject.get("name"));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       // (Fragment) WebViewFragment.class.newInstance()
                        WebViewFragment webViewFragment = new WebViewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("URL", (String) jsonObject.get("url"));
                        webViewFragment.setArguments(bundle);
                        FragmentActivityHelper.getInstance().startFragment((AppCompatActivity) context, webViewFragment);
                }
            });
        }
    }

}