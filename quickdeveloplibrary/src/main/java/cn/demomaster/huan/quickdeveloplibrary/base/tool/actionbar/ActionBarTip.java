package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import static cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot.TAG;

/**
 * Created by Squirrel桓 on 2018/12/25.
 */
public class ActionBarTip extends FrameLayout {
    public ActionBarTip(@NonNull Context context) {
        super(context);
        init();
    }

    public ActionBarTip(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionBarTip(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private View contentView;

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
        this.addView(contentView);
    }

    int contentViewResID;
    public void setContentView(int contentViewResID) {
        this.contentViewResID = contentViewResID;
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(contentViewResID, this);
        this.contentView = this.getChildAt(0);
    }

    private FrameLayout.LayoutParams layoutParams_tip;
    public void init() {
        if (getLayoutParams() == null) {
            layoutParams_tip = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams_tip);
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //hide();
            }
        });

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        position_Y = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        position_Y = topMin + motionEvent.getY();
                        hide();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float distanc_y = motionEvent.getY() - position_Y;

                        layoutParams_tip = (LayoutParams) getLayoutParams();
                        int top_c = (int) (layoutParams_tip.topMargin + distanc_y);
                        if (top_c > topMin && top_c < topMax) {
                            layoutParams_tip.topMargin = top_c;
                        }
                        Log.i(TAG, "topMax=" + topMax);
                        Log.i(TAG, "topMin=" + topMin);
                        setLayoutParams(layoutParams_tip);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        position_Y = motionEvent.getY();
                        break;
                }
                //Log.i(TAG,"Y="+motionEvent.getY());
                return false;
            }
        });

        //view加载完成时回调
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                topMin = actionBarHeight - getHeight();
                topMax = actionBarHeight;
                setVisibility(View.GONE);
                // show();
            }
        });
    }

    private int topMax;
    private int topMin;
    private float position_Y;
    private int duration = 400;
    ValueAnimator animator;

    public void show() {
        animator = ValueAnimator.ofFloat(topMin, (int) (getHeight() + topMin));
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                layoutParams_tip.topMargin = (int) value;
                setLayoutParams(layoutParams_tip);
                if (value == topMin) {
                    setVisibility(GONE);
                } else {
                    setVisibility(VISIBLE);
                }
            }
        });
        //animator.setRepeatMode(ValueAnimator.REVERSE);
        //animator.setRepeatCount(ValueAnimator.INFINITE);//accelerate_decelerate_interpolator
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    void hide() {
        layoutParams_tip = (LayoutParams) getLayoutParams();
        if (animator != null) {
            animator.setFloatValues(topMin, layoutParams_tip.topMargin);
            animator.setDuration((int) (duration * ((float) (layoutParams_tip.topMargin - topMin) / getHeight())));
            animator.reverse();
        }
    }

    private int actionBarHeight;
    public void setActionBarHeight(int height) {
        this.actionBarHeight = height;
    }


    /**
     * 状态
     * 1.可以手动点击关闭
     * 2.可以强制固定不可关闭
     * 3.定时关闭
     * 4.收到更新消息
     */

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    public void showDelayed(){
        showDelayed(5000);
    }
    private int delayedTime=5000;
    public void showDelayed(int time){
        delayedTime = time;
        show();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delayedTime);
    }


}
