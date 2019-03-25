package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;

import static cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot.TAG;
import static cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView.StateType.COMPLETE;
import static cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView.StateType.ERROR;
import static cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView.StateType.LOADING;

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

    private StateView stateView;
    private TextView textView;
    private ImageTextView itv_retry;
    int contentViewResID;
    private ActionBarState.OnLoadingStateListener loadingStateListener;
    private ActionBarState.Loading retry;
    public void setLoadingStateListener(ActionBarState.OnLoadingStateListener onLoadingStateListener) {
        if (onLoadingStateListener == null) {
            return;
        } else {
            onLoadingStateListener.setResult(retry);
        }
        this.loadingStateListener = onLoadingStateListener;
        actionBarState.setOnLoadingStateListener(onLoadingStateListener);
    }

    public void setContentView(int contentViewResID) {
        this.contentViewResID = contentViewResID;
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(contentViewResID, this);
        this.contentView = this.getChildAt(0);
        this.stateView = contentView.findViewById(R.id.sv_icon);
        this.textView = contentView.findViewById(R.id.textView);
        this.itv_retry = contentView.findViewById(R.id.itv_retry);
        this.itv_retry.setTextSize(14);
        this.itv_retry.setText("重试");
        itv_retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionBarState != null && actionBarState.getOnLoadingStateListener() != null) {
                    ActionBarTip.this.loading();
                    //新线程处理耗时操作
                    Thread t = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                actionBarState.getOnLoadingStateListener().loading();
                            } catch (Exception e) {
                                e.printStackTrace();
                                QDLogger.e(TAG,"异常/耗时操作，不能直接处理UI");
                            }
                        }
                    });
                    t.start();
                }
            }
        });
    }

    private ActionBarState actionBarState;
    public void setActionBarState(ActionBarState actionBarState) {
        this.actionBarState = actionBarState;
    }

    private FrameLayout.LayoutParams layoutParams_tip;

    public void init() {
        retry = new ActionBarState.Loading() {
            @Override
            public void hide() {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //成功 结束并隐藏
                        ActionBarTip.this.hide();
                    }
                });
            }

            @Override
            public void success() {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //成功 结束并隐藏
                        ActionBarTip.this.completeAndHide();
                    }
                });
            }

            @Override
            public void fail() {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //失败 改变状态不隐藏
                        ActionBarTip.this.errorAndShow();
                    }
                });
            }

            @Override
            public void success(final String message) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //成功 结束并隐藏
                        ActionBarTip.this.completeAndHide();
                        textView.setText(message);
                    }
                });
            }

            @Override
            public void fail(final String message) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //失败 改变状态不隐藏
                        ActionBarTip.this.errorAndShow();
                        textView.setText(message);
                    }
                });
            }

            @Override
            public void setText(final String message) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(message);
                    }
                });
            }
        };
        actionBarState = new ActionBarState();

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
                        if (stateType == ERROR || stateType == LOADING) {
                            show();
                        } else {
                            hide();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float distanc_y = motionEvent.getY() - position_Y;

                        layoutParams_tip = (LayoutParams) getLayoutParams();
                        int top_c = (int) (layoutParams_tip.topMargin + distanc_y);
                        if (top_c > topMin && top_c < topMax) {
                            layoutParams_tip.topMargin = top_c;
                        }
                        QDLogger.i(TAG, "topMax=" + topMax);
                        QDLogger.i(TAG, "topMin=" + topMin);
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

    public void startAnimation() {
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

    public void show() {
        layoutParams_tip = (LayoutParams) getLayoutParams();
        if (animator != null) {
            animator.setFloatValues(layoutParams_tip.topMargin, (int) (getHeight() + topMin));
            animator.setDuration((int) (duration * ((float) ((getHeight() + topMin) - layoutParams_tip.topMargin) / getHeight())));
            animator.start();
        }
    }

    public void hide() {
        layoutParams_tip = (LayoutParams) getLayoutParams();
        if (animator != null) {
            animator.setFloatValues(topMin, layoutParams_tip.topMargin);
            animator.setDuration((int) (duration * ((float) (layoutParams_tip.topMargin - topMin) / getHeight())));
            animator.reverse();
        }
    }

    //成功 结束并隐藏
    public void completeAndHide() {
        stateType = StateView.StateType.COMPLETE;
        stateView.setStateType(COMPLETE);
        itv_retry.setVisibility(GONE);
        hideDelayed(3000);
    }

    //失败 改变状态不隐藏
    public void errorAndShow() {
        stateType = StateView.StateType.ERROR;
        stateView.setStateType(ERROR);
    }

    public void loading() {
        stateType = StateView.StateType.LOADING;
        stateView.setStateType(stateType);
        itv_retry.setVisibility(GONE);
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
            if (stateType == ERROR || stateType == LOADING) {

            } else {
                hide();
            }
        }
    };

    public void showDelayed() {
        showDelayed(5000);
    }

    private int delayedTime = 5000;

    public void showDelayed(int time) {
        delayedTime = time;
        startAnimation();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delayedTime);
    }

    public void hideDelayed() {
        hideDelayed(5000);
    }

    public void hideDelayed(int time) {
        delayedTime = time;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delayedTime);
    }

    private StateView.StateType stateType = StateView.StateType.COMPLETE;

    public void showWarning(String message) {
        stateType = StateView.StateType.WARNING;
        stateView.setStateType(stateType);
        textView.setText(message);
        itv_retry.setVisibility(GONE);
        showDelayed();
    }

    public void showComplete(String message) {
        stateType = StateView.StateType.COMPLETE;
        stateView.setStateType(stateType);
        textView.setText(message);
        itv_retry.setVisibility(GONE);
        showDelayed();
    }

    public void showError(String message) {
        stateType = ERROR;
        stateView.setStateType(stateType);
        textView.setText(message);
        itv_retry.setVisibility(VISIBLE);
        showDelayed();
    }

    public void showLoading(String message) {
        stateType = StateView.StateType.LOADING;
        stateView.setStateType(stateType);
        textView.setText(message);
        itv_retry.setVisibility(GONE);
        showDelayed();
    }

    private View mBelowContentView;
    private int paddingTopBack;
    private ActionBarLayoutContentView actionBarLayoutContentView;
    public ACTIONBARTIP_TYPE mActionbartip_type = ACTIONBARTIP_TYPE.NORMAL;
    public void setBelowContent(ActionBarLayoutContentView actionBarLayoutContentView) {
        this.actionBarLayoutContentView = actionBarLayoutContentView;
        mBelowContentView = actionBarLayoutContentView.contentViewBack;
        mActionbartip_type = actionBarLayoutContentView.actionbartipType;
        paddingTopBack = mBelowContentView.getPaddingTop();
    }

    public void setActionTipType(ACTIONBARTIP_TYPE actionbartip_type) {
        mActionbartip_type = actionbartip_type;
    }

    /**
     * 导航栏样式三种
     */
    public enum ACTIONBARTIP_TYPE {
        //有导航栏
        NORMAL,
        //层叠
        STACK
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        if(mBelowContentView!=null&&mActionbartip_type!=ACTIONBARTIP_TYPE.STACK){
            int h = actionBarLayoutContentView.getActionBarPaddingTop();
            QDLogger.d("setLayoutParams H=="+h);
            mBelowContentView.setPadding(mBelowContentView.getPaddingLeft(), h+getHeight()+paddingTopBack+((FrameLayout.LayoutParams)params).topMargin, mBelowContentView.getPaddingRight(), mBelowContentView.getPaddingBottom());
        }
    }
}
