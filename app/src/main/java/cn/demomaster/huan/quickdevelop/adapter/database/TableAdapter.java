package cn.demomaster.huan.quickdevelop.adapter.database;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private List<String[]> data;
    private Context context;

    public TableAdapter(Context context,TabAttrs tabAttrs) {
        this.context = context;
        data = new ArrayList<>();
        this.mTabAttrs = tabAttrs;
    }

    TabAttrs mTabAttrs;
    public void updateList(TabAttrs tabAttrs, List<String[]> data) {
        this.data.clear();
        this.data.addAll(data);
        this.mTabAttrs = tabAttrs;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        LinearLayout tableRow = new LinearLayout(context);
        tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        tableRow.setOrientation(LinearLayout.HORIZONTAL);
        return new ViewHolder(tableRow,mTabAttrs);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position,mTabAttrs);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TabAttrs mTabAttrs;
        public ViewHolder(View itemView,TabAttrs tabAttrs) {
            super(itemView);
            mTabAttrs = tabAttrs;
            resetChild();
        }

        public void resetChild() {
            ((ViewGroup) itemView).removeAllViews();
            for (int i = 0; i < mTabAttrs.getTabCount(); i++) {
                TextView textView = new TextView(itemView.getContext());
                textView.setPadding(20,20,20,20);
                ((ViewGroup) itemView).addView(textView,new ViewGroup.LayoutParams(mTabAttrs.getTabWidth(i), ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        public void onBind(final int position, TabAttrs tabAttrs) {
            mTabAttrs = tabAttrs;
            this.itemView.setBackgroundColor(position%2==0?Color.GRAY:Color.WHITE);
            if (mTabAttrs != null && ((ViewGroup) this.itemView).getChildCount() != mTabAttrs.getTabCount()) {
                resetChild();
            }

            for (int i = 0; i < mTabAttrs.getTabCount(); i++) {
                TextView view = (TextView) ((ViewGroup) this.itemView).getChildAt(i);
                view.setText(data.get(position)[i]);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = getAdapterPosition();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, p);
                    }
                }
            });
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}