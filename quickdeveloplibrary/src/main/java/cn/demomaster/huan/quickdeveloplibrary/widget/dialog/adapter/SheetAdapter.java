package cn.demomaster.huan.quickdeveloplibrary.widget.dialog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.QuickRecyclerAdapter;
import cn.demomaster.huan.quickdeveloplibrary.view.tabmenu.TabMenuAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDTextView;

public class SheetAdapter<V extends RecyclerView.ViewHolder> extends QuickRecyclerAdapter<SheetAdapter.ViewHolder> {
    public List data;
    public Context context;
  /*  private AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }*/

    public SheetAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public SheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = new FrameLayout(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new SheetAdapter.SheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SheetAdapter.ViewHolder vhItem, int i) {
        /*vhItem.itemView.setOnClickListener(v -> {
            int position = vhItem.getAdapterPosition();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, position);
            }
        });*/
        vhItem.onbind(i,data);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public interface ViewHolderInterface{
        void onbind(int position,List data);
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder implements ViewHolderInterface {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class SheetViewHolder extends ViewHolder {
        public Context context;
        QDTextView textView;

        public SheetViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            textView = new QDTextView(itemView.getContext());
            textView.setText("");
            int p = DisplayUtil.dip2px(itemView.getContext(), 15);
            textView.setPadding(p, p, p, p);
            textView.setTextSize(16);
            textView.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= 19) {
                textView.setBackgroundResource(R.drawable.ripple_bg);
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ((ViewGroup) itemView).addView(textView, layoutParams);
        }

        @Override
        public void onbind(int position, List data) {
            textView.setText(data.get(position) + "");
        }
    }
}
