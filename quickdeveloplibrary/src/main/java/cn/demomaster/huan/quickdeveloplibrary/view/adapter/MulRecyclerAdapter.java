package cn.demomaster.huan.quickdeveloplibrary.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 复合型recycler
 * @author squirrel桓
 * @date 2022/4/13.
 * description：
 */
public class MulRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends QuickRecyclerAdapter<VH> {

    Context context;
    List<MulItemRecycler> mulItemRecyclers;
    public MulRecyclerAdapter(Context context, List<MulItemRecycler> mulItemRecyclers) {
        this.context = context;
        this.mulItemRecyclers = mulItemRecyclers;
    }
    @Override
    public int getItemCount() {
        return mulItemRecyclers==null?0:mulItemRecyclers.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;// super.getItemViewType(position);
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return (VH) new ChildHolder(context,parent,mulItemRecyclers.get(viewType));
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        super.onBindViewHolder(holder, position);
        ((ChildHolder)holder).bind(holder.itemView);
    }
    
    public static class ChildHolder extends RecyclerView.ViewHolder {
        MulItemRecycler mulItemRecycler;
        Context context;
        public ChildHolder(Context context, ViewGroup parent, @NotNull MulItemRecycler mulItemRecycler) {
            super(mulItemRecycler.onCreateView(context,parent));
            this.context = context;
            this.mulItemRecycler = mulItemRecycler;
        }
        public void bind(View itemView) {
            mulItemRecycler.bindView(context,itemView);
        }
    }

    public static abstract class MulItemRecycler {
        public abstract View onCreateView(Context context,ViewGroup parent);
        public abstract void bindView(Context context,View itemView);
    }

}
