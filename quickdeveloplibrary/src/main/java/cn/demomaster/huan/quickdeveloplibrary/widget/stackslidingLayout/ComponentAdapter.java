package cn.demomaster.huan.quickdeveloplibrary.widget.stackslidingLayout;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * Created by amitshekhar on 14/01/17.
 */

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder> {

    private List<String> data;
    private Context context;

    public ComponentAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public ComponentAdapter(Context context, int textColor) {
        this.context = context;
        this.textColor = textColor;
        data = new ArrayList<>();
    }

    public void updateList(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    private int textColor = Color.WHITE;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position, textColor);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }

        public void onBind(final int position, int textColor) {
            String clazz = data.get(position);
            tv_title.setText(clazz);
            tv_title.setTextColor(textColor);
        }
    }

}