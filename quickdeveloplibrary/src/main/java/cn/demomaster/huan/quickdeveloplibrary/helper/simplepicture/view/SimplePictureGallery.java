package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.SimplePictureAdapter;
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
    //private PictureAdapter mAdapter;
    private int spanCount =4;
    private List<Image> imageList;
    public void init( Context context){
        mLayoutManager = new GridLayoutManager(context, spanCount);
        setLayoutManager(mLayoutManager);
        //mAdapter = new PictureAdapter(context, imageList, true,true);
        //setAdapter(mAdapter);
    }



}
