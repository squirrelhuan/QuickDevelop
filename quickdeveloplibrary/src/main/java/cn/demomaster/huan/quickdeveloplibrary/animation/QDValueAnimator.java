package cn.demomaster.huan.quickdeveloplibrary.animation;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

public class QDValueAnimator extends ValueAnimator {

    public static enum AnimationState {
        idle,
        isOpened,
        isOpening,
        isClosed,
        isColosing,

        isOpenStart,
        isCloseStart,
    }

    boolean hasReversed = false;

    public boolean isHasReversed() {
        return hasReversed;
    }

    private Object tag;
    private AnimationState animationState = AnimationState.idle;
    private int currentIndex;//当前执行次数
    public QDValueAnimator(Class dataType) {
        this.dataType = dataType;
        if (animatorUpdateListener == null) {
            animatorUpdateListener = new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    value = animation.getAnimatedValue();
                    if (!isReversing(ValueAnimator.class, animation)) {
                        onOpening(value);
                    } else {
                        onClosing(value);
                    }
                    lastValue = value;
                }
            };
            addUpdateListener(animatorUpdateListener);
            if (mAnimatorListener == null) {
                addListener(new AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        for (AnimatorListener animatorListener : listeners) {
                            animatorListener.onAnimationStart(animation);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        for (AnimatorListener animatorListener : listeners) {
                            animatorListener.onAnimationEnd(animation);
                        }
                        if (isReversing(ValueAnimator.class, animation)) {
                            onEndClose();
                        } else {
                            onEndOpen();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        for (AnimatorListener animatorListener : listeners) {
                            animatorListener.onAnimationCancel(animation);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        for (AnimatorListener animatorListener : listeners) {
                            animatorListener.onAnimationRepeat(animation);
                        }
                    }
                });
            }
        }
    }

    boolean isReversing() {
        return isReversing(ValueAnimator.class, this);
    }

    boolean isReversing(Class clazz, Animator animator) {
        Field[] fields = clazz.getDeclaredFields();
        // 暴力反射获取属性
       // Field filed = class1.getDeclaredField("name");
        if (fields != null) {
            QDLogger.println(clazz.getName()+":fields.length =" + fields.length);
            for (int i = 0, len = fields.length; i < len; i++) {
                String varName = fields[i].getName();
                QDLogger.println("varName =" + varName);
                if (varName.equals("mReversing")) {
                    try {
                        boolean accessFlag = fields[i].isAccessible();
                        fields[i].setAccessible(true);
                        Object o = fields[i].get(animator);
                        fields[i].setAccessible(accessFlag);
                        QDLogger.println("isReversing =" + o);
                        return (boolean) o;
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        QDLogger.e(ex);
                    }
                }
            }
        }
        return false;
    }

    AnimatorListener mAnimatorListener;
    List<AnimatorListener> listeners;

    @Override
    public void addListener(AnimatorListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        if (mAnimatorListener == null) {
            mAnimatorListener = listener;
        } else {
            listeners.add(listener);
        }
        super.addListener(mAnimatorListener);
    }

    public Class dataType;

    public static QDValueAnimator ofFloat(float... values) {
        QDValueAnimator anim = new QDValueAnimator(Float.class);
        anim.setFloatValues(values);
        return anim;
    }

    public static QDValueAnimator ofInt(int... values) {
        QDValueAnimator anim = new QDValueAnimator(Integer.class);
        anim.setIntValues(values);
        return anim;
    }

    @Override
    public void setIntValues(int... values) {
        super.setIntValues(values);
        startValue = values[0];
        endValue = values[values.length - 1];
    }

    Object value;
    Object lastValue;

    public AnimationState getAnimationState() {
        return animationState;
    }

    @Override
    public void setFloatValues(float... values) {
        setInitData(values);
        super.setFloatValues(values);
    }

    Object startValue, endValue;

    private void setInitData(float[] values) {
        if (values.length > 1) {
            startValue = values[0];
            endValue = values[values.length - 1];
        }
    }

    @Override
    public void start() {
        hasReversed = false;
        super.start();
    }

    @Override
    public void reverse() {
        hasReversed = true;
        super.reverse();
    }

    public void reverseBack() {
        if (animationState == AnimationState.isOpened || animationState == AnimationState.isOpening) {
            reverse();
        }
    }

    AnimatorUpdateListener animatorUpdateListener;

    @Override
    public void setupStartValues() {
        animationState = AnimationState.isClosed;
        super.setupStartValues();
    }

    @Override
    public void setupEndValues() {
        animationState = AnimationState.isOpened;
        super.setupEndValues();
    }

    private void onStartOpen(Object value) {
        if (animationListener != null && animationState != AnimationState.isOpening) {
            animationState = AnimationState.isOpening;
            QDLogger.i("onStartOpen:" + value);
            animationListener.onStartOpen(value);
        }
    }

    private void onOpening(Object value) {
        //QDLogger.i("onOpening:" + value);
        animationState = AnimationState.isOpening;
        if (animationListener != null) {
            animationListener.onOpening(value);
        }
    }

    @SuppressLint("WrongConstant")
    private void onEndOpen() {
        animationState = AnimationState.isOpened;
        if (getRepeatMode() == ValueAnimator.INFINITE) {
            currentIndex++;
        }
        QDLogger.i("onEndOpen:" + endValue);
        if (animationListener != null) {
            animationListener.onEndOpen(endValue);
        }
    }

    private void onStartClose(Object value) {
        if (animationListener != null && animationState != AnimationState.isColosing) {
            animationState = AnimationState.isColosing;
            QDLogger.i("onStartOpen:" + value);
            animationListener.onStartClose(value);
        }
    }

    private void onClosing(Object value) {
        animationState = AnimationState.isColosing;
        //QDLogger.i("onClosing:" + value);
        if (animationListener != null) {
            animationListener.onClosing(value);
        }
    }

    private void onEndClose() {
        animationState = AnimationState.isClosed;
        if (getRepeatMode() == ValueAnimator.REVERSE) {
            currentIndex++;
        }
        if (animationListener != null) {
            //QDLogger.i("onEndClose，getRepeatCount()=" + getRepeatCount());
            animationListener.onEndClose(startValue);
        }
    }

    @Override
    public void setRepeatCount(int value) {
        super.setRepeatCount(value);
    }

    AnimationListener animationListener;

    public void setAnimationListener(AnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    public static interface AnimationListener {
        void onStartOpen(Object value);

        void onOpening(Object value);

        void onEndOpen(Object value);

        void onStartClose(Object value);

        void onClosing(Object value);

        void onEndClose(Object value);
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
