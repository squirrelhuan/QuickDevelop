package cn.demomaster.huan.quickdevelop.view.picktime;

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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.view.picktime.data.DateTimeModel;
import cn.demomaster.huan.quickdeveloplibrary.util.DateTimeUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopScrollListener;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView;

/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：客户端首页时间选择器
 */
public class DateTimePickerPopWin extends PopupWindow implements View.OnClickListener {
    private Button cancelBtn;
    private Button confirmBtn;
    public TextView tv_sign_year;
    public TextView tv_sign_month;
    public TextView tv_sign_day;
    private LoopView monthDayLoopView;
    private LoopView hourLoopView;
    private LoopView minuteLoopView;
    // private LoopView meridianLoopView;
    private View pickerContainerV;
    private View contentView;
    private int hourPos = 0;
    private int monthDayPos = 0;
    private int minutePos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;
    private String dateTime_ydm;
    private String dateTime_hour;
    private String dateTime_minute;

    private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
    private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
    private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
    private int viewTextSize;
    List<String> monthDayList = new ArrayList();
    List<String> hourList = new ArrayList();
    List<String> minuteList = new ArrayList();
    private OnTimePickListener mListener;

    public DateTimePickerPopWin(Builder builder) {
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.topBottomTextColor = builder.topBottomTextColor;
        this.centerTextColor = builder.centerTextColor;
        this.centerLineColor = builder.centerLineColor;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.dateTime_ydm = builder.dateTime_ydm;
        this.dateTime_hour = builder.dateTime_hour;
        this.dateTime_minute = builder.dateTime_minute;
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
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_datetime_picker, (ViewGroup) null);
        this.cancelBtn = (Button) this.contentView.findViewById(R.id.btn_cancel);
        this.cancelBtn.setTextColor(this.colorCancel);
        this.cancelBtn.setTextSize((float) this.btnTextsize);
        this.confirmBtn = (Button) this.contentView.findViewById(R.id.btn_confirm);
        this.confirmBtn.setTextColor(this.colorConfirm);
        this.confirmBtn.setTextSize((float) this.btnTextsize);

        this.monthDayLoopView = (LoopView) this.contentView.findViewById(R.id.picker_month_day);
        this.monthDayLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.monthDayLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DateTimePickerPopWin.this.monthDayPos = item;
                //更新时刻
                if (dateTimeModels.get(item).isToday()) {//处理今日
                    resetTodayHour();
                } else {
                    hourList.clear();
                    for (int j = 8; j <= 20; ++j) {
                        hourList.add(format2LenStr(j));
                    }
                }

