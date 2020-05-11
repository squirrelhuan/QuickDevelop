package cn.demomaster.huan.quickdeveloplibrary.animation;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import java.util.Arrays;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.util.AnimationUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

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
                    // QDLogger.i("value="+value+",isStarted()="+isStarted());
                    if (startValue == null || endValue == null) {
                        return;
                    }
                    if (lastValue == null) {
                        lastValue = startValue;
                        onStartOpen(value);
                    }
                    double c = 0;
                    boolean isEndValue = false;
                    boolean isStartValue = false;
                    if (dataType.equals(Double.class)) {
                        double a = Double.valueOf((double) endValue - (double) startValue);
                        double b = Double.valueOf((double) value - (double) lastValue);
                        c = a * b;

                        isEndValue=(double)value==(double)endValue;
                        isStartValue=(double)value==(double)startValue;
                    } else if (dataType.equals(Float.class)) {
                        float a = Float.valueOf((float) endValue - (float) startValue);
                        float b = Float.valueOf((float) value - (float) lastValue);
                        c = (double) a * b;
                        
                        isEndValue=(float)value==(float)endValue;
                        isStartValue=(float)value==(float)startValue;
                    } else if (dataType.equals(Integer.class)) {
                        int a = Integer.valueOf((int) endValue - (int) startValue);
                        int b = Integer.valueOf((int) value - (int) lastValue);
                        c = (double) a * b;

                        isEndValue=(int)value==(int)endValue;
                        isStartValue=(int)value==(int)startValue;
                    }
                    //QDLogger.e("c="+c+",value="+value+",endValue="+endValue+",state="+animationState);
                    if (c > 0) {
                        if(isEndValue){
                            //QDLogger.e("onEndOpen.......");
                            onEndOpen();
                        }else {
                            //QDLogger.e("onOpening.......");
                            onOpening(value);
                        }
                    } else if (c < 0) {//打开中
                        if(isStartValue){
                            onEndClose();
                        }else {
                            onClosing(value);
                        }
                    } else if (c == 0) {
                        boolean nearStart = false;
                        if (dataType.equals(Double.class)) {
                            double a = Double.valueOf((double) value - (double) endValue);
                            double b = Double.valueOf((double) value - (double) startValue);

                            double dz = Double.valueOf((double) value - (double) lastValue);
                            nearStart = Math.abs(a) > Math.abs(b);
                        } else if (dataType.equals(Float.class)) {
                            float a = Float.valueOf((float) value - (float) endValue);
                            float b = Float.valueOf((float) value - (float) startValue);
                            nearStart = Math.abs(a) > Math.abs(b);
                        } else if (dataType.equals(Integer.class)) {
                            int a = Integer.valueOf((int) value - (int) endValue);
                            int b = Integer.valueOf((int) value - (int) startValue);
                            nearStart = Math.abs(a) > Math.abs(b);
                        }
                        if (!nearStart) {//打开结束
                            onEndOpen();
                        } else {//关闭结束
                            onEndClose();
                        }
                    }

                    lastValue = value;
                }
            };
            addUpdateListener(animatorUpdateListener);
        }
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
        if (value == startValue) {
            animationState = AnimationState.isClosed;
        } else if (value == endValue) {
            animationState = AnimationState.isOpened;
        }
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
        animationState = AnimationState.isOpening;
        super.start();
    }

    @Override
    public void reverse() {
        super.reverse();
    }

    public void reverseBack() {
        if (animationState == AnimationState.isOpened || animationState == AnimationState.isOpening) {
            animationState = AnimationState.isColosing;
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
        if (animationListener != null) {
            if(animationState==AnimationState.isColosing){
                onEndClose();
            }else if(animationState==AnimationState.isOpened){
                onStartOpen(startValue);
            }else {
                animationState = AnimationState.isOpening;
                QDLogger.i("onOpening:" + value);
                animationListener.onOpening(value);
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void onEndOpen() {
        if (animationState == AnimationState.isOpening) {
            animationState = AnimationState.isOpened;
            if(getRepeatMode()==ValueAnimator.INFINITE){
                currentIndex++;
            }
            QDLogger.i("onEndOpen:" + endValue);
            if(animationListener != null ) {
                animationListener.onEndOpen(endValue);
            }
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
        if(animationState==AnimationState.isOpening){
            onEndOpen();
        }else if(animationState==AnimationState.isOpened){
            onStartClose(value);
        }else if (animationListener != null) {
            animationState = AnimationState.isColosing;
            QDLogger.i("onClosing:" + value);
            animationListener.onClosing(value);
        }
    }

    private void onEndClose() {
        if (animationListener != null && animationState == AnimationState.isColosing) {
            animationState = AnimationState.isClosed;
            if(getRepeatMode()==ValueAnimator.REVERSE){
                currentIndex++;
            }
            QDLogger.i("onEndClose，getRepeatCount()=" + getRepeatCount());
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
