package cn.demomaster.huan.quickdeveloplibrary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;

import java.util.Arrays;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 滚动文字
 */
public class MarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {

    boolean defStyle = false;//系统默认跑马灯效果
    public MarqueeTextView(Context context) {
        this(context, null);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(defStyle) {
            //设置单行
            setSingleLine();
            //设置Ellipsize
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
            //获取焦点
            setFocusable(true);
            //走马灯的重复次数，-1代表无限重复
            setMarqueeRepeatLimit(-1);
            //强制获得焦点
            setFocusableInTouchMode(true);
        }
    }

    /*
     *这个属性这个View得到焦点,在这里我们设置为true,这个View就永远是有焦点的
     */
    @Override
    public boolean isFocused() {
        if(defStyle) {
            return true;
        }else {
            return super.isFocused();
        }
    }

    int mScrollHeight;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScrollHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(0,translate_Y);
        super.onDraw(canvas);
    }

    /***设置纵向滚动***/
    int lineIndex;//当前行数
    int translate_Y;//绘制偏移量
    List<String> stringlist;
    Handler handler = new Handler();
    public void setTextArray(String[] textArray) {
        this.stringlist = Arrays.asList(textArray);
        lineIndex = 0;
        startAnimation();
    }

    ValueAnimator animator;
    public void startAnimation() {
        lineIndex = lineIndex%stringlist.size();
        setText(stringlist.get(lineIndex));
        if(animator!=null){
            animator.cancel();
            animator.removeAllUpdateListeners();
        }
        animator = ValueAnimator.ofInt(0, mScrollHeight);
        animator.setDuration(360);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            translate_Y = value;
            //QDLogger.d( "progress=" + value);
            //setText(stringlist.get(lineIndex));
            invalidate();
            if(value==mScrollHeight){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lineIndex++;
                        startAnimation();
                    }
                },6000);
            }
        });
        //animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(0);
        //new AccelerateInterpolator()
        animator.setInterpolator(new AccelerateInterpolator());
        //animator.setInterpolator(new CycleInterpolator());
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null){
            animator.cancel();
            animator.removeAllUpdateListeners();
        }
    }
}
