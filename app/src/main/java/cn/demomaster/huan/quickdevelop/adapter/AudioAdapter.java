package cn.demomaster.huan.quickdevelop.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    private List<String> appList;
    private final Context context;

    public AudioAdapter(Context context) {
        this.context = context;
        appList = new ArrayList<>();
    }

    public void updateList(List<String> appList) {
        this.appList = appList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_audio_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_size;
        ImageView iv_play,iv_delete;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title =itemView.findViewById(R.id.tv_title);
            tv_size =itemView.findViewById(R.id.tv_size);
            iv_play =itemView.findViewById(R.id.iv_play);
            iv_delete =itemView.findViewById(R.id.iv_delete);
        }

        public void onBind(final int position) {
            if(appList!=null&&position<appList.size()) {
                tv_title.setText(appList.get(position));
                iv_play.setOnClickListener(v -> {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,position);
                    }
                });
                iv_delete.setOnClickListener(v -> {
                    if(onItemClickListener!=null){
                        onItemClickListener.onDelete(v,position);
                    }
                });
                File file = new File(tv_size.getContext().getFilesDir().getAbsolutePath()+"/wav"+"/"+appList.get(position));
                tv_size.setText(FormetFileSize(file.length()));
            }
        }
    }

    public String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemPreview(View view, int position,Image image);
        void onDelete(View view, int position);
    }

}