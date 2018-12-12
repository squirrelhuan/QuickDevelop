package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.view.PreviewFragment;

import static cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout.ACTIONBAR_TYPE.ACTION_STACK_NO_STATUS;

public class PreviewActivity extends BaseActivityParent {


    //private PhotoView pv_image;
    private ViewPager vp_image;
    private FragmentManager mFragmentManager;
    private FragmentPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        getActionBarLayout().getRightView().setVisibility(View.GONE);
        getActionBarLayout().setActionBarModel(ACTION_STACK_NO_STATUS);
        getActionBarLayout().setStateBarColorAuto(true);
        vp_image = findViewById(R.id.vp_image);
        //pv_image = (PhotoView) findViewById(R.id.pv_image);
        initV();
    }


    private void initV() {
        //pv_image = findViewById(R.id.pv_image);
        if (mBundle != null && mBundle.containsKey("images")) {
            images = (ArrayList<Image>) mBundle.getSerializable("images");
            int index = mBundle.getInt("imageIndex", 0);
            initViewPager(index);
            //File file = new File(image.getPath());
            /*FileInputStream fs = null;
            try {
                fs = new FileInputStream(image.getPath());
                Bitmap bitmap = BitmapFactory.decodeStream(fs);
                Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                photo_view.setImageURI(imageUri);
                actionBarLayout.setStateBarColorAuto(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        }
    }

    private ArrayList<Image> images;

    public void initViewPager(final int index) {
        if (images == null || images.size() == 0) {
            return;
        }
        final int imageCount = images.size();
        getActionBarLayout().setTitle((index + 1) + "/" + images.size());
        mFragmentManager = getSupportFragmentManager();
        fragmentAdapter = new PictureFragmentAdapter(mFragmentManager, images);

        vp_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getActionBarLayout().refreshStateBarColor();
            }

            @Override
            public void onPageSelected(int position) {
                getActionBarLayout().setTitle(((position + 1) +"")+ ("/" + imageCount));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_image.setAdapter(fragmentAdapter);
        vp_image.setCurrentItem(index);
    }

    private class PictureFragmentAdapter extends FragmentPagerAdapter {

        private ArrayList<Image> images;

        public PictureFragmentAdapter(FragmentManager fm, ArrayList<Image> images) {
            super(fm);
            this.images = images;
        }

        @Override
        public Fragment getItem(int i) {
            PreviewFragment fragment = new PreviewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("image", images.get(i));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }
}
