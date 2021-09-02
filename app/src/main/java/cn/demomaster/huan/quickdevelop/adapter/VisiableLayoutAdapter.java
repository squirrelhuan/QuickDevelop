package cn.demomaster.huan.quickdevelop.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.demomaster.huan.quickdevelop.R;

/**
 * Created by amitshekhar on 14/01/17.
 */

public class VisiableLayoutAdapter extends RecyclerView.Adapter<VisiableLayoutAdapter.ViewHolder> {

    private List<String> data;
    private Context context;

    public VisiableLayoutAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    public void updateList(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_visiable_child, parent, false);
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

        TextView tv_title;
        TextView tv_message;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_message = itemView.findViewById(R.id.tv_message);
        }

        public void onBind(final int position) {
            String clazz = data.get(position);
            tv_title.setText(clazz);
            tv_title.setOnClickListener(v -> {
                if(tv_message.getVisibility()== View.VISIBLE){
                    tv_message.setVisibility(View.GONE);
                }else {
                    tv_message.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}