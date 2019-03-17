package cn.demomaster.huan.quickdevelop.activity.sample;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.UrlType;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view.PictureAdapter;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view.SimplePictureGallery;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;

@ActivityPager(name = "PictureSelect",preViewClass = ToggleButton.class,resType = ResType.Custome)
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
        recyclerView.setCanPreview(true);
        recyclerView.getImages();
        SimplePictureGallery recyclerView2 = findViewById(R.id.ga_picture2);
        recyclerView2.setSpanCount(5);
        recyclerView2.setCanPreview(true);
        recyclerView2.setShowAdd(false);
        Image image = new Image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547548027518&di=ca4d2461d36c0bdb016ef8542f8f55a5&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201812%2F10%2F231855basa2bkay3yv3jsg.jpg",UrlType.url);
        recyclerView2.addImage(image);

        Image image2 = new Image("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1358989200,1538679988&fm=11&gp=0.jpg",UrlType.url);
        recyclerView2.addImage(image2);

        Image image3 = new Image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547549924091&di=34666e5915f8c54aea815b1d77d17b2d&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201105%2F31%2F20110531094303_d5JZB.jpg",UrlType.url);
        recyclerView2.addImage(image3);

        recyclerView2.getImages();
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
