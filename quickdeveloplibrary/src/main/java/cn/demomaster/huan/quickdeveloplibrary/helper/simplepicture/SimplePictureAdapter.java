package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.widget.CircleTextView2;
import cn.demomaster.huan.quickdeveloplibrary.widget.square.SquareImageView;

/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class SimplePictureAdapter extends RecyclerView.Adapter<SimplePictureAdapter.ViewHolder> {

    private static Context mContext;
    private ArrayList<Image> mImages;
    private LayoutInflater mInflater;

    private static int mMaxCount;
    private THEME themeType = THEME.number;

    enum THEME {
        number,
        normal
    }

    //保存选中的图片
    private static LinkedHashMap<Integer, Image> map;
    private boolean useCamera;
    private boolean isViewImage;
    private RecyclerView mRecyclerView;

    public SimplePictureAdapter(Context context, ArrayList<Image> mImages, int maxCount, boolean isViewImage, RecyclerView recyclerView) {
        this.mImages = mImages;
        this.mContext = context.getApplicationContext();
        this.mInflater = LayoutInflater.from(mContext);
        this.mMaxCount = maxCount;
        this.isViewImage = isViewImage;
        this.mRecyclerView = recyclerView;
        map = new LinkedHashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_images_item, parent, false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Image image = mImages.get(position);
        holder.setImage(image);
        Glide.with(mContext).load(new File(image.getPath())).into(holder.iv_picture);
        //.diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.ivImage);
        holder.iv_masking.setAlpha(0.2f);
        holder.ct_select.setTag(position);
        //点击选中/取消选中图片
        holder.ct_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedImage(holder, (int) v.getTag());
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, (int) v.getTag(), map);
                }
            }
        });
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = holder.getAdapterPosition();
                if (isViewImage) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemPreview(v, p, mImages.get(p));
                    }
                } else {
                    checkedImage(holder, p);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, p, map);
                    }
                }
            }
        });
        holder.setCheckSelf();
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    public ArrayList<Image> getImages() {
        ArrayList<Image> images = new ArrayList<>();
        Object[] objects = map.entrySet().toArray();
        for (int i = 0; i < map.size(); i++) {
            Map.Entry entry = (Map.Entry) objects[i];
            images.add((Image) entry.getValue());
        }
        return images;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Image image;
        SquareImageView iv_picture;
        CircleTextView2 ct_select;
        SquareImageView iv_masking;

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            iv_picture = itemView.findViewById(R.id.iv_picture);
            ct_select = itemView.findViewById(R.id.ct_select);
            ct_select.setLine_width(2);
            ct_select.setBackgroundColor(context.getResources().getColor(R.color.transparent_light_33));
            ct_select.setUsePadding(true);
            iv_masking = itemView.findViewById(R.id.iv_masking);
        }

        public void setCheckSelf() {
            if (image != null) {
                refreshItemView(this, map.containsValue(image));
            } else {
                refreshItemView(this, false);
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position, Map<Integer, Image> map);

        void onItemPreview(View view, int position, Image image);
    }

    private void checkedImage(ViewHolder holder, int position) {
        if (map.containsKey(position)) {
            removeItem(holder, position);
        } else {
            if (mMaxCount <= 0 || map.size() < mMaxCount) {
                addItem(position, holder);
            } else {
                removeFirst();
                addItem(position, holder);
            }
        }
    }

    private void removeFirst() {
        Object[] objects = map.entrySet().toArray();
        Map.Entry entry = (Map.Entry) objects[0];
        //int i = (Integer) entry.getKey();
        map.remove(entry.getKey());
        refreshAllItems();
    }

    /**
     * 选中图片
     */
    private void addItem(int position, ViewHolder viewHolder) {
        map.put(position, mImages.get(position));
        refreshItemView(viewHolder, true);
    }

    private void refreshAllItems() {
        for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
            View view = mRecyclerView.getChildAt(i);
            if (null != mRecyclerView.getChildViewHolder(view)) {
                ViewHolder viewHolder = (ViewHolder) mRecyclerView.getChildViewHolder(view);
                viewHolder.setCheckSelf();
            }
        }
    }

    /**
     * 取消选中图片
     */
    private void removeItem(ViewHolder viewHolder, int position) {
        map.remove(position);
        refreshAllItems();
        //reSort();
        //pictureModels.remove(pictureModel);
    }

    /**
     * 设置图片选中和未选中的效果
     */
    private static void refreshItemView(ViewHolder holder, boolean isSelect) {
        if (holder != null) {
            if (isSelect) {
                if (mMaxCount == 1) {
                    holder.ct_select.setRound(false);
                    holder.ct_select.setDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                } else {
                    holder.ct_select.setRound(true);
                    holder.ct_select.setBackgroundColor(Color.BLUE);
                }
                holder.ct_select.setText(mMaxCount == 1 ? "" : (getSort(holder.getImage()) + 1) + "");
                holder.iv_masking.setAlpha(0.5f);
            } else {
                if (mMaxCount == 1) {
                    holder.ct_select.setRound(false);
                    holder.ct_select.setDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp));
                } else {
                    holder.ct_select.setRound(true);
                    holder.ct_select.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_light_33));
                }
                holder.ct_select.setText("");
                holder.iv_masking.setAlpha(0.2f);
            }
        }
    }

    private static int getSort(Image image) {
        Object[] objects = map.entrySet().toArray();
        //Map.Entry entry = (Map.Entry) objects[0];
        for (int i = 0; i < objects.length; i++) {
            if (((Image) (((Map.Entry) objects[i])).getValue()) == image) {
                return i;
            }
        }
        return 0;
    }

    public void refresh(ArrayList<Image> data, boolean useCamera) {
        mImages = data;
        this.useCamera = useCamera;
        map.clear();
        notifyDataSetChanged();
    }

}