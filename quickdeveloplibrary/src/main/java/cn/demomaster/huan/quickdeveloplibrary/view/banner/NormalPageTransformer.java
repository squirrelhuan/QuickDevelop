package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;
public class NormalPageTransformer implements ViewPager2.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        view.setScaleX(1);
        view.setScaleY(1);
        view.setAlpha(1);
    }

}