                hourPos = 0;
                hourLoopView.setDataList(hourList);
                hourLoopView.setInitPosition(hourPos);
                //更新分钟
                resetTodayMinute();
                minutePos = 0;
                minuteLoopView.setDataList(minuteList);
                minuteLoopView.setInitPosition(minutePos);
            }
        });
        this.hourLoopView = (LoopView) this.contentView.findViewById(R.id.picker_hour);
        this.minuteLoopView = (LoopView) this.contentView.findViewById(R.id.picker_minute);
        //this.meridianLoopView = (LoopView) this.contentView.findViewById(R.id.picker_meridian);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.hourLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.hourLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DateTimePickerPopWin.this.hourPos = item;
                resetTodayMinute();
                minutePos = 0;
                minuteLoopView.setDataList(minuteList);
                minuteLoopView.setInitPosition(minutePos);
            }
        });
        this.minuteLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.minuteLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DateTimePickerPopWin.this.minutePos = item;
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
        this.setAnimationStyle(cn.demomaster.huan.quickdeveloplibrary.R.style.FadeInPopWin);
        this.setContentView(this.contentView);
        this.setWidth(-1);
        this.setHeight(-1);
    }

    private int getDefIndex(String def_provience, List<String> provinceList) {
        for (int i = 0; i < provinceList.size(); i++) {
            if (provinceList.get(i).equals(def_provience)) {
                return i;
            }
        }
        return 0;
    }

    List<DateTimeModel> dateTimeModels;

    //重置当日的钟点
    public void resetTodayHour() {
        //获取当前几点钟 ,这是默认为0的情况
        DateTimeUtil.MyDate myDate = DateTimeUtil.getToday();
        int startHour = 8;
        if (availableToday) {
            if (myDate.getHour() > 8 && myDate.getHour() < 20) {
                startHour = myDate.getHour();
                if (myDate.getMinute() > 45) {//超过四十五分，删掉当前时刻
                    startHour = startHour + 1;
                }
            }
        }
        hourList.clear();
        for (int j = startHour; j <= 20; ++j) {
            this.hourList.add(format2LenStr(j));
        }
    }

    //重置当日当时的分钟
    public void resetTodayMinute() {
        //获取当前几点钟 ,这是默认为0的情况
        DateTimeUtil.MyDate myDate = DateTimeUtil.getToday();
        int startMinute = 0;
        if (myDate.getHour() == Integer.valueOf(hourList.get(hourPos))) {
            if (myDate.getMinute() < 45) {
                startMinute = myDate.getMinute() / 15 + 1;
            }
        }
        String[] m = {"00", "15", "30", "45"};
        int endMinute = m.length;
        if (Integer.valueOf(hourList.get(hourPos)) == 20) {
            endMinute = 1;
        }
        this.minuteList.clear();
        for (int j = startMinute; j < endMinute; ++j) {
            this.minuteList.add(m[j]);
        }
    }

    private boolean availableToday = true;//今日是否可用

    private void initPickerViews() {
        dateTimeModels = DateTimeHelper.getDate();//（获取两周是14天，多向后获取一天以作备用）
        DateTimeUtil.MyDate today = DateTimeUtil.getToday();
        //日期
        if (today.getHour() >= 20) {//晚上八点以后不可预约了，删掉当日日期向后推一天
            availableToday = false;
        } else {//没有晚上八点当日可预约了，删掉多出来的最后一天
            availableToday = true;
        }
        monthDayList.clear();
        for (int i = 0; i < dateTimeModels.size(); i++) {
            this.monthDayList.add(format2MonthDayStr(dateTimeModels.get(i).getDate()) + "  " + dateTimeModels.get(i).getWeek());
        }
        if (dateTime_ydm != null) {
            this.monthDayPos = getDefIndex(format2MonthDayStr(dateTime_ydm) + "  " + DateTimeUtil.getWeekName(dateTime_ydm), monthDayList);
        }
        //时
        int startHour = 8;
        if (availableToday&&this.monthDayPos==0) {//当日可选的时候才处理当前时刻
            if (today.getHour() > 8 && today.getHour() < 20) {
                startHour = today.getHour();
                if (today.getMinute() > 45) {//超过四十五分，删掉当前时刻
                    startHour = startHour + 1;
                }
            }
        }
        hourList.clear();
        for (int j = startHour; j <= 20; ++j) {
            this.hourList.add(format2LenStr(j));
        }
        if (dateTime_hour != null) {
            this.hourPos = getDefIndex(dateTime_hour, hourList);
        }
        //分钟
        int startMinute = 0;
        if (today.getHour() == Integer.valueOf(hourList.get(hourPos))) {
            if (today.getMinute() < 45) {
                startMinute = today.getMinute() / 15 + 1;
            }
        }
        String[] m = {"00", "15", "30", "45"};
        int endMinute = m.length;
        if (today.getHour() == 20) {
            endMinute = 1;
        }
        minuteList.clear();
        for (int j = startMinute; j < endMinute; ++j) {
            this.minuteList.add(m[j]);
        }
        if (dateTime_minute != null) {
            this.minutePos = getDefIndex(dateTime_minute, minuteList);
        }

        this.monthDayLoopView.setDataList(this.monthDayList);
        this.monthDayLoopView.setInitPosition(this.monthDayPos);
        this.hourLoopView.setDataList(this.hourList);
        this.hourLoopView.setInitPosition(this.hourPos);
        this.minuteLoopView.setDataList(this.minuteList);
        this.minuteLoopView.setInitPosition(this.minutePos);
    }

    private String format2MonthDayStr(String date) {
        return date.substring(5, date.length()).replace("-", mContext.getResources().getString(R.string.month)) + mContext.getResources().getString(R.string.day);
    }

    public void onClick(View v) {
        if (v != this.contentView && v != this.cancelBtn) {
            if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(this.dateTimeModels.get(this.monthDayPos).getDate());
                    sb.append(" ");
                    sb.append(this.hourList.get(this.hourPos));
                    sb.append(":");
                    sb.append(this.minuteList.get(this.minutePos));
                    sb.append(":00");
                    this.mListener.onTimePickCompleted(dateTimeModels.get(this.monthDayPos).getDate(), hourList.get(this.hourPos), minuteList.get(this.minutePos), sb.toString());
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
            trans.setDuration(200L);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());
            this.pickerContainerV.startAnimation(trans);
        }
    }

    public void dismissPopWin() {
        TranslateAnimation trans = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
        trans.setDuration(160L);
        trans.setInterpolator(new AccelerateDecelerateInterpolator());//AccelerateInterpolator
        trans.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                DateTimePickerPopWin.this.dismiss();
            }
        });
        this.pickerContainerV.startAnimation(trans);
    }

    public static String format2LenStr(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    public interface OnTimePickListener {
        void onTimePickCompleted(String ymd, String hour, String minute, String datetime);
    }

    public static class Builder {
        private Context context;
        private OnTimePickListener listener;
        private String textCancel;
        private String textConfirm;
        private String text_sign_year = "时";
        private String text_sign_month = "分";
        private String text_sign_day = "秒";
        private int colorSignText = Color.BLACK;
        private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
        private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
        private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
        private int colorCancel;
        private int colorConfirm;
        private int colorText;
        private int btnTextSize = 16;
        private int viewTextSize = 25;
        private String dateTime_ydm;
        private String dateTime_hour;
        private String dateTime_minute;

        public Builder(Context context, OnTimePickListener listener) {
            this.context = context;
            this.listener = listener;
            textCancel = context.getResources().getString(cn.demomaster.huan.quickdeveloplibrary.R.string.Cancel);
            textConfirm = context.getResources().getString(cn.demomaster.huan.quickdeveloplibrary.R.string.Confirm);
            colorCancel = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#999999");
            colorConfirm = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#303F9F");
            colorText = context.getResources().getColor(cn.demomaster.huan.quickdeveloplibrary.R.color.colorAccent);//Color.parseColor("#f60");
        }

        public Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        public Builder colorSignText(int colorSignText) {
            this.colorSignText = colorSignText;
            return this;
        }

        public Builder colorContentText(int topBottomTextColor, int centerTextColor, int centerLineColor) {
            this.topBottomTextColor = topBottomTextColor;
            this.centerTextColor = centerTextColor;
            this.centerLineColor = centerLineColor;
            return this;
        }

        public Builder setSignText(String year, String month, String day) {
            this.text_sign_year = year;
            this.text_sign_month = month;
            this.text_sign_day = day;
            return this;
        }

        public Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public DateTimePickerPopWin build() {
            return new DateTimePickerPopWin(this);
        }

        public Builder setDefaultPosition(String dateTime_ydm, String dateTime_hour, String dateTime_minute) {
            this.dateTime_ydm = dateTime_ydm;
            this.dateTime_hour = dateTime_hour;
            this.dateTime_minute = dateTime_minute;
            return this;
        }
    }



}
