package cn.demomaster.huan.quickdeveloplibrary.ui.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.QuickRecyclerAdapter;

/**
 * 文件适配器
 */
public class FileAdapter extends QuickRecyclerAdapter<FileAdapter.ViewHolder> {
    private final List<FileInfo> mFolders;
    private final LayoutInflater mInflater;
    private int mSelectItem;

    public FileAdapter(Context context, List<FileInfo> folders) {
        mFolders = folders;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FileInfo fileInfo = mFolders.get(position);
        //holder.iv_image.setVisibility(View.GONE);
        holder.iv_select.setVisibility(View.GONE);
        holder.tv_file_name.setText(fileInfo.file.getName());
        holder.iv_more.setVisibility(View.GONE);
        holder.iv_image.setImageResource(R.drawable.ic_file);
        if(fileInfo.file.exists()){
            if(fileInfo.file.isDirectory()){
                holder.iv_image.setImageResource(R.drawable.ic_file_package);
                holder.iv_more.setVisibility(View.VISIBLE);
            }
        }
       /* ArrayList<Image> images = folder.getImages();
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
        }*/

    }

    @Override
    public int getItemCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        ImageView iv_select;
        ImageView iv_more;
        TextView tv_file_name;
        TextView tv_file_size;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_more = itemView.findViewById(R.id.iv_more);
            iv_image = itemView.findViewById(R.id.iv_image);
            iv_select = itemView.findViewById(R.id.iv_select);
            tv_file_name = itemView.findViewById(R.id.tv_file_name);
            tv_file_size = itemView.findViewById(R.id.tv_file_size);
        }
    }

}
