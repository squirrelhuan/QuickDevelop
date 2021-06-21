package cn.demomaster.huan.quickdeveloplibrary.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 动画工具类
 * Created by huan on 2017/10/10.
 */
public class AnimationUtil {

    public static class Builder {
        View mView;
        float startValue;
        float endValue;
        long duration = 200;
        MyAnimatorUpdateListener animatorUpdateListener;
        View.OnClickListener onClickListener;
        ListenerTriggerType listenerTriggerType = ListenerTriggerType.onAnimationFinished;

        public Builder() {
            animatorUpdateListener = new MyAnimatorUpdateListener() {
                @Override
                void onAnimationUpdate(View view, ValueAnimator animation) {
                    float curValue = (float) animation.getAnimatedValue();
                    view.setScaleX(curValue);
                    view.setScaleY(curValue);
                }
            };
        }

        public Builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setValue(float startValue, float endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
            return this;
        }

        public Builder setView(View view) {
            this.mView = view;
            return this;
        }

        public Builder setAnimatorUpdateListener(MyAnimatorUpdateListener animatorUpdateListener) {
            if (animatorUpdateListener != null) {
                this.animatorUpdateListener = animatorUpdateListener;
            }
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder setListenerTriggerType(ListenerTriggerType listenerTriggerType) {
            this.listenerTriggerType = listenerTriggerType;
            return this;
        }

        public ValueAnimator apply() {
            return addScaleAnimition(mView, startValue, endValue, duration, onClickListener, animatorUpdateListener, listenerTriggerType);
        }
    }

    public static ValueAnimator addScaleAnimition(final View targetView, final View.OnClickListener listener) {
        return new AnimationUtil.Builder().setView(targetView).setValue(1, 0.8f).setOnClickListener(listener).setDuration(200).setAnimatorUpdateListener(new MyAnimatorUpdateListener() {
            @Override
            void onAnimationUpdate(View view, ValueAnimator animation) {
                float curValue = (float) animation.getAnimatedValue();
                view.setScaleX(curValue);
                view.setScaleY(curValue);
            }
        }).apply();
    }

