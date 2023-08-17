package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.PreviewActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.UrlType;
import cn.demomaster.huan.quickdeveloplibrary.util.QDBitmapUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.ScrollRecyclerView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDSheetDialog;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2018/12/1.
 */
public class SimplePictureGallery extends ScrollRecyclerView  {
    public SimplePictureGallery(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public SimplePictureGallery(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SimplePictureGallery(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*if (spanCount > imageList.size()) {
            int width = getMySize(100, widthMeasureSpec);
            int height = getMySize(100, heightMeasureSpec);

            if (width < height) {
                height = width;
            } else {
                width = height;
            }
            setMeasuredDimension(width, height);
        }*/
    }

    private GridLayoutManager mLayoutManager;
    private PictureAdapter mAdapter;
    private int spanCount = 4;//横向排列个数
    private int maxCount = 5;//最大图片数量
    private boolean canPreview = true;//是否可以预览
    private boolean showAddButton = true;//是否可以添加
    private ArrayList<String> imgData = new ArrayList<>();;//要展示的图片
    private List<String> selectedPathList;//选中的图片
    private OnPictureDataChangeListener onPictureDataChangeListener;//选中元素变换监听
    private OnPictureViewClickListener onItemViewClickListener;//点击添加按钮事件

    public void setOnItemViewClickListener(OnPictureViewClickListener onItemViewClickListener) {
        if(this.onItemViewClickListener!=null){
            this.onItemViewClickListener.setPicGallery(null);
        }
        if(onItemViewClickListener!=null){
            onItemViewClickListener.setPicGallery(this);
        }
        this.onItemViewClickListener = onItemViewClickListener;
        mAdapter.setOnItemViewClickListener(this.onItemViewClickListener);
    }

    public void setOnPictureDataChangeListener(OnPictureDataChangeListener onPictureDataChangeListener) {
        this.onPictureDataChangeListener = onPictureDataChangeListener;
    }

    private boolean autoWidth = false;//宽度自适应

    public void setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
        updateLayoutManeger();
    }

    Fragment mFragment;

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
        photoHelper = null;
        getPhotoHelper();
    }

    boolean selectAble;//点击选中 和点击查看会有冲突
    public void setSelectAble(boolean selectAble) {
        this.selectAble = selectAble;
        if (canPreview) {
            this.canPreview = false;
            mAdapter.setCanPreview(this.canPreview);
        }
        mAdapter.setSelectAble(selectAble);
    }

    boolean clickCancelSelect;//是否可以取消选中
    public void setClickCancelSelect(boolean clickCancelSelect) {
        this.clickCancelSelect = clickCancelSelect;
        mAdapter.setClickCancelSelect(clickCancelSelect);
    }

    private int maxSelectCount = 1;//最大选中数量 默认单选1
    public void setMaxSelectCount(int maxSelectCount) {
        this.maxSelectCount = maxSelectCount;
        mAdapter.setMaxSelectCount(maxSelectCount);
    }

    public void setSelectedList(List<String> selectedList) {
        this.selectedPathList = selectedList;
        mAdapter.setSelectedList(selectedPathList);
    }

