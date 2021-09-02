package cn.demomaster.huan.quickdeveloplibrary.animation.tiny;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class TinyAnimation {

    public static void payRandomAnimition(View targetView) {
        if (targetView!=null&& targetView.getParent() instanceof View) {
            View parentView = (View) targetView.getParent();
            // 组合动画设置
            AnimationSet setAnimation = new AnimationSet(true);

            // 特别说明以下情况
            // 因为在下面的旋转动画设置了无限循环(RepeatCount = INFINITE)
            // 所以动画不会结束，而是无限循环
            // 所以组合动画的下面两行设置是无效的
            setAnimation.setRepeatMode(Animation.RESTART);
            setAnimation.setRepeatCount(1);// 设置了循环一次,但无效

            // 旋转动画
            Animation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(1000);
            rotate.setRepeatMode(Animation.RESTART);
            rotate.setRepeatCount(Animation.INFINITE);

            // 平移动画
            Animation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, -0.5f,
                    TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0
                    , TranslateAnimation.RELATIVE_TO_SELF, 0);
            translate.setDuration(10000);

            // 透明度动画
            Animation alpha = new AlphaAnimation(1, 0);
            alpha.setDuration(3000);
            alpha.setStartOffset(7000);

            // 缩放动画
            Animation scale1 = new ScaleAnimation(1, 0.5f, 1, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scale1.setDuration(1000);
            scale1.setStartOffset(4000);

            // 将创建的子动画添加到组合动画里
            setAnimation.addAnimation(alpha);
            setAnimation.addAnimation(rotate);
            setAnimation.addAnimation(translate);
            setAnimation.addAnimation(scale1);
            // 使用
            targetView.startAnimation(setAnimation);

        }
    }

}
