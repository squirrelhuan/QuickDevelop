package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
    private static final float SCALE_X = .05f;
    private static final float SCALE_Y = .05f;
    private static final float MIN_SCALE = 0.9f;
    private static final float MAX_ALPHA = 1.0F;
    private static final float MIN_ALPHA = 0.9F;

    @Override
    public void transformPage(View view, float position) {

        if (position < 0) {
            view.setScaleX(1+SCALE_X*position);
            view.setScaleY(1+SCALE_Y*position);
        }else if (position > 0) {
            view.setScaleX(1-SCALE_X*position);
            view.setScaleY(1-SCALE_Y*position);
        } else {
            view.setScaleX(1);
            view.setScaleY(1);
        }
        
           /* float scaleFactorX = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE_X - MIN_SCALE);
            float scaleFactorY = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE_Y - MIN_SCALE);
            view.setScaleX(scaleFactorX);
            if (position > 0) { //(0,1]
                view.setTranslationX(-scaleFactorX * 2);
            } else if (position < 0) { //[-1,0)
                view.setTranslationX(scaleFactorX * 2);
            }
            view.setScaleY(scaleFactorY);*/
/*

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
        }*/

    }

}
