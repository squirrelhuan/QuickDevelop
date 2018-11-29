package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.widget.SquareImageView;

/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class SimplePictureAdapter extends RecyclerView.Adapter<SimplePictureAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Image> mImages;
    private LayoutInflater mInflater;

    private int mMaxCount;
    private boolean isSingle;
    //保存选中的图片
    private ArrayList<Image> mSelectImages = new ArrayList<>();
    private boolean useCamera;
    private boolean isViewImage;

    /**
     * @param maxCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isSingle 是否单选
     */
    public SimplePictureAdapter(Context context, ArrayList<Image> mImages, int maxCount, boolean isSingle, boolean isViewImage) {
        this.mImages = mImages;
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mMaxCount = maxCount;
        this.isSingle = isSingle;
        this.isViewImage = isViewImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_images_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Image image = mImages.get(position);
        Glide.with(mContext).load(new File(image.getPath())).into(holder.iv_picture);
        //.diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.ivImage);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, (int) view.getTag());
            }
        });
        holder.iv_masking.setAlpha(0.2f);
        holder.iv_select.setTag(position);
        //点击选中/取消选中图片
        holder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedImage(holder, (int) v.getTag());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getAdapterPosition();
                if (isViewImage) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, useCamera ? p - 1 : p);
                    }
                } else {
                    checkedImage(holder, p);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        SquareImageView iv_picture;
        ImageView iv_select;
        SquareImageView iv_masking;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_picture = itemView.findViewById(R.id.iv_picture);
            iv_select = itemView.findViewById(R.id.iv_select);
            iv_masking = itemView.findViewById(R.id.iv_masking);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private List<ViewHolder> holders = new ArrayList<>();

    private void checkedImage(ViewHolder holder, int position) {
        Image image = getImage(position);
        image.setPosition(position);
        if (isSingle) {
            if (!mSelectImages.contains(image)) {
                //如果是单选，就先清空已经选中的图片，再选中当前图片
                clearImageSelect();
            }
            selectImage(image, holders.get(position));
            setItemSelect(holder, true);
        } else if (mMaxCount <= 0 || mSelectImages.size() < mMaxCount) {
            //如果不限制图片的选中数量，或者图片的选中数量
            // 还没有达到最大限制，就直接选中当前图片。
            selectImage(image,holders.get(position));
            setItemSelect(holder, true);
        }
    }

    private Image getImage(int position) {
        return mImages.get(useCamera ? position - 1 : position);
    }

    private void clearImageSelect() {
        mSelectImages.clear();
        for(ViewHolder viewHolder:holders){
            setItemSelect(viewHolder,false);
        }
    }

    /**
     * 选中图片
     */
    private void selectImage(Image image, ViewHolder viewHolder) {
        mSelectImages.add(image);
        holders.add(viewHolder);
    }

    /**
     * 取消选中图片
     */
    private void unSelectImage(Image image) {
        mSelectImages.remove(image);
    }

    /**
     * 设置图片选中和未选中的效果
     */
    private void setItemSelect(ViewHolder holder, boolean isSelect) {
        if (isSelect) {
            holder.iv_select.setImageResource(R.drawable.ic_check_box_black_24dp);
            holder.iv_masking.setAlpha(0.5f);
        } else {
            holder.iv_select.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
            holder.iv_masking.setAlpha(0.2f);
        }
    }

}