package cn.demomaster.huan.quickdeveloplibrary.widget.slidingLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.QuickRecyclerAdapter;

public class ComponentAdapter extends QuickRecyclerAdapter<ComponentAdapter.ViewHolder> {

    private List<String> data;
    private final Context context;
    
    public ComponentAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void updateList(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quick_item_tab_menu, parent, false);
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
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
        public void onBind(final int position) {
            String clazz = data.get(position);
            tv_title.setText(clazz);

        }
    }
}