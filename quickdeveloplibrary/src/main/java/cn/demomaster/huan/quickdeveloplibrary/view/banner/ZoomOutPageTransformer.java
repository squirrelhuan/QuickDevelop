package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
    private static final float MAX_SCALE = 1f;
    private static final float MAX_SCALE_X = 1f;
    private static final float MAX_SCALE_Y = 1f;
    private static final float MIN_SCALE = 0.9f;
    private static final float MAX_ALPHA = 1.0F;
    private static final float MIN_ALPHA = 0.9F;

    @Override
    public void transformPage(View view, float position) {
        if (position < -1) {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        } else if (position <= 1) {
            float scaleFactorX = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE_X - MIN_SCALE);
            float scaleFactorY = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE_Y - MIN_SCALE);
            view.setScaleX(scaleFactorX);
            if (position > 0) { //(0,1]
                view.setTranslationX(-scaleFactorX * 2);
            } else if (position < 0) { //[-1,0)
                view.setTranslationX(scaleFactorX * 2);
            }
            view.setScaleY(scaleFactorY);
        } else {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        }

        if (position < -1 || position > 1) {
            view.setAlpha(MIN_ALPHA);
        } else {
            //不透明->半透明
            if (position < 0) {//[0,-1]
                view.setAlpha(MIN_ALPHA + (1 + position) * (1 - MIN_ALPHA));
            } else {//[1,0]
                //半透明->不透明
                view.setAlpha(MIN_ALPHA + (1 - position) * (1 - MIN_ALPHA));
            }
        }


    }

}
