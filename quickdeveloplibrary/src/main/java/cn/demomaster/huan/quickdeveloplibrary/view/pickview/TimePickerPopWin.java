package cn.demomaster.huan.quickdeveloplibrary.view.pickview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class TimePickerPopWin extends PopupWindow implements View.OnClickListener {
    private Button cancelBtn;
    private Button confirmBtn;
    private LoopView hourLoopView;
    private LoopView minuteLoopView;
    private LoopView meridianLoopView;
    private View pickerContainerV;
    private View contentView;
    private int hourPos = 0;
    private int minutePos = 0;
    private int meridianPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;
    private int viewTextSize;
    List<String> hourList = new ArrayList();
    List<String> minList = new ArrayList();
    List<String> meridianList = new ArrayList();
    private TimePickerPopWin.OnTimePickListener mListener;

    public TimePickerPopWin(TimePickerPopWin.Builder builder) {
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        init();
        this.initView();
    }

    private WindowManager.LayoutParams mWindowParams;
    private void init() {
        mWindowParams = new WindowManager.LayoutParams();
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

        mWindowParams.setTitle("ToastHelper");
        mWindowParams.packageName = mContext.getPackageName();
        setClippingEnabled(false);
        //mWindowParams.windowAnimations = animStyleId;// TODO
        //mWindowParams.y = mContext.getResources().getDisplayMetrics().widthPixels / 5;
        //mWindowParams.windowAnimations = R.style.CustomToast;//动画
    }

    private void initView() {
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_time_picker, (ViewGroup)null);
        this.cancelBtn = (Button)this.contentView.findViewById(R.id.btn_cancel);
        this.cancelBtn.setTextColor(this.colorCancel);
        this.cancelBtn.setTextSize((float)this.btnTextsize);
        this.confirmBtn = (Button)this.contentView.findViewById(R.id.btn_confirm);
        this.confirmBtn.setTextColor(this.colorConfirm);
        this.confirmBtn.setTextSize((float)this.btnTextsize);
        this.hourLoopView = (LoopView)this.contentView.findViewById(R.id.picker_hour);
        this.minuteLoopView = (LoopView)this.contentView.findViewById(R.id.picker_minute);
        this.meridianLoopView = (LoopView)this.contentView.findViewById(R.id.picker_meridian);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.hourLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                TimePickerPopWin.this.hourPos = item;
            }
        });
        this.minuteLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                TimePickerPopWin.this.minutePos = item;
            }
        });
        this.meridianLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                TimePickerPopWin.this.meridianPos = item;
            }
        });
        this.initPickerViews();
        this.cancelBtn.setOnClickListener(this);
        this.confirmBtn.setOnClickListener(this);
        this.contentView.setOnClickListener(this);
        if (!TextUtils.isEmpty(this.textConfirm)) {
            this.confirmBtn.setText(this.textConfirm);
        }

        if (!TextUtils.isEmpty(this.textCancel)) {
            this.cancelBtn.setText(this.textCancel);
        }

        this.setTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setAnimationStyle(R.style.FadeInPopWin);
        this.setContentView(this.contentView);
        this.setWidth(-1);
        this.setHeight(-1);
    }

    private void initPickerViews() {
        this.hourPos = Calendar.getInstance().get(10) - 1;
        this.minutePos = Calendar.getInstance().get(12);
        this.meridianPos = Calendar.getInstance().get(9);

        int j;
        for(j = 1; j <= 12; ++j) {
            this.hourList.add(format2LenStr(j));
        }

        for(j = 0; j < 60; ++j) {
            this.minList.add(format2LenStr(j));
        }

        this.meridianList.add("AM");
        this.meridianList.add("PM");
        this.hourLoopView.setDataList(this.hourList);
        this.hourLoopView.setInitPosition(this.hourPos);
        this.minuteLoopView.setDataList(this.minList);
        this.minuteLoopView.setInitPosition(this.minutePos);
        this.meridianLoopView.setDataList(this.meridianList);
        this.meridianLoopView.setInitPosition(this.meridianPos);
    }

    public void onClick(View v) {
        if (v != this.contentView && v != this.cancelBtn) {
            if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    String amPm = (String)this.meridianList.get(this.meridianPos);
                    StringBuffer sb = new StringBuffer();
                    sb.append(String.valueOf(this.hourList.get(this.hourPos)));
                    sb.append(":");
                    sb.append(String.valueOf(this.minList.get(this.minutePos)));
                    sb.append(amPm);
                    this.mListener.onTimePickCompleted(this.hourPos + 1, this.minutePos, amPm, sb.toString());
                }

                this.dismissPopWin();
            }
        } else {
            this.dismissPopWin();
        }

    }

    public void showPopWin(Activity activity) {
        if (null != activity) {
            TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
            this.showAtLocation(activity.getWindow().getDecorView(), 80, 0, 0);
            trans.setDuration(400L);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());
            this.pickerContainerV.startAnimation(trans);
        }

    }

    public void dismissPopWin() {
        TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
        trans.setDuration(400L);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                TimePickerPopWin.this.dismiss();
            }
        });
        this.pickerContainerV.startAnimation(trans);
    }

    public static String format2LenStr(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    public interface OnTimePickListener {
        void onTimePickCompleted(int var1, int var2, String var3, String var4);
    }

    public static class Builder {
        private Context context;
        private TimePickerPopWin.OnTimePickListener listener;
        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;
        private int viewTextSize = 25;

        public Builder(Context context, TimePickerPopWin.OnTimePickListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public TimePickerPopWin.Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public TimePickerPopWin.Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public TimePickerPopWin.Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public TimePickerPopWin.Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        public TimePickerPopWin.Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public TimePickerPopWin.Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public TimePickerPopWin build() {
            return new TimePickerPopWin(this);
        }
    }
}
