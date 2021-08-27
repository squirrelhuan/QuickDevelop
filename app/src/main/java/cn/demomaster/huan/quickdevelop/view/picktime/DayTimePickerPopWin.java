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
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopScrollListener;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView;


/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class DayTimePickerPopWin extends PopupWindow implements View.OnClickListener {
    private Button cancelBtn;
    private Button confirmBtn;
    public TextView tv_sign_year;
    public TextView tv_sign_month;
    public TextView tv_sign_day;
    private LoopView hourLoopView;
    private LoopView minuteLoopView;
   // private LoopView meridianLoopView;
    private View pickerContainerV;
    private View contentView;
    private int hourPos = 0;
    private int minutePos = 0;
    private int meridianPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private String text_sign_year;
    private String text_sign_month;
    private String text_sign_day;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;
    private int colorSignText;
    private int dateTime_hour_index;
    private int dateTime_minute_index;
    List<HourModel> hourModels;

    private int topBottomTextColor = 0xffA9A9A9;//array.getColor(styleable.LoopView_topBottomTextColor, -5263441);
    private int centerTextColor = 0xff11ddaf;//array.getColor(styleable.LoopView_centerTextColor, -13553359);
    private int centerLineColor = 0x00000000;//array.getColor(styleable.LoopView_lineColor, -3815995);
    private int viewTextSize;
    List<String> hourList = new ArrayList();
    List<String> minList = new ArrayList();
    List<String> meridianList = new ArrayList();
    private OnTimePickListener mListener;

    public DayTimePickerPopWin(Builder builder) {
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.topBottomTextColor = builder.topBottomTextColor;
        this.centerTextColor = builder.centerTextColor;
        this.centerLineColor = builder.centerLineColor;
        this.text_sign_year = builder.text_sign_year;
        this.text_sign_month = builder.text_sign_month;
        this.text_sign_day = builder.text_sign_day;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.hourModels = builder.hourModels;
        this.dateTime_hour_index = builder.dateTime_hour_index;
        this.dateTime_minute_index = builder.dateTime_minute_index;
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
        this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_daytime_picker, (ViewGroup) null);
        this.cancelBtn = (Button) this.contentView.findViewById(R.id.btn_cancel);
        this.cancelBtn.setTextColor(this.colorCancel);
        this.cancelBtn.setTextSize((float) this.btnTextsize);
        this.confirmBtn = (Button) this.contentView.findViewById(R.id.btn_confirm);
        this.confirmBtn.setTextColor(this.colorConfirm);
        this.confirmBtn.setTextSize((float) this.btnTextsize);
        /*this.tv_sign_year = this.contentView.findViewById(R.id.tv_sign_year);
        this.tv_sign_year.setTextColor(this.colorSignText);

        this.tv_sign_month = this.contentView.findViewById(R.id.tv_sign_month);
        this.tv_sign_month.setTextColor(this.colorSignText);

        this.tv_sign_day = this.contentView.findViewById(R.id.tv_sign_day);
        this.tv_sign_day.setTextColor(this.colorSignText);*/

        this.hourLoopView = (LoopView) this.contentView.findViewById(R.id.picker_hour);
        this.minuteLoopView = (LoopView) this.contentView.findViewById(R.id.picker_minute);
        //this.meridianLoopView = (LoopView) this.contentView.findViewById(R.id.picker_meridian);
        this.pickerContainerV = this.contentView.findViewById(R.id.container_picker);
        this.hourLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.hourLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DayTimePickerPopWin.this.hourPos = item;
                //控制联动
                dateTime_minute_index = 0;
                minutePos = dateTime_minute_index;
                minList.clear();
                List<String> m = hourModels.get(item).getChild();
                for (int i = 0; i < m.size(); ++i) {
                    minList.add(format2LenStr(Integer.valueOf(m.get(i))));
                }
                minuteLoopView.setDataList(minList);
                minuteLoopView.setInitPosition(minutePos);
            }
        });
        this.minuteLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.minuteLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DayTimePickerPopWin.this.minutePos = item;
            }
        });
        /*this.meridianLoopView.setTextColor(topBottomTextColor, centerTextColor, centerLineColor);
        this.meridianLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DateTimePickerPopWin.this.meridianPos = item;
            }
        });*/
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
        for(int i=0;i<provinceList.size();i++){
            if(provinceList.get(i).equals(def_provience)){
                return i;
            }
        }
        return 0;
    }

    private void initPickerViews() {
        this.hourPos = 0;//Calendar.getInstance().get(10) - 1;
        this.minutePos = 0;//Calendar.getInstance().get(12);
        this.meridianPos = 0;//Calendar.getInstance().get(9);

        for(int i=0;i<hourModels.size();i++){
            this.hourList.add(hourModels.get(i).getHour());
        }

        this.hourPos = dateTime_hour_index;
        List<String> m = hourModels.get(hourPos).getChild();
        for (int i = 0; i < m.size(); ++i) {
            minList.add(format2LenStr(Integer.valueOf(m.get(i))));
        }

        this.minutePos = dateTime_minute_index;

        this.meridianList.add("AM");
        this.meridianList.add("PM");
        this.hourLoopView.setDataList(this.hourList);
        this.hourLoopView.setInitPosition(this.hourPos);
        this.minuteLoopView.setDataList(this.minList);
        this.minuteLoopView.setInitPosition(this.minutePos);
       /* this.meridianLoopView.setDataList(this.meridianList);
        this.meridianLoopView.setInitPosition(this.meridianPos);*/
    }

    private String format2MonthDayStr(String date) {
        return date.substring(5,date.length()).replace("-",mContext.getResources().getString(R.string.month))+mContext.getResources().getString(R.string.day);
    }

    public void onClick(View v) {
        if (v != this.contentView && v != this.cancelBtn) {
            if (v == this.confirmBtn) {
                if (null != this.mListener) {
                    String amPm = this.meridianList.get(this.meridianPos);
                    StringBuffer sb = new StringBuffer();
                    sb.append(this.hourModels.get(this.hourPos).getDate());
                    sb.append(" ");
                    sb.append(this.hourList.get(this.hourPos));
                    sb.append(":");
                    sb.append(this.minList.get(this.minutePos));
                    //sb.append(amPm);
                    this.mListener.onTimePickCompleted(hourModels.get(this.hourPos),hourModels.get(this.hourPos).getChild().get(minutePos));
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
                DayTimePickerPopWin.this.dismiss();
            }
        });
        this.pickerContainerV.startAnimation(trans);
    }

    public static String format2LenStr(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    public interface OnTimePickListener {
        void onTimePickCompleted(HourModel hourModel, String minute);
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
        private int dateTime_hour_index;
        private int dateTime_minute_index;
        private List<HourModel> hourModels;

        public Builder(Context context,List<HourModel> hourModels, OnTimePickListener listener) {
            this.context = context;
            this.listener = listener;
            this.hourModels = hourModels;
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

        public Builder setDefIndex(int hourIndex,int minuteIndex){
            this.dateTime_hour_index = hourIndex;
            this.dateTime_minute_index = minuteIndex;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public DayTimePickerPopWin build() {
            return new DayTimePickerPopWin(this);
        }

    }






















}
