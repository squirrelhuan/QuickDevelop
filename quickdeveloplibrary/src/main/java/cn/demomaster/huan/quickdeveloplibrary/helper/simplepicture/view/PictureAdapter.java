package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import static cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil.isHttpUrl;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.util.StringUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.QuickRecyclerAdapter;
import cn.demomaster.huan.quickdeveloplibrary.widget.QuickImageView;
import cn.demomaster.huan.quickdeveloplibrary.widget.radiobutton.QuickRadioButtonLayout;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2018/11/9.
 * description：
 */
// ① 创建Adapter
public class PictureAdapter extends QuickRecyclerAdapter<PictureAdapter.ViewHolder_Add> {

    private final Context mContext;
    private List<String> data;
    //保存选中的图片
    private LinkedHashMap<String, String> selectedPathMap;
    private final LayoutInflater mInflater;

    private boolean showAddButton;//是否展示添加按钮
    private int maxCount;//最大展示数量
    private int maxSelectCount = 1;//最大选中数量 默认单选1
    boolean selectAble;//可点击选中 和点击查看会有冲突
    boolean canPreview;//是否支持点击预览 和点击选择冲突
    boolean clickCancelSelect = true;//是否可以取消选中

    // private boolean saturated = false;//饱和（图片数量达到上限）
    private MODEL modelType = MODEL.normal;//状态

    public enum MODEL {
        edit,
        normal
    }

    public PictureAdapter(Context context, List<String> mImages, boolean showAddButton, int maxCount, List<String> selectedList, int maxSelectCount) {
        this.data = mImages;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.showAddButton = showAddButton;
        this.maxCount = maxCount;
        this.selectedPathMap = new LinkedHashMap<>();
        if (selectedList != null) {
            for (String path : selectedList) {
                selectedPathMap.put(path, path);
            }
        }
        this.maxSelectCount = maxSelectCount;
    }

    public void setClickCancelSelect(boolean clickCancelSelect) {
        this.clickCancelSelect = clickCancelSelect;
    }

    public void setMaxSelectCount(int maxSelectCount) {
        this.maxSelectCount = maxSelectCount;
    }

    public void setSelectedList(List<String> selectedList) {
        this.selectedPathMap.clear();
        if (selectedList != null) {
            for (String path : selectedList) {
                selectedPathMap.put(path, path);
            }
        }
    }

    public void setCanPreview(boolean canPreview) {
        this.canPreview = canPreview;
        notifyDataSetChanged();
    }

    public MODEL getModelType() {
        return modelType;
    }

//    public void setData(List<Image> imageList) {
//        this.mImages.clear();
//        this.mImages.addAll(imageList);
//        notifyData();
//    }


    public void setSelectAble(boolean selectAble) {
        this.selectAble = selectAble;
        notifyDataSetChanged();
    }

