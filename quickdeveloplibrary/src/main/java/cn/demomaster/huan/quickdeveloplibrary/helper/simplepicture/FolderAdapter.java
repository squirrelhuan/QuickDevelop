package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Folder;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Folder> mFolders;
    private final LayoutInflater mInflater;
    private int mSelectItem;
    private OnFolderSelectListener mListener;

    public FolderAdapter(Context context, ArrayList<Folder> folders) {
        mContext = context;
        mFolders = folders;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Folder folder = mFolders.get(position);
        ArrayList<Image> images = folder.getImages();
        holder.tvFolderName.setText(folder.getName());
        holder.ivSelect.setVisibility(mSelectItem == position ? View.VISIBLE : View.GONE);
        if (images != null && !images.isEmpty()) {
            holder.tvFolderSize.setText(images.size() + "张");
            Glide.with(mContext).load(new File(images.get(0).getPath()))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(holder.ivImage);
        } else {
            holder.tvFolderSize.setText("0张");
            holder.ivImage.setImageBitmap(null);
        }

        holder.itemView.setOnClickListener(v -> {
            mSelectItem = holder.getAdapterPosition();
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.OnFolderSelect(folder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    public void setOnFolderSelectListener(OnFolderSelectListener listener) {
        this.mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivSelect;
        TextView tvFolderName;
        TextView tvFolderSize;
        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivSelect = itemView.findViewById(R.id.iv_select);
            tvFolderName = itemView.findViewById(R.id.tv_folder_name);
            tvFolderSize = itemView.findViewById(R.id.tv_folder_size);
        }
    }

    public interface OnFolderSelectListener {
        void OnFolderSelect(Folder folder);
    }

}
