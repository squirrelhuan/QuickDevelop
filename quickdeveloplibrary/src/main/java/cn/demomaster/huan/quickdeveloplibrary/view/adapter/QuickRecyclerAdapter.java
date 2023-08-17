package cn.demomaster.huan.quickdeveloplibrary.view.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2022/2/25.
 * description：
 */
public abstract class QuickRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public AdapterView.OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public AdapterView.OnItemClickListener onItemClickListener;

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        //super.onBindViewHolder(holder,position);
        //onBindViewHolder(holder,position,null);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position, @NonNull @NotNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int p = holder.getBindingAdapterPosition();
                onItemClickListener.onItemClick(null, v, p, v.getId());
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                int p = holder.getBindingAdapterPosition();
                return onItemLongClickListener.onItemLongClick(null, v, p, v.getId());
            });
        }
    }

}
