package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.content.Context;
import android.os.Vibrator;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.UrlType;
import cn.demomaster.huan.quickdeveloplibrary.widget.SquareImageView;

/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private Context mContext;
    private List<Image> mImages;
    private LayoutInflater mInflater;

    //保存选中的图片
    private LinkedHashMap<Integer, Image> map;
    private boolean showAdd;
    private boolean isViewImage;
    private int maxCount;

    private boolean saturated = false;//饱和（图片数量达到上限）
    private MODEL modelType = MODEL.normal;//状态

    public static enum MODEL {
        edit,
        normal
    }

    public MODEL getModelType() {
        return modelType;
    }

    public void setModelType(MODEL modelType) {
        if (this.modelType != modelType) {
            this.modelType = modelType;
            notifyDataSetChanged();
        }
    }

    public PictureAdapter(Context context, List<Image> mImages, boolean showAdd, boolean isViewImage, int maxCount) {
        this.mImages = mImages;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.isViewImage = isViewImage;
        this.showAdd = showAdd;
        this.maxCount = maxCount;
        this.saturated = (maxCount <= mImages.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (!showAdd) {
            return 0;
        }
        if (saturated) {//饱和了去掉加号
            return 0;
        } else {
            if (position == getItemCount() - 1) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_images_weight, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder != null) {
            if (holder.getType() == 0) {
                Image image = mImages.get(position);
                holder.setImage(image);
                if (image.getUrlType() == UrlType.url) {
                    Glide.with(mContext).load(image.getPath()).into(holder.iv_picture);
                } else if (image.getUrlType() == UrlType.file) {
                    Glide.with(mContext).load(new File(image.getPath())).into(holder.iv_picture);
                }
                //.diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.ivImage);
                if (modelType == MODEL.edit) {
                    holder.iv_delete.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_delete.setVisibility(View.GONE);
                }
                holder.iv_delete.setTag(position);
                holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            int p = holder.getAdapterPosition();
                            onItemClickListener.onItemDelete(view, p, mImages.get(p));
                        }
                    }
                });
                holder.itemView.setTag(position);
                //点击选中/取消选中图片
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int p = holder.getAdapterPosition();
                        setModelType(MODEL.edit);
                        Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
                        long[] patter = {100, 50, 50};
                        vibrator.vibrate(patter, -1);
                        return true;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((View) v.getParent()).findViewById(R.id.iv_delete).setVisibility(View.GONE);
                        int p = holder.getAdapterPosition();
                        setModelType(MODEL.normal);
                        if (isViewImage) {
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(v, p, mImages.get(p));
                            }
                        }
                    }
                });
                ((ViewHolder) holder).rmovePadding();
            } else {
                ((ViewHolder) holder).setAddButton(addButtonPadding);
            }
        }
    }

    @Override
    public int getItemCount() {
        this.saturated = (maxCount <= mImages.size());
        int a = (showAdd && !saturated ? 1 : 0);
        return a + (mImages == null ? 0 : mImages.size());

    }
    private int addButtonPadding;

    public void setAddButtonPadding(int addButtonPadding) {
        this.addButtonPadding = addButtonPadding;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Image image;
        private int type;
        SquareImageView iv_picture;
        ImageView iv_delete;

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ViewHolder(View itemView, int type) {
            super(itemView);
            this.type = type;
            iv_picture = itemView.findViewById(R.id.iv_picture);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            if (type == -1) {
                setAddButton(addButtonPadding);
            }
        }
        private void rmovePadding( ) {
            iv_picture.setPadding(0,0,0,0);
        }
        private void setAddButton(int p ) {
            iv_picture.setPadding(p,p,p,p);
            iv_picture.setImageResource(R.drawable.ic_add_black_24dp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setModelType(MODEL.normal);
                    if (onItemClickListener != null) {
                        onItemClickListener.onLastClick(view);
                    }
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        //void onItemLongClick(View view, int position, Map<Integer, Image> map);

        void onItemClick(View view, int position, Image image);

        void onItemDelete(View view, int position, Image image);

        void onLastClick(View view);
    }


    public void refresh(ArrayList<Image> data, boolean showAdd) {
        mImages = data;
        this.showAdd = showAdd;
        map.clear();
        notifyDataSetChanged();
    }

}