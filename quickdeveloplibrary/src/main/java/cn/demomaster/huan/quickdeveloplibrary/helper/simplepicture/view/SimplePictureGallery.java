package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.AttributeSet;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.constant.FilePath;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.PreviewActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.UrlType;
import cn.demomaster.huan.quickdeveloplibrary.util.ImageUitl;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollRecyclerView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;

/**
 * Created by Squirrel桓 on 2018/12/1.
 */
public class SimplePictureGallery extends ScrollRecyclerView {
    public SimplePictureGallery(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SimplePictureGallery(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimplePictureGallery(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private Context context;

    private GridLayoutManager mLayoutManager;
    private PictureAdapter mAdapter;
    private int spanCount = 4;//横向排列个数
    private int maxCount = 5;//最大图片数量
    private boolean canPreview=true;//是否可以预览
    private boolean showAdd=true;//是否可以添加
    private ArrayList<Image> imageList = new ArrayList<>();
    private OnPictureChangeListener onPictureChangeListener;

    public void setOnPictureChangeListener(OnPictureChangeListener onPictureChangeListener) {
        this.onPictureChangeListener = onPictureChangeListener;
    }

    public void init(final Context context) {
        this.context = context;
        mLayoutManager = new GridLayoutManager(context, spanCount);
        setLayoutManager(mLayoutManager);

        mLayoutManager = new GridLayoutManager(context, spanCount);
        mAdapter = new PictureAdapter(context, imageList, showAdd, true, maxCount);
        mAdapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Image image) {
                if (canPreview) {
                    Bundle bundle = new Bundle();
                    ArrayList<Image> images = new ArrayList<>();
                    images.add(image);
                    bundle.putSerializable("images", (Serializable) imageList);
                    bundle.putInt("imageIndex",position);
                    Intent intent = new Intent(context,PreviewActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onLastClick(View view) {
                showMenuDialog();
            }

            @Override
            public void onItemDelete(View view, int position, Image image) {
                imageList.remove(position);
                mAdapter.notifyDataSetChanged();
                if (onPictureChangeListener!=null){
                    onPictureChangeListener.onChanged(imageList);
                }
            }
        });
        setAdapter(mAdapter);
    }

    private void showMenuDialog() {
        String[] menus ={"拍摄","从相册选择"};
        new QDSheetDialog.MenuBuilder(context).setData(menus).setOnDialogActionListener(new QDSheetDialog.OnDialogActionListener() {
            @Override
            public void onItemClick(QDSheetDialog dialog, int position, List<String> data) {
                dialog.dismiss();
                if(position==0){
                    ((BaseActivityParent) context).photoHelper.takePhoto(new PhotoHelper.OnTakePhotoResult() {
                        @Override
                        public void onSuccess(Intent data, String path) {
                            Bundle extras = data.getExtras();
                            if (extras != null) {
                                Bitmap bitmap = extras.getParcelable("data");
                               String filePath = ImageUitl.savePhoto(bitmap, FilePath.APP_PATH_PICTURE, "header");//String.valueOf(System.currentTimeMillis())
                                imageList.add(new Image(filePath,UrlType.file));
                                mAdapter.notifyDataSetChanged();
                                if (onPictureChangeListener!=null){
                                    onPictureChangeListener.onChanged(imageList);
                                }
                            }
                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });
                }else {//从相册选择
                    ((BaseActivityParent) context).photoHelper.selectPhotoFromMyGallery(new PhotoHelper.OnSelectPictureResult() {
                        @Override
                        public void onSuccess(Intent data, ArrayList<Image> images) {
                            imageList.addAll(images);
                            mAdapter.notifyDataSetChanged();
                            if (onPictureChangeListener!=null){
                                onPictureChangeListener.onChanged(imageList);
                            }
                        }

                        @Override
                        public void onFailure(String error) {

                        }

                        @Override
                        public int getImageCount() {
                            return maxCount - imageList.size();
                        }
                    });
                }
            }
        }).create().show();
    }

    public List<Image> getImages() {
        return imageList;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        init(context);
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        init(context);
    }

    public boolean isCanPreview() {
        return canPreview;
    }

    public void setCanPreview(boolean canPreview) {
        this.canPreview = canPreview;
        init(context);
    }

    public boolean isShowAdd() {
        return showAdd;
    }

    public void setShowAdd(boolean showAdd) {
        this.showAdd = showAdd;
        init(context);
    }


    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<Image> imageList) {
        this.imageList.clear();
        this.imageList.addAll(imageList);
        if(mAdapter!=null&&imageList!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addImage(Image image){
        if(imageList!=null&&image!=null){
            imageList.add(image);
            mAdapter.notifyDataSetChanged();
        }
    }
    public void clearImages(){
        if(imageList!=null){
            imageList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    public static interface OnPictureChangeListener{
       void onChanged(List<Image> images);
    }
}
