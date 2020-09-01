package cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.QDHandler;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadStateType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.ImageTextView;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadStateType.COMPLETE;
import static cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadStateType.ERROR;
import static cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadStateType.LOADING;

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
                    QdThreadHelper.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                actionBarState.getOnLoadingStateListener().loading();
                            } catch (Exception e) {
                                QDLogger.e(e);
                            }
                        }
                    });
                }
            }
        });
    }

    private ActionBarState actionBarState;
    public void setActionBarState(ActionBarState actionBarState) {
        this.actionBarState = actionBarState;
    }

    private LayoutParams layoutParams_tip;

    public void init() {
        retry = new ActionBarState.Loading() {
            @Override
            public void hide() {
                QdThreadHelper.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //成功 结束并隐藏
                        ActionBarTip.this.hide();
                    }
                });
            }

            @Override
            public void success() {
                QdThreadHelper.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //成功 结束并隐藏
                        ActionBarTip.this.completeAndHide();
                    }
                });
            }

            @Override
            public void fail() {
                QdThreadHelper.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //失败 改变状态不隐藏
                        ActionBarTip.this.errorAndShow();
                    }
                });
            }

            @Override
            public void success(final String message) {
                QdThreadHelper.runOnUiThread(new Runnable() {
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
                QdThreadHelper.runOnUiThread(new Runnable() {
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
            layoutParams_tip = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                        QDLogger.i( "topMax=" + topMax+",topMin=" + topMin);
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

        setVisibility(INVISIBLE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setActionBarHeight(bottom-top);

        /*
        QDLogger.i( "onLayout max="+max+",height=" +(bottom-top)+","+ (-max+top)+",top="+top+", getHeight()=" +  getHeight());*/
    }


    private int topMax;
    private int topMin;
    private float position_Y;
    private int duration = 400;
    ValueAnimator animator;

    public void startAnimation() {
        animator = ValueAnimator.ofFloat(topMin,topMax);
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
        stateType = LoadStateType.COMPLETE;
        stateView.setStateType(COMPLETE);
        itv_retry.setVisibility(GONE);
        hideDelayed(3000);
    }

    //失败 改变状态不隐藏
    public void errorAndShow() {
        stateType = LoadStateType.ERROR;
        stateView.setStateType(ERROR);
    }

    public void loading() {
        stateType = LoadStateType.LOADING;
        stateView.setStateType(stateType);
        itv_retry.setVisibility(GONE);
    }

    private int actionBarHeight;
    public void setActionBarHeight(int height) {
        this.actionBarHeight = height;
        topMin = actionBarHeight - getHeight();
        topMax = actionBarHeight;
        QDLogger.e("setActionBarHeight="+height+", getHeight()="+ getHeight()+",topMin="+topMin+",topMax="+topMax);
    }

    /**
     * 状态
     * 1.可以手动点击关闭
     * 2.可以强制固定不可关闭
     * 3.定时关闭
     * 4.收到更新消息
     */
    Handler handler = new QDHandler();
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if (stateType == ERROR || stateType == LOADING) {

            } else {
                hide();
            }
        }
    };

    public void onDestroy() {
        handler.removeCallbacks(myRunnable);
    }

    public void showDelayed() {
        showDelayed(5000);
    }

    private int delayedTime = 5000;
    public void showDelayed(int time) {
        delayedTime = time;
        startAnimation();
        handler.removeCallbacks(myRunnable);
        handler.postDelayed(myRunnable, delayedTime);
    }

    public void hideDelayed() {
        hideDelayed(5000);
    }
    public void hideDelayed(int time) {
        delayedTime = time;
        handler.removeCallbacks(myRunnable);
        handler.postDelayed(myRunnable, delayedTime);
    }

    private LoadStateType stateType = LoadStateType.COMPLETE;
    public void showWarning(String message) {
        stateType = LoadStateType.WARNING;
        stateView.setStateType(stateType);
        textView.setText(message);
        itv_retry.setVisibility(GONE);
        showDelayed();
    }

    public void showComplete(String message) {
        stateType = LoadStateType.COMPLETE;
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
        stateType = LoadStateType.LOADING;
        stateView.setStateType(stateType);
        textView.setText(message);
        itv_retry.setVisibility(GONE);
        showDelayed();
    }

    public ACTIONBARTIP_TYPE mActionbartip_type = ACTIONBARTIP_TYPE.NORMAL;
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

        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        Rect rect = new Rect(0, (int) (-layoutParams.topMargin+topMax),getWidth(), getHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setClipBounds(rect);
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null) {
            animator.cancel();
        }
        actionBarState=null;
        retry = null;
    }
}
