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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.demomaster.huan.quickdeveloplibrary.R;

public class DatePickerPopWin extends PopupWindow implements OnClickListener {
    private static final int DEFAULT_MIN_YEAR = 1900;
    public Button cancelBtn;
    public Button confirmBtn;
    public LoopView yearLoopView;
    public LoopView monthLoopView;
    public LoopView dayLoopView;
    public View pickerContainerV;
    public View contentView;
    private int minYear;
    private int maxYear;
    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;
    private int viewTextSize;
    private boolean showDayMonthYear;
    List<String> yearList = new ArrayList();
    List<String> monthList = new ArrayList();
    List<String> dayList = new ArrayList();
    private DatePickerPopWin.OnDatePickedListener mListener;

    private WindowManager mWindowManager;
    public DatePickerPopWin(DatePickerPopWin.Builder builder) {
        this.minYear = builder.minYear;
        this.maxYear = builder.maxYear;
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.showDayMonthYear = builder.showDayMonthYear;
        this.setSelectedDate(builder.dateChose);
        init();
        this.initView();
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
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
        this.contentView = LayoutInflater.from(this.mContext).inflate(this.showDayMonthYear ? R.layout.layout_date_picker_inverted : R.layout.layout_date_picker, (ViewGroup)null);
       this.contentView.setLayoutParams(mWindowParams);
        this.cancelBtn = (Button)this.contentView.findViewById(R.id.btn_cancel);
        this.cancelBtn.setTextColor(this.colorCancel);
        this.cancelBtn.setTextSize((float)this.btnTextsize);
        this.confirmBtn = (Button)this.contentView.findViewById(R.id.btn_confirm);
        this.confirmBtn.setTextColor(this.colorConfirm);
        this.confirmBtn.setTextSize((float)this.btnTextsize);
        this.yearLoopView = (LoopView)this.contentView.findViewById(R.id.picker_year);
        this.monthLoopView = (LoopView)this.contentView.findViewById(R.id.picker_month);
        this.dayLoopView = (LoopView)this.contentView.findViewById(R.id.picker_day);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.yearLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DatePickerPopWin.this.yearPos = item;
                DatePickerPopWin.this.initDayPickerView();
            }
        });
        this.monthLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DatePickerPopWin.this.monthPos = item;
                DatePickerPopWin.this.initDayPickerView();
            }
        });
        this.dayLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DatePickerPopWin.this.dayPos = item;
            }
        });
        this.initPickerViews();
        this.initDayPickerView();
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
        int yearCount = this.maxYear - this.minYear;

        int j;
        for(j = 0; j < yearCount; ++j) {
            this.yearList.add(format2LenStr(this.minYear + j));
        }

        for(j = 0; j < 12; ++j) {
            this.monthList.add(format2LenStr(j + 1));
        }

        this.yearLoopView.setDataList((ArrayList)this.yearList);
        this.yearLoopView.setInitPosition(this.yearPos);
        this.monthLoopView.setDataList((ArrayList)this.monthList);
        this.monthLoopView.setInitPosition(this.monthPos);
    }

    private void initDayPickerView() {
        Calendar calendar = Calendar.getInstance();
        this.dayList = new ArrayList();
        calendar.set(1, this.minYear + this.yearPos);
        calendar.set(2, this.monthPos);
        int dayMaxInMonth = calendar.getActualMaximum(5);

        for(int i = 0; i < dayMaxInMonth; ++i) {
            this.dayList.add(format2LenStr(i + 1));
        }

        this.dayLoopView.setDataList((ArrayList)this.dayList);
        this.dayLoopView.setInitPosition(this.dayPos);
    }

    public void setSelectedDate(String dateStr) {
        if (!TextUtils.isEmpty(dateStr)) {
            long milliseconds = getLongFromyyyyMMdd(dateStr);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            if (milliseconds != -1L) {
                calendar.setTimeInMillis(milliseconds);
                this.yearPos = calendar.get(1) - this.minYear;
                this.monthPos = calendar.get(2);
                this.dayPos = calendar.get(5) - 1;
            }
        }

    }

    public void showPopWin(Activity activity) {
        if (null != activity) {
            TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
            this.showAtLocation(activity.getWindow().getDecorView(), 0, 0, 0);
            trans.setDuration(400L);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());
            this.pickerContainerV.startAnimation(trans);
        }

    }

    public void dismissPopWin() {
        TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
        trans.setDuration(400L);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                DatePickerPopWin.this.dismiss();
            }
        });
        this.pickerContainerV.startAnimation(trans);
    }

    public void onClick(View v) {
        if (v != this.contentView && v != this.cancelBtn) {
            if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    int year = this.minYear + this.yearPos;
                    int month = this.monthPos + 1;
                    int day = this.dayPos + 1;
                    StringBuffer sb = new StringBuffer();
                    sb.append(String.valueOf(year));
                    sb.append("-");
                    sb.append(format2LenStr(month));
                    sb.append("-");
                    sb.append(format2LenStr(day));
                    this.mListener.onDatePickCompleted(year, month, day, sb.toString());
                }

                this.dismissPopWin();
            }
        } else {
            this.dismissPopWin();
        }

    }

    public static long getLongFromyyyyMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date parse = null;

        try {
            parse = mFormat.parse(date);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return parse != null ? parse.getTime() : -1L;
    }

    public static String getStrDate() {
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dd.format(new Date());
    }

    public static String format2LenStr(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    public static int spToPx(Context context, int spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)((float)spValue * fontScale + 0.5F);
    }

    public interface OnDatePickedListener {
        void onDatePickCompleted(int var1, int var2, int var3, String var4);
    }

    public static class Builder {
        private Context context;
        private DatePickerPopWin.OnDatePickedListener listener;
        private boolean showDayMonthYear = false;
        private int minYear = 1900;
        private int maxYear = Calendar.getInstance().get(1) + 1;
        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private String dateChose = DatePickerPopWin.getStrDate();
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;
        private int viewTextSize = 25;

        public Builder(Context context, DatePickerPopWin.OnDatePickedListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public DatePickerPopWin.Builder minYear(int minYear) {
            this.minYear = minYear;
            return this;
        }

        public DatePickerPopWin.Builder maxYear(int maxYear) {
            this.maxYear = maxYear;
            return this;
        }

        public DatePickerPopWin.Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public DatePickerPopWin.Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public DatePickerPopWin.Builder dateChose(String dateChose) {
            this.dateChose = dateChose;
            return this;
        }

        public DatePickerPopWin.Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public DatePickerPopWin.Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        public DatePickerPopWin.Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public DatePickerPopWin.Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public DatePickerPopWin build() {
            if (this.minYear > this.maxYear) {
                throw new IllegalArgumentException();
            } else {
                return new DatePickerPopWin(this);
            }
        }

        public DatePickerPopWin.Builder showDayMonthYear(boolean useDayMonthYear) {
            this.showDayMonthYear = useDayMonthYear;
            return this;
        }
    }
}