    /**
     * view按压动画效果
     *
     * @param targetView 要添加动画的目标view
     * @param listener   点击事件监听器，如果自身已经添加了点击事件可设置为null(这也是通用的参数)
     */
    public static ValueAnimator addScaleAnimition(View targetView, float startValue, float endValue, long duration, final View.OnClickListener listener, MyAnimatorUpdateListener animatorUpdateListener, ListenerTriggerType listenerTriggerType) {
        final int[] location = new int[2];
        targetView.getLocationOnScreen(location);

        MyValueAnimator animator = new MyValueAnimator();
        animator.setFloatValues(startValue, endValue);
        //final ValueAnimator animator = new ValueAnimator().ofFloat(1, 0.8f);
        animator.setRepeatCount(0);
        animator.setDuration(duration);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animatorUpdateListener.setView(targetView);
        animatorUpdateListener.setOnClickListener(listener);
        animatorUpdateListener.setStartValue(startValue);
        animatorUpdateListener.setEndValue(endValue);
        animatorUpdateListener.setValueAnimator(animator);
        animatorUpdateListener.setTriggerType(listenerTriggerType);
        animator.addUpdateListener(animatorUpdateListener);
       /* animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                QDLogger.e("onAnimationEnd="+animator.getAnimatedValue());
                //animatorUpdateListener.onAnimationUpdate(animator);
            }
        });*/
        //targetView.setOnClickListener(listener);
        targetView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {//按下
                    animator.start();
                    //animatorUpdateListener.onTouchDown();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {//移动
                    if (motionEvent.getX() < 0 || (motionEvent.getX() > targetView.getWidth()) || motionEvent.getY() < 0 || (motionEvent.getY() > targetView.getHeight())) {
                        animator.reverse();
                        return false;
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) { //抬起
                    if (motionEvent.getX() > 0 && motionEvent.getX() < targetView.getWidth()) {
                        //pointF.x = location[0]+x;
                        //pointF.y = location[1]+y;
                        if (y > 0 && motionEvent.getY() < targetView.getHeight()) {
                            animatorUpdateListener.doClick();
                            return false;
                        }
                    }
                    animator.reverse();
                    QDLogger.println("CGQ", "点击动画 无效点击");
                } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL || motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    animator.reverse();
                }
                return true;
            }
        });
        return animator;
    }

    /**
     * view按压动画效果
     *
     * @param targetView 要添加动画的目标view
     * @param listener   点击事件监听器，如果自身已经添加了点击事件可设置为null(这也是通用的参数)
     */
    public static ValueAnimator addScaleAnimition2(View targetView, final View.OnClickListener listener) {
        return new AnimationUtil.Builder().setView(targetView).setValue(1, 1.2f).setOnClickListener(listener).setDuration(200).setAnimatorUpdateListener(new MyAnimatorUpdateListener() {
            @Override
            void onAnimationUpdate(View view, ValueAnimator animation) {
                float curValue = (float) animation.getAnimatedValue();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                layoutParams.topMargin = (int) -(100 * (curValue - 1));
                view.setLayoutParams(layoutParams);
            }
        }).apply();
    }

    public static class MyValueAnimator extends ValueAnimator {
        boolean hasReversed = false;

        @Override
        public void reverse() {
            if (!hasReversed) {
                hasReversed = true;
                super.reverse();
            }
        }

        @Override
        public void start() {
            hasReversed = false;
            super.start();
        }

        @Override
        public void addListener(AnimatorListener listener) {
            super.addListener(listener);
        }
    }

    public static abstract class MyAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        View mView;
        ListenerTriggerType triggerType = ListenerTriggerType.onAnimationFinished;
        View.OnClickListener onClickListener;
        MyValueAnimator valueAnimator;

        float startValue;
        float endValue;

        public void setStartValue(float startValue) {
            this.startValue = startValue;
        }

        public void setEndValue(float endValue) {
            this.endValue = endValue;
        }

        public void setValueAnimator(MyValueAnimator valueAnimator) {
            this.valueAnimator = valueAnimator;
        }

        public void setTriggerType(ListenerTriggerType triggerType) {
            if (triggerType != null) {
                this.triggerType = triggerType;
            }
        }

        public void setView(View view) {
            this.mView = view;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        boolean canDoClickEvent;//是否可以执行点击事件
       /* public void onTouchDown(){
            canDoClickEvent = false;
        }*/

        /**
         * 手势触发点击效果
         */
        public void doClick() {
            switch (triggerType) {
                case None://无需判断当前动画状态，直接触发点击事件
                    triggerClick();
                    break;
                case onAnimationFinished://等待动画执行结束后，触发点击事件
                    if ((float) valueAnimator.getAnimatedValue() == endValue) {
                        triggerClick();
                    } else {
                        canDoClickEvent = true;
                    }
                    break;
                case onAnimationReverseed://等待动画回滚结束后，触发点击事件
                    if ((float) valueAnimator.getAnimatedValue() == startValue) {
                        triggerClick();
                    } else {
                        canDoClickEvent = true;
                    }
                    break;
            }
        }

        /**
         * 执行点击事件
         */
        private void triggerClick() {
            canDoClickEvent = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    valueAnimator.reverse();
                }
            }, 0);
            if (onClickListener != null) {
                View.OnClickListener ref = onClickListener;
                try {
                    Method method = ref.getClass().getMethod("onClick",
                            new Class[]{View.class});
                    QDLogger.println("反射方法:" + method.getName());
                    method.invoke(onClickListener,
                            new Object[]{mView});
                } catch (Exception e) {
                    QDLogger.e(e);
                }
            } else {
                mView.performClick();
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            onAnimationUpdate(mView, animation);
            if (canDoClickEvent) {
                switch (triggerType) {
                    case onAnimationFinished:
                        if ((float) animation.getAnimatedValue() == endValue) {
                            triggerClick();
                        }
                        break;
                    case onAnimationReverseed:
                        if ((float) animation.getAnimatedValue() == startValue) {
                            triggerClick();
                        }
                        break;
                }
            }
        }

        abstract void onAnimationUpdate(View view, ValueAnimator animation);
    }

    //触发listener类型
    public static enum ListenerTriggerType {
        onAnimationFinished,//当前动画结束后执行
        onAnimationReverseed,//关闭（回滚）动画结束后执行
        onAnimationStarted,//开启动画结束后执行
        None,//随机
    }

}

