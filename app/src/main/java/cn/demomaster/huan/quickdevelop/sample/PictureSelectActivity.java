package cn.demomaster.huan.quickdevelop.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.PhotoHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view.PictureAdapter;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view.SimplePictureGallery;

public class PictureSelectActivity extends BaseActivityParent {


    private GridLayoutManager mLayoutManager;
    private PictureAdapter mAdapter;
    private List<Image> imageList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

        SimplePictureGallery recyclerView = findViewById(R.id.ga_picture);
        recyclerView.setMaxCount(13);
        recyclerView.setSpanCount(3);
        recyclerView.setCanPreview(false);
        recyclerView.getImages();
        /*mLayoutManager = new GridLayoutManager(mContext, 4);
        mAdapter = new PictureAdapter(mContext, imageList, true,true);
        mAdapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onItemPreview(View view, int position, Image image) {

            }

            @Override
            public PhotoHelper.OnSelectPictureResult onReciveImages() {
                return new PhotoHelper.OnSelectPictureResult() {
                    @Override
                    public void onSuccess(Intent data, ArrayList<Image> images) {
                        imageList.addAll(images);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                };
            }
        });
        recyclerView.setAdapter(mAdapter);*/

    }
}
