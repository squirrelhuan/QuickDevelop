package cn.demomaster.huan.quickdeveloplibrary.util;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Method;

import cn.demomaster.qdlogger_library.QDLogger;

/** 全局动画工具类
 * Created by huan on 2017/10/10.
 */

public class AnimationUtil {

    public static boolean isClicked = true;//是否是点击
    //public static PointF pointF = new PointF(0,0);

    /**
     * view按压动画效果
     * @param targetView 要添加动画的目标view
     * @param listener 点击事件监听器，如果自身已经添加了点击事件可设置为null(这也是通用的参数)
     */
    public static ValueAnimator addScaleAnimition(final View targetView, final View.OnClickListener listener) {
        final int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        final ValueAnimator animator = new ValueAnimator().ofFloat(1, 0.8f);
        animator.setRepeatCount(0);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float curValue = (float) valueAnimator.getAnimatedValue();
                targetView.setScaleX(curValue);
                targetView.setScaleY(curValue);
            }
        });

        targetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {//按下
                    isClicked = true;
                    animator.start();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {//移动
                    if (motionEvent.getX() < 0 || (motionEvent.getX() > targetView.getWidth()) || motionEvent.getY() < 0 || (motionEvent.getY() > targetView.getHeight())) {
                        if (isClicked) {
                            animator.reverse();
                            isClicked = false;
                        }
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) { //抬起
                    if (isClicked) {
                        animator.reverse();
                    }
                    if (isClicked && motionEvent.getX() > 0 && motionEvent.getX() < targetView.getWidth()) {
                        //pointF.x = location[0]+x;
                        //pointF.y = location[1]+y;
                        if (y > 0&& motionEvent.getY() < targetView.getHeight()) {
                            //执行点击事件
                            //Log.i("CGQ", "event type = " + motionEvent.getAction());
                            if (listener != null) {
                                View.OnClickListener ref = listener;
                                try {
                                    Method method = ref.getClass().getMethod("onClick",
                                            new Class[]{View.class});
                                   QDLogger.println("反射方法名" + method.getName());
                                    method.invoke(listener,
                                            new Object[]{targetView});
                                } catch (Exception e) {
                                    QDLogger.e(e);
                                }
                            }else {
                                targetView.performClick();
                            }
                        }
                    } else {
                        Log.i("CGQ", "event type = 无效點擊");
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    if (isClicked) {
                        animator.reverse();
                        isClicked = false;
                    }
                }
                return true;
            }
        });
        return animator;
    }


}
