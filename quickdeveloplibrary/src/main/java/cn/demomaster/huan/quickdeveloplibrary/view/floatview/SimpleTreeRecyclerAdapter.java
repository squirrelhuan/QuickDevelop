package cn.demomaster.huan.quickdeveloplibrary.view.floatview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.treeviewlibrary.Node;
import cn.demomaster.treeviewlibrary.TreeRecyclerAdapter;

/**
 * Created by zhangke on 2017-1-14.
 */
public class SimpleTreeRecyclerAdapter extends TreeRecyclerAdapter {
  
    public SimpleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
    }

    public SimpleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas, int defaultExpandLevel) {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHoder(View.inflate(mContext, R.layout.treelist_item,null));
    }

    @Override
    public void onBindViewHolder(final Node node, RecyclerView.ViewHolder holder, int position) {
        final MyHoder viewHolder = (MyHoder) holder;
        //todo do something
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(node,viewHolder.cb.isChecked());
            }
        });

        if (node.isChecked()){
            viewHolder.cb.setChecked(true);
        }else {
            viewHolder.cb.setChecked(false);
        }
        
        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
        viewHolder.label.setText(node.getName());
        holder.itemView.setPadding(node.getLevel() * 10, 3, 3, 3);
    }

    static class MyHoder extends RecyclerView.ViewHolder{
        public CheckBox cb;
        public TextView label;
        public ImageView icon;
        public MyHoder(View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb_select_tree);
            label = itemView.findViewById(R.id.id_treenode_label);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
