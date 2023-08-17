package cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.model.Image;

/**
 *
 */
public class PreviewActivity extends QDActivity {
    //private PhotoView pv_image;
    private ViewPager2 vp_image;
    private FragmentManager mFragmentManager;
    private ImageFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        getActionBarTool().getRightView().setVisibility(View.GONE);
        //getActionBarTool().setActionBarType(ACTION_STACK_NO_STATUS);
        //getActionBarTool().setStateBarColorAuto(true);
        vp_image = findViewById(R.id.vp_image);
        //pv_image = (PhotoView) findViewById(R.id.pv_image);
        initV();
    }

    private void initV() {
        //pv_image = findViewById(R.id.pv_image);
        Bundle mBundle = getIntent().getExtras();
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
            QDLogger.e(e);
            }*/
        }
    }

    private ArrayList<Image> images;
    ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if(images!=null)
            getActionBarTool().setTitle(((position + 1) + "") + ("/" + images.size()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };
    public void initViewPager(final int index) {
        if (images == null || images.size() == 0) {
            return;
        }
        getActionBarTool().setTitle((index + 1) + "/" + images.size());
        mFragmentManager = getSupportFragmentManager();
        fragmentAdapter = new ImageFragmentAdapter(this, images);
        vp_image.unregisterOnPageChangeCallback(onPageChangeCallback);
        vp_image.registerOnPageChangeCallback(onPageChangeCallback);
        vp_image.setAdapter(fragmentAdapter);
        vp_image.setCurrentItem(index,false);
    }
}
