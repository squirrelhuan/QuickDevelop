package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.PreviewActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;

/**
 * Created by Squirrel桓 on 2018/12/1.
 */
public class SimplePictureGallery extends RecyclerView {
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


    private GridLayoutManager mLayoutManager;
    private PictureAdapter mAdapter;
    private int spanCount = 4;//横向排列个数
    private int imageCount = 5;//最大图片数量
    private boolean canPreview=true;//是否可以预览
    private List<Image> imageList = new ArrayList<>();

    public void init(final Context context) {
        mLayoutManager = new GridLayoutManager(context, spanCount);
        setLayoutManager(mLayoutManager);
        //mAdapter = new PictureAdapter(context, imageList, true,true);
        //setAdapter(mAdapter);

        mLayoutManager = new GridLayoutManager(context, spanCount);
        mAdapter = new PictureAdapter(context, imageList, true, true, imageCount);
        mAdapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Image image) {
                if (canPreview) {
                    Bundle bundle = new Bundle();
                    ArrayList<Image> images = new ArrayList<>();
                    images.add(image);
                    bundle.putSerializable("images",images);
                    bundle.putInt("imageIndex",0);
                    Intent intent = new Intent(context,PreviewActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onLastClick(View view) {
                ((BaseActivityParent) context).photoHelper.selectPhotoFromMyGallery(new PhotoHelper.OnSelectPictureResult() {
                    @Override
                    public void onSuccess(Intent data, ArrayList<Image> images) {
                        imageList.addAll(images);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String error) {

                    }

                    @Override
                    public int getImageCount() {
                        return imageCount - imageList.size();
                    }
                });
            }

            @Override
            public void onItemDelete(View view, int position, Image image) {
                imageList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });

        setAdapter(mAdapter);

    }

    public List<Image> getImages() {
        return imageList;
    }
}