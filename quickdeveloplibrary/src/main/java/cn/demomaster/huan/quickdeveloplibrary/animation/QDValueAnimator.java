package cn.demomaster.huan.quickdeveloplibrary.animation;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Field;

import cn.demomaster.qdlogger_library.QDLogger;

public class QDValueAnimator extends ValueAnimator {

    public enum AnimationState {
        idle,
        isOpened,
        isOpening,
        isClosed,
        isColosing,
        isOpenStart,
        isCloseStart,
    }

    boolean isFrward = false;
    boolean hasReversed = false;

    public boolean isHasReversed() {
        return hasReversed;
    }

    Handler handler;
    private Object tag;
    private AnimationState state = AnimationState.idle;
    AnimatorListener mAnimatorListener;

    public QDValueAnimator(Class dataType) {
        this.dataType = dataType;
        handler = new Handler(Looper.getMainLooper());
        AnimatorUpdateListener animatorUpdateListener = new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = animation.getAnimatedValue();
                //QDLogger.i("value="+value);
                if (isFrward) {
                    onOpening(value);
                } else {
                    onClosing(value);
                }
                lastValue = value;
            }
        };
        addUpdateListener(animatorUpdateListener);
        mAnimatorListener = new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

           /* @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
            }*/

            @Override
            public void onAnimationEnd(Animator animation) {
                //QDLogger.e("onAnimationEnd hash="+QDValueAnimator.this.hashCode()+",listener="+animation.hashCode()+",isFrward="+isFrward+",animation.isRunning="+animation.isRunning());
                if (isFrward) {
                    onEndOpen();
                } else {
                    onEndClose();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // QDLogger.e("onAnimationCancel hash="+QDValueAnimator.this.hashCode());
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // QDLogger.e("onAnimationRepeat hash="+QDValueAnimator.this.hashCode());
            }
        };
        addListener(mAnimatorListener);
    }

    public boolean isFrward() {
        return isFrward;
    }

    //弃用
    public boolean isReversing2() {
        return isReversing2(ValueAnimator.class, this);
    }

    //弃用
    boolean isReversing2(Class clazz, Animator animator) {
        Field[] fields = clazz.getDeclaredFields();
        // 暴力反射获取属性
        // Field filed = class1.getDeclaredField("name");
        if (fields != null) {
            //QDLogger.println(clazz.getName()+":fields.length =" + fields.length);
            for (int i = 0, len = fields.length; i < len; i++) {
                String varName = fields[i].getName();
                //QDLogger.println("varName =" + varName);
                if (varName.equals("mReversing")) {
                    try {
                        boolean accessFlag = fields[i].isAccessible();
                        fields[i].setAccessible(true);
                        Object o = fields[i].get(animator);
                        fields[i].setAccessible(accessFlag);
                        //QDLogger.println("isReversing =" + o);
                        return (boolean) o;
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        QDLogger.e(ex);
                    }
                }
            }
        }
        return false;
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

    Object value;//当前值
    Object lastValue;//上一次动画值

    public Object getValue() {
        return value;
    }

    public AnimationState getState() {
        return state;
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
        isFrward = true;
        super.start();
    }

    @Override
    public void reverse() {
        hasReversed = true;
        isFrward = !isFrward;
        //QDLogger.e("reverse() isReversing="+isReversing);
        super.reverse();
    }

    /**
     * 顺序动画
     */
    public void forward() {
        if (!isFrward) {
            start();
        }
    }

    /**
     * 逆序动画
     */
    public void backward() {
        if (isFrward) {
            reverse();
        }
    }

    @Override
    public void resume() {
        hasReversed = false;
        super.resume();
    }

    @Override
    public void setupStartValues() {
        state = AnimationState.isClosed;
        super.setupStartValues();
    }

    @Override
    public void setupEndValues() {
        state = AnimationState.isOpened;
        super.setupEndValues();
    }

    private void onStartOpen(Object value) {
        if (animationListener != null && state != AnimationState.isOpening) {
            state = AnimationState.isOpening;
            //QDLogger.i("onStartOpen:" + value);
            animationListener.onStartOpen(value);
        }
    }

    private void onOpening(Object value) {
        //QDLogger.i("onOpening:" + value);
        state = AnimationState.isOpening;
        if (animationListener != null) {
            animationListener.onOpening(value);
        }
    }

    private void onEndOpen() {
        setupEndValues();
        state = AnimationState.isOpened;
        //QDLogger.i("onEndOpen:" + endValue);
        if (animationListener != null) {
            animationListener.onEndOpen(endValue);
        }
    }

    private void onStartClose(Object value) {
        if (animationListener != null && state != AnimationState.isColosing) {
            state = AnimationState.isColosing;
            //QDLogger.i("onStartOpen:" + value);
            animationListener.onStartClose(value);
        }
    }

    private void onClosing(Object value) {
        state = AnimationState.isColosing;
        //QDLogger.i("onClosing:" + value);
        if (animationListener != null) {
            animationListener.onClosing(value);
        }
    }

    private void onEndClose() {
        setupStartValues();
        state = AnimationState.isClosed;
        if (animationListener != null) {
            //QDLogger.i("onEndClose，getRepeatCount()=" + getRepeatCount());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    animationListener.onEndClose(startValue);
                }
            });
        }
    }

    @Override
    public void setRepeatCount(int value) {
        super.setRepeatCount(value);
    }

    private AnimationListener animationListener;

    public void setAnimationListener(AnimationListener listener) {
        //QDLogger.e("hash=" + hashCode() + ",listener=" + listener.hashCode());
        this.animationListener = listener;
    }

    public interface AnimationListenerInterface {
        void onStartOpen(Object value);

        void onOpening(Object value);

        void onEndOpen(Object value);

        void onStartClose(Object value);

        void onClosing(Object value);

        void onEndClose(Object value);
    }

    public static abstract class AnimationListener implements AnimationListenerInterface {
        @Override
        public void onStartOpen(Object value) {

        }

        @Override
        public void onStartClose(Object value) {

        }
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