    public void setModelType(MODEL modelType) {
        this.modelType = modelType;
        notifyDataSetChanged();
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setShowAddButton(boolean showAddButton) {
        this.showAddButton = showAddButton;
    }

    @Override
    public int getItemViewType(int position) {
        boolean saturated = (maxCount <= data.size());
        if (showAddButton && !saturated && position == getItemCount() - 1) {//开始添加按钮 且未饱和 且是最后一个
            return 3;
        }
        return 0;
    }

    @Override
    public ViewHolder_Add onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 3) {
            view = mInflater.inflate(R.layout.item_images_weight_add, parent, false);
            return new ViewHolder_Add(view);
        } else {
            view = mInflater.inflate(R.layout.item_images_weight, parent, false);
            return new ViewHolder_Img(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder_Add holder, final int position) {
        //点击选中/取消选中图片
        holder.iv_picture.setOnLongClickListener(view -> {
            setModelType(MODEL.edit);
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            long[] patter = {100, 50, 50};
            vibrator.vibrate(patter, -1);
            return true;
        });
        if (holder instanceof ViewHolder_Img) {
            ViewHolder_Img holder2 = (ViewHolder_Img) holder;
            if (position <= data.size() - 1) {
                String imagePath = data.get(position);
                if (isHttpUrl(imagePath)) {
                    Glide.with(mContext).load(imagePath).into(holder.iv_picture);
                } else {
                    Glide.with(mContext).load(new File(imagePath)).into(holder.iv_picture);
                }
                //.diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.ivImage);
                if (modelType == MODEL.edit) {
                    holder2.iv_delete.setVisibility(View.VISIBLE);
                } else {
                    holder2.iv_delete.setVisibility(View.GONE);
                }
                holder2.iv_delete.setTag(position);
                holder2.iv_delete.setOnClickListener(view -> {
                    if (onItemViewClickListener != null) {
                        int p = (Integer) view.getTag();
                        onItemViewClickListener.onItemDelete(view, p, data.get(p));
                        //onItemViewClickListener.onItemDelete(null, view, (Integer) view.getTag(), view.getId());
                    }
                });
                holder.iv_picture.setTag(position);
                holder.iv_picture.setOnClickListener(v -> {
                    ((View) v.getParent()).findViewById(R.id.iv_delete).setVisibility(View.GONE);
                    setModelType(MODEL.normal);
                    int p = (Integer) v.getTag();
                    String imgPath = data.get(p);
                    if (onItemViewClickListener != null) {
                        onItemViewClickListener.onImageClick(v, p, data.get(p));
                    }
                    if (selectAble) {//处理点击选择
                        if (selectedPathMap.containsKey(imgPath)) {
                            if (clickCancelSelect) {
                                selectedPathMap.remove(imgPath);
                            }
                        } else {
                            if (selectedPathMap.size() >= maxSelectCount) {//如果超过数量限制移除第一个
                                Map.Entry<String, String> firstEntry = selectedPathMap.entrySet().iterator().next();
                                selectedPathMap.remove(firstEntry.getKey());
                            }
                            selectedPathMap.put(imgPath, imgPath);
                        }

                        if (onItemSelectChangeListener != null) {
                            List<String> stringList = StringUtil.StringSet2Array(selectedPathMap.keySet());
                            onItemSelectChangeListener.onSelectdChanged(stringList);
                        }
                    }
                });
                holder2.iv_select.setVisibility(selectAble ? View.VISIBLE : View.GONE);
                if (selectAble) {
                    if (selectedPathMap.containsKey(data.get(position))) {
                        holder2.iv_select.setBackground(mContext.getResources().getDrawable(R.drawable.ic_selected));
                    } else {
                        holder2.iv_select.setBackground(mContext.getResources().getDrawable(R.drawable.ic_un_selected));
                    }
                }
            }
        } else if (holder instanceof ViewHolder_Add) {
            holder.iv_picture.setTag(position);
            holder.iv_picture.setOnClickListener(v -> {
                if (onItemViewClickListener != null) {
                    onItemViewClickListener.onAddButtonClick(v);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        boolean saturated = (maxCount <= data.size());
        int a = (showAddButton && !saturated) ? 1 : 0;//未达到指定数量
        return a + data.size();
    }

    public class ViewHolder_Img extends ViewHolder_Add {
        ImageView iv_delete;
        ImageView iv_select;

        public ViewHolder_Img(View itemView) {
            super(itemView);
            iv_picture = itemView.findViewById(R.id.iv_picture);
            iv_select = itemView.findViewById(R.id.iv_select);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }

    public class ViewHolder_Add extends RecyclerView.ViewHolder {
        QuickImageView iv_picture;

        public ViewHolder_Add(View itemView) {
            super(itemView);
            iv_picture = itemView.findViewById(R.id.iv_picture);
        }
    }

    private OnItemViewClickListener onItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    public interface OnItemViewClickListener {
        //void onItemLongClick(View view, int position, Map<Integer, Image> map);
        void onImageClick(View view, int position, String image);
        //void onItemClick(View view, int position, String image);
        void onItemDelete(View view, int position, String image);
        void onAddButtonClick(View view);
    }
    OnItemSelectChangeListener onItemSelectChangeListener;
    public interface OnItemSelectChangeListener {
        void onSelectdChanged(List<String> images);
    }
//    public void notifyData() {
//        List<Image> list = new ArrayList();
//        list.addAll(mImages);
//        mImages.clear();
//        mImages.addAll(list);
//        notifyDataSetChanged();
//    }

}