    public PhotoHelper photoHelper;
    public PhotoHelper getPhotoHelper() {
        if (photoHelper == null) {
            if (getContext() instanceof Activity) {
                photoHelper = new PhotoHelper((Activity) getContext(), mFragment, getContext().getPackageName() + ".fileprovider");
            }
        }
        return photoHelper;
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickView, defStyle, 0);
//        int borderWidth = typedArray.getDimensionPixelSize(R.styleable.QDButton_qd_borderWidth, 0);
//        typedArray.recycle();
        updateLayoutManeger();
        setItemMargin(itemMargin);
        mAdapter = new PictureAdapter(getContext(), imgData, showAddButton, maxCount, selectedPathList, maxSelectCount);
        if(onItemViewClickListener==null){
            onItemViewClickListener = new OnPictureViewClickListener() {};
            onItemViewClickListener.setPicGallery(this);
        }
        mAdapter.setOnItemViewClickListener(onItemViewClickListener);
        setAdapter(mAdapter);
    }

    private void updateLayoutManeger() {
        int spanCount_c = spanCount;
        if (autoWidth) {
            spanCount_c = Math.max(imgData.size() + (showAddButton ? 1 : 0), 1);
            //w = spanCount > (imageList.size() + (showAdd ? 1 : 0)) ? ((float) spanCount_c / spanCount) : 1;
//            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    if (widthMath == -1) {
//                        widthMath = getWidth();
//                    }
//                    setLayoutParams(new LinearLayout.LayoutParams((int) (widthMath * w), getHeight()));
//                }
//            });
            // setLayoutParams(new LinearLayout.LayoutParams((int) (getMeasuredWidth() * w), getMeasuredHeight()));
        }
        mLayoutManager = new QDGridLayoutManager(getContext(), spanCount_c);
        setLayoutManager(mLayoutManager);
    }

    String savePath = null;
    boolean isCrop = false;//是否截图
    public void setCrop(boolean crop) {
        isCrop = crop;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void showMenuDialog() {
        String[] menus = getResources().getStringArray(R.array.select_picture_items);
        new QDSheetDialog.MenuBuilder(getContext()).setData(menus).setOnDialogActionListener((dialog, position, data) -> {
            dialog.dismiss();
            if (position == 0) {
                getPhotoHelper().takePhoto(null, new PhotoHelper.OnTakePhotoResult() {
                    @Override
                    public void onSuccess(Intent data, String path) {
                        if (data == null) {
                            Image image = new Image(path, UrlType.file);
                            ArrayList list = new ArrayList<Image>() {
                            };
                            list.add(image);
                            addImageToView(list);
                        } else {
                            Bundle extras = data.getExtras();
                            if (extras != null) {
                                Bitmap bitmap = extras.getParcelable("data");
                                String filePath = QDBitmapUtil.savePhoto(bitmap, getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "temp", "header");//String.valueOf(System.currentTimeMillis())
                                imgData.add(filePath);
                                ArrayList list = new ArrayList<Image>();
                                list.add(filePath);
                                addImageToView(list);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
            } else {//从相册选择
                if (!isCrop) {
                    getPhotoHelper().selectPhotoFromMyGallery(new PhotoHelper.OnSelectPictureResult() {
                        @Override
                        public void onSuccess(Intent data, ArrayList<Image> images) {
                            ArrayList<String> imgPathList = new ArrayList<>();
                            for (Image image : images) {
                                imgPathList.add(image.getPath());
                            }
                            addImageToView(imgPathList);
                        }

                        @Override
                        public void onFailure(String error) {

                        }

                        @Override
                        public int getImageCount() {
                            return maxCount - imgData.size();
                        }
                    });
                } else {
                    File file = new File(savePath, System.currentTimeMillis() + ".jpg");
                    QDFileUtil.createFile(file);
                    getPhotoHelper().selectPhotoFromGalleryAndCrop(Uri.fromFile(file), new PhotoHelper.OnTakePhotoResult() {
                        @Override
                        public void onSuccess(Intent data, String path) {
                            Image image = new Image(path, UrlType.file);
                            ;
                            if (data.getData() != null) {
                                image.setPath(data.getData().getPath());
                            } else {
                                image.setPath(Uri.parse(data.getAction()).getPath());
                            }
                            ArrayList list = new ArrayList<Image>() {
                            };
                            list.add(image);
                            addImageToView(list);
                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });
                }
            }
        }).create().show();
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (photoHelper != null) {
            QDLogger.println("onResult requestCode=" + requestCode);
            photoHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 更新视图
     *
     * @param
     */
    private void addImageToView(ArrayList<String> images) {
        imgData.addAll(images);
        QDLogger.println("从相册选择 imageList size=" + imgData.size());
        if (onPictureDataChangeListener != null) {
            //onPictureChangeListener.onChanged(imageList);
            onPictureDataChangeListener.onAdd(images);
        }
        updateLayoutManeger();
        mAdapter.notifyDataSetChanged();//.notifyData();// setData(imageList);
    }

    /**
     * 更新视图
     *
     * @param
     */
    private void removeImageFromView(String path) {
        imgData.remove(path);
        updateLayoutManeger();
        mAdapter.notifyDataSetChanged();//.notifyData();

        if (onPictureDataChangeListener != null) {
            List list = new ArrayList();
            list.add(path);
            onPictureDataChangeListener.onRemove(list);
        }
    }

    private int itemMargin;
    public void setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
        addItemDecoration(new GridSpacesItemDecoration(itemMargin, true));
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        updateLayoutManeger();
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        mAdapter.setMaxCount(maxCount);
    }

    public boolean isCanPreview() {
        return canPreview;
    }

    public void setCanPreview(boolean canPreview) {
        this.canPreview = canPreview;
        if (selectAble) {
            this.selectAble = false;
        }
        mAdapter.setCanPreview(canPreview);
    }

    public boolean isShowAddButton() {
        return showAddButton;
    }

    public void setShowAddButton(boolean showAddButton) {
        this.showAddButton = showAddButton;
        mAdapter.setShowAddButton(showAddButton);
        mAdapter.notifyDataSetChanged();
    }

    public void setImgData(ArrayList<String> imgData) {
        this.imgData.clear();
        this.imgData.addAll(imgData);
        updateLayoutManeger();
        mAdapter.notifyDataSetChanged();
    }

    public void addImage(String image) {
        if (image != null) {
            imgData.add(image);
        }
        updateLayoutManeger();
        mAdapter.notifyDataSetChanged();
    }


    public interface OnPictureChangeListener {
        void onRemove(List<String> images);

        void onAdd(List<String> images);
        //void onChanged(List<Image> images);
        void onSelectdChanged(List<String> images);
    }

    public abstract static class OnPictureViewClickListener implements PictureAdapter.OnItemViewClickListener{
        SimplePictureGallery picGallery;

        public void setPicGallery(SimplePictureGallery picGallery) {
            this.picGallery = picGallery;
        }

        @Override
        public void onImageClick(View view, int position, String image) {
            if (picGallery.canPreview) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", picGallery.imgData);
                bundle.putInt("imageIndex", position);
                Intent intent = new Intent(picGallery.getContext(), PreviewActivity.class);
                intent.putExtras(bundle);
                picGallery.getContext().startActivity(intent);
            }
        }

        @Override
        public void onItemDelete(View view, int position, String path) {
            picGallery.removeImageFromView(path);
        }

        @Override
        public void onAddButtonClick(View view) {
            if (picGallery.isShowAddButton()) {
                picGallery.showMenuDialog();
            }
        }
    }

    public abstract static class OnPictureDataChangeListener implements OnPictureChangeListener{
        @Override
        public void onAdd(List<String> images) {

        }

        @Override
        public void onSelectdChanged(List<String> images) {

        }

        @Override
        public void onRemove(List<String> images) {

        }
    }
}
