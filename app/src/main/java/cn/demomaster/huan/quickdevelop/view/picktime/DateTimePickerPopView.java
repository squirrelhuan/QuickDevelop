package cn.demomaster.huan.quickdevelop.view.picktime;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.view.picktime.data.DateTimeModel;
import cn.demomaster.huan.quickdeveloplibrary.util.DateTimeUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopScrollListener;
import cn.demomaster.huan.quickdeveloplibrary.view.pickview.LoopView2;

/**
 * @author squirrel桓
 * @date 2018/11/16.
 * description：
 */
public class DateTimePickerPopView implements View.OnClickListener {
    public TextView tv_sign_year;
    public TextView tv_sign_month;
    public TextView tv_sign_day;
    private LoopView2 monthDayLoopView;
    private LoopView2 hourLoopView;
    private LoopView2 minuteLoopView;
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

    public DateTimePickerPopView(Builder builder) {
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
        this.initView(builder.contentView);
    }

    private void initView(View contentView) {
        this.contentView = contentView;

        this.monthDayLoopView = (LoopView2) this.contentView.findViewById(R.id.pv_picker_month_day);
        this.monthDayLoopView.setTextColor(topBottomTextColor, centerTextColor);
        this.monthDayLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DateTimePickerPopView.this.monthDayPos = item;

                //更新时刻
                if (item == 0) {//处理今日
                    resetTodayHour();
                } else {
                    hourList.clear();
                    for (int j = 8; j <= 20; ++j) {
                        hourList.add(format2LenStr(j));
                    }
                }

                hourPos = 0;
                hourLoopView.setDataList(hourList);
                hourLoopView.setCurrentIndex(hourPos);
//更新分钟
                resetTodayMinute();
                minutePos = 0;
                minuteLoopView.setDataList(minuteList);
                minuteLoopView.setCurrentIndex(minutePos);
            }
        });
        this.hourLoopView = (LoopView2) this.contentView.findViewById(R.id.pv_picker_hour);
        this.minuteLoopView = (LoopView2) this.contentView.findViewById(R.id.pv_picker_minute);
        this.hourLoopView.setTextColor(topBottomTextColor, centerTextColor);
        this.hourLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DateTimePickerPopView.this.hourPos = item;
                resetTodayMinute();
                minutePos = 0;
                minuteLoopView.setDataList(minuteList);
                minuteLoopView.setCurrentIndex(minutePos);
            }
        });
        this.minuteLoopView.setTextColor(topBottomTextColor, centerTextColor);
        this.minuteLoopView.setLoopListener(new LoopScrollListener() {
            public void onItemSelect(int item) {
                DateTimePickerPopView.this.minutePos = item;
            }
        });
        this.initPickerViews();
        if (this.contentView.findViewById(R.id.btn_confirm) != null) {
            this.contentView.findViewById(R.id.btn_confirm).setOnClickListener(this);
        }

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
        dateTimeModels = getDateTime();//（获取两周是14天，多向后获取一天以作备用）

        DateTimeUtil.MyDate myDate = DateTimeUtil.getToday();
        //日期
        int startDay = 0;
        int endDay = 0;
        if (myDate.getHour() >= 20) {//晚上八点以后不可预约了，删掉当日日期向后推一天
            availableToday = false;
            startDay = 1;
            endDay = dateTimeModels.size();
        } else {//没有晚上八点当日可预约了，删掉多出来的最后一天
            availableToday = true;
            startDay = 0;
            endDay = dateTimeModels.size() - 1;
        }
        monthDayList.clear();
        for (int i = startDay; i < endDay; i++) {
            this.monthDayList.add(format2MonthDayStr(dateTimeModels.get(i).getDate()) + "  " + dateTimeModels.get(i).getWeek());
        }
        if (dateTime_ydm != null) {
            this.monthDayPos = getDefIndex(format2MonthDayStr(dateTime_ydm) + "  " + DateTimeUtil.getWeekName(dateTime_ydm), monthDayList);
        }
        //时
        int startHour = 8;
        if (availableToday) {//当日可选的时候才处理当前时刻
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
        if (dateTime_hour != null) {
            this.hourPos = getDefIndex(dateTime_hour, hourList);
        }
        //分钟
        int startMinute = 0;
        if (myDate.getHour() == Integer.valueOf(hourList.get(hourPos))) {
            if (myDate.getMinute() < 45) {
                startMinute = myDate.getMinute() / 15 + 1;
            }
        }
        String[] m = {"00", "15", "30", "45"};
        int endMinute = m.length;
        if (myDate.getHour() == 20) {
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
        this.monthDayLoopView.setCurrentIndex(this.monthDayPos);
        this.hourLoopView.setDataList(this.hourList);
        this.hourLoopView.setCurrentIndex(this.hourPos);
        this.minuteLoopView.setDataList(this.minuteList);
        this.minuteLoopView.setCurrentIndex(this.minutePos);
    }

    private String format2MonthDayStr(String date) {
        return date.substring(5, date.length()).replace("-", mContext.getResources().getString(R.string.month)) + mContext.getResources().getString(R.string.day);
    }

    public void onClick(View v) {
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
        private View contentView;

        public Builder(Context context, View contentView, OnTimePickListener listener) {
            this.context = context;
            this.listener = listener;
            this.contentView = contentView;
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

        public View getContentView() {
            return contentView;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public DateTimePickerPopView build() {
            return new DateTimePickerPopView(this);
        }

        public Builder setDefaultPosition(String dateTime_ydm, String dateTime_hour, String dateTime_minute) {
            this.dateTime_ydm = dateTime_ydm;
            this.dateTime_hour = dateTime_hour;
            this.dateTime_minute = dateTime_minute;
            return this;
        }
    }


    private List<DateTimeModel> getDateTime() {
        List<DateTimeModel> lists = new ArrayList<>();
        //获取当前日期
        /*String today = DateTimeUtil.StringData();
        //获取是星期几
        int weekIndex = DateTimeUtil.getWeek(today);
        int oldCount = weekIndex - 2;
        Date oldDate = DateTimeUtil.getOldDate(-oldCount);//获取前-n天的日期*/
        String week = "";
        Date date = new Date();
        List<String> dateList = DateTimeUtil.getDateAsCount(date, 15);
        boolean isFree = false;
        boolean isAvailable = false;
        int availableCount = 0;
        for (String s : dateList) {
            DateTimeModel dateTimeModel = new DateTimeModel();
            /*System.out.println(s);
            if (s.equals(DateTimeUtil.StringData())) {
                week = "今天";
                isAvailable = true;
                isFree = true;
                dateTimeModel.setSpecial(true);//特殊日期需要标红
            } else {
                //每逢15或初一 ，显示为 YY-DD
                int day = Integer.valueOf(s.substring(s.length() - 2, s.length()));
                if (day == 1) {
                    week = s.substring(s.length() - 5, s.length());
                    week = week.replace("-", "月") + "日";
                    dateTimeModel.setSpecial(true);//特殊日期需要标红
                } else {
                    week = "" + day;//"周"+DateTimeUtil.getWeek(s);
                }
            }
            if (isAvailable) {
                availableCount++;
                dateTimeModel.setAvailable(true);
                if (availableCount == 14) {
                    isAvailable = false;
                }
            }*/
            dateTimeModel.setWeek(DateTimeUtil.getWeekName(s));
            //dateTimeModel.setTitle(week);
            dateTimeModel.setDate(s);
            //dateTimeModel.setFree(isFree);
            lists.add(dateTimeModel);
        }

        return lists;
    }


}
