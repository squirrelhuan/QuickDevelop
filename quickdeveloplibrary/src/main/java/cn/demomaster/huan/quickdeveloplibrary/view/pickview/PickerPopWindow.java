package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class PickerPopWindow extends PopupWindow{

    public final Context context;
    public OnPickListener mListener;
    public View contentView;
    public PickerPopWindow(Builder builder) {
        this.mListener = builder.listener;
        this.context = builder.context;
        this.contentView = builder.contentView;
        initWindow();
        this.initContenView(builder.contentView);
    }

    private void initWindow() {
        WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.alpha = 1.0f;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        //mWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //窗口类型
        if (Build.VERSION.SDK_INT > 25) {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        mWindowParams.setTitle("");
        mWindowParams.packageName = context.getPackageName();
        setClippingEnabled(false);
        //mWindowParams.windowAnimations = animStyleId;// TODO
        //mWindowParams.y = mContext.getResources().getDisplayMetrics().widthPixels / 5;
        //mWindowParams.windowAnimations = R.style.CustomToast;//动画
        this.setTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.FadeInPopWin);
        this.setWidth(-1);
        this.setHeight(-1);
    }

    private void initContenView(View contentView) {
        //this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_common_picker, (ViewGroup) null);
        this.setContentView(contentView);
    }

    public void showWithView(Activity activity) {
        if (null != activity) {
            TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
            this.showAtLocation(activity.getWindow().getDecorView(), 80, 0, 0);
            trans.setDuration(200L);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());
            contentView.startAnimation(trans);
        }
    }

    public void dismissWithView(View view) {
        TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
        trans.setDuration(160L);
        trans.setInterpolator(new AccelerateDecelerateInterpolator());//AccelerateInterpolator
        trans.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                PickerPopWindow.this.dismiss();
            }
        });
       view.startAnimation(trans);
    }

    public interface OnPickListener {
        void onSelect(PickerPopWindow pickerPopWindow,Object[] selectObject,int[] selectPosition);
    }

    public static class Builder {
        public OnPickListener listener;
        public Context context;
        public View contentView;
        public Builder(Context context,View contentView, OnPickListener listener) {
            this.context = context;
            this.listener = listener;
            this.contentView = contentView;
        }

        public PickerPopWindow build() {
            return new PickerPopWindow(this);
        }
    }

}
