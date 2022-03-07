package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.QuickRecyclerAdapter;

/**
 * Created by Squirrel桓 on 2018/11/11.
 */
public class OptionsMenuAdapter extends QuickRecyclerAdapter<OptionsMenuAdapter.ViewHolder> {

    private List<OptionsMenu.Menu> lists = new ArrayList<>();
    private Context context;

    public OptionsMenuAdapter(Context context, List<OptionsMenu.Menu> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_option_menu, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.tv_title.setText(lists.get(i).getTitle());
        Drawable drawable = null;
        if (lists.get(i).getIconId() != 0) {
            drawable = ResourcesCompat.getDrawable(context.getResources(),lists.get(i).getIconId(),null);
            if (lists.get(i).getIconWidth() != 0) {
                //设置大小，注意默认的是 px, UI 图上的 dp 单位需要转换
                drawable.setBounds(0, 0, lists.get(i).getIconWidth(), lists.get(i).getIconWidth());
                //给View左边添加一个图片
                viewHolder.tv_title.setCompoundDrawables(drawable, null, null, null);
            } else {
                viewHolder.tv_title.setCompoundDrawablesWithIntrinsicBounds(drawable,
                        null, null, null);
            }
        } else {
            viewHolder.tv_title.setCompoundDrawables(null, null, null, null);
        }
        if (lists.get(i).getIconPadding() != 0) {
            viewHolder.tv_title.setCompoundDrawablePadding(lists.get(i).getIconPadding());
        }
        //
        viewHolder.setTextColor(textColor);
        viewHolder.setTextGravity(textGravity);
        viewHolder.setTextSize(textSize);
        viewHolder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(viewHolder.getAdapterPosition(), lists.get(viewHolder.getAdapterPosition()));
            }
        });
    }

    private int textColor = Color.BLACK;

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        notifyDataSetChanged();
    }

    private int textGravity = Gravity.CENTER_VERTICAL;

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
        notifyDataSetChanged();
    }

    private int textSize = 16;

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;

        public void setTextColor(int textColor) {
            tv_title.setTextColor(textColor);
        }

        public void setTextGravity(int textGravity) {
            tv_title.setGravity(textGravity);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }

        public void setTextSize(int textSize) {
            tv_title.setTextSize(textSize);
        }
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    
    public interface OnItemClickListener {
        void onItemClick(int position, OptionsMenu.Menu menu);
    }
}

