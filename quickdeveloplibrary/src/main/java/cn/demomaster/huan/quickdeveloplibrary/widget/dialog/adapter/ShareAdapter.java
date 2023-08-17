package cn.demomaster.huan.quickdeveloplibrary.widget.dialog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDTextView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.adapter.SheetAdapter.SheetViewHolder;

public class ShareAdapter extends SheetAdapter<ShareAdapter.ViewHolderItem2>{
    public ShareAdapter(Context context, List data) {
        super(context, data);
    }

    @NonNull
    @Override
    public ViewHolderItem2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.quick_simple_list_item_1,null);
        return new ViewHolderItem2(view);
    }

    public static class ViewHolderItem2 extends SheetViewHolder{
        ImageView iv_picture;
        TextView tv_text;

        public ViewHolderItem2(@NonNull View itemView) {
            super(itemView);
            iv_picture = itemView.findViewById(R.id.iv_picture);
            tv_text = itemView.findViewById(R.id.tv_text);
        }

        public void onbind(int position, List data) {
            if(data.get(position) instanceof ShareModel) {
                ShareModel shareModel = (ShareModel) data.get(position);
                tv_text.setText(shareModel.getName());
                Glide.with(context).load(shareModel.getImgResId()).into(iv_picture);
            }
        }
    }

    public static class ShareModel{
        private String name;
        private int imgResId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImgResId() {
            return imgResId;
        }

        public void setImgResId(int imgResId) {
            this.imgResId = imgResId;
        }
    }
}
