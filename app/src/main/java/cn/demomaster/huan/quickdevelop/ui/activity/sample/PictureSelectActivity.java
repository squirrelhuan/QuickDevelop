package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.UrlType;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view.PictureAdapter;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view.SimplePictureGallery;

@ActivityPager(name = "图片选择器",preViewClass = TextView.class,resType = ResType.Resource,iconRes = R.drawable.ic_gallery_selector)
public class PictureSelectActivity extends BaseActivity {

    private GridLayoutManager mLayoutManager;
    private PictureAdapter mAdapter;
    private List<Image> imageList=new ArrayList<>();
    SimplePictureGallery simplePictureGallery,recyclerView2,recyclerView3,recyclerView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

        simplePictureGallery = findViewById(R.id.ga_picture);
        simplePictureGallery.setMaxCount(13);
        simplePictureGallery.setSpanCount(3);
        simplePictureGallery.setCanPreview(true);
        simplePictureGallery.setItemMargin(20);
        recyclerView2 = findViewById(R.id.ga_picture2);
        recyclerView2.setSpanCount(5);
        recyclerView2.setCanPreview(true);
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add("https://img2.baidu.com/it/u=345670089,3951600800&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        imgs.add("https://img1.baidu.com/it/u=1954533644,1324198317&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=500");
        imgs.add("https://lmg.jj20.com/up/allimg/tp05/1Z9291S4041415-0-lp.jpg");
        recyclerView2.setImgData(imgs);
        recyclerView2.setShowAddButton(false);

        recyclerView3 = findViewById(R.id.ga_picture3);
        recyclerView3.setMaxCount(13);
        recyclerView3.setSpanCount(3);
        recyclerView3.setCanPreview(true);
        recyclerView3.setAutoWidth(true);
        recyclerView4 = findViewById(R.id.ga_picture4);
        recyclerView4.setSpanCount(5);
        recyclerView4.setCanPreview(true);
        recyclerView4.setShowAddButton(true);
        ArrayList<String> imgs4 = new ArrayList<>();
        imgs4.add("https://img1.baidu.com/it/u=7456317,4282812646&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        imgs4.add("https://img2.baidu.com/it/u=1792249350,650626052&fm=253&fmt=auto&app=120&f=JPEG?w=1200&h=675");
        imgs4.add("https://img0.baidu.com/it/u=3182669744,3015526205&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500");
        recyclerView4.setImgData(imgs4);
        recyclerView4.setAutoWidth(true);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        simplePictureGallery.onResult(requestCode, resultCode, data);
//        recyclerView2.onResult(requestCode, resultCode, data);
//        recyclerView3.onResult(requestCode, resultCode, data);
//        recyclerView4.onResult(requestCode, resultCode, data);
    }
}
