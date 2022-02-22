package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2018/11/27.
 * description：
 */
public class DateTimeUtil {

    public static String getDuration(long startTime, long currentTimeMillis) {
        return formatTime((currentTimeMillis - startTime) / 1000);
    }

    /**
     * 将秒转化为 HH:mm:ss 的格式
     *
     * @param duration 秒
     * @return
     */
    public static String formatTime(long duration) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        //String dd = decimalFormat.format(time / 3600/24);
        String hh = decimalFormat.format(duration / 3600);
        String mm = decimalFormat.format(duration % 3600 / 60);
        String ss = decimalFormat.format(duration % 60);
        return hh + ":" + mm + ":" + ss;
       /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");//设置日期格式,这里只取出小时和分钟
        return simpleDateFormat.format(new Date(time));*/
    }

    public static String formatDayTime1(Context context, long time) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        //String dd = decimalFormat.format(time / 3600/24);
        String timeStr = "";
        if (time > 3600) {
            String hh = decimalFormat.format(time / 3600);
            timeStr += hh + context.getResources().getString(R.string.hour);
        }
        if (time > 3600 / 60) {
            String mm = decimalFormat.format(time % 3600 / 60);
            timeStr += mm + context.getResources().getString(R.string.minute);
        }
        String ss = decimalFormat.format(time % 60);
        timeStr += ss + context.getResources().getString(R.string.second);
        return timeStr;
    }

    /**
     * 将秒转化为 HH:mm:ss 的格式
     *
     * @param time 秒
     * @return
     */
    public static String formatDayTime2(long time) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        //String dd = decimalFormat.format(time / 3600/24);
        String hh = decimalFormat.format(time / 3600);
        String mm = decimalFormat.format(time % 3600 / 60);
        String ss = decimalFormat.format(time % 60);
        return hh + ":" + mm + ":" + ss;
    }

    public static String formatTime(Context context, long time, String formatStrs) {
        int day = (int) (time / 86400);
        int hour = (int) (time % 86400 / 3600);
        int minute = (int) (time % 86400 % 3600 / 60);
        int second = (int) (time % 86400 % 3600 % 60);
        StringBuilder stringBuffer = new StringBuilder();
        if (day > 0) {
            stringBuffer.append(day);
        }
        if (day > 0 || hour > 0) {
            stringBuffer.append(hour);
        }
        if (day > 0 || hour > 0 || minute > 0) {
            stringBuffer.append(minute);
        }
        if (day > 0 || hour > 0 || minute > 0 || second > 0) {
            stringBuffer.append(second);
        }
        return stringBuffer.toString();
    }


    /**
     * 获取今天往后一周的集合
     */
    public void get7week() {
        String week = "";
        List<String> weeksList = new ArrayList<>();
        List<String> dateList = getDateAsCount(21);
        for (String s : dateList) {
            QDLogger.println(s);
            if (s.equals(StringData())) {
                week = "今天";
            } else {
                week = "周" + getWeek(s);
            }
            weeksList.add(week);
        }
        for (String str : weeksList) {
            QDLogger.println(str);
        }
        //return weeksList;
    }


    public static List<String> getDateAsCount(int count) {
        List<String> dates = new ArrayList<>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat sim = new SimpleDateFormat(
                "yyyy-MM-dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < count - 1; i++) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        return dates;
    }

    public static List<String> getDateAsCount(Date targetDate, int count) {
        List<String> dates = new ArrayList<>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat sim = new SimpleDateFormat(
                "yyyy-MM-dd");
        String date = sim.format(targetDate);//targetDate+" 00:00:00"/*c.getTime()*/);
        dates.add(date);
        for (int i = 0; i < count - 1; i++) {
            dates.add(addDay(date, i + 1));
            /*c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);*/
        }
        return dates;
    }

    public static String addDay(String s, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);//增加一天
            //cd.add(Calendar.MONTH, n);//增加一个月
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }

    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        return stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }


    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mDay;
    private static String mWay;

    /**
     * 获取当前年月日
     *  
     *
     * @return
     */
    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth)))) {
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth))));
        }
        return mYear + "-" + (mMonth.length() == 1 ? "0" + mMonth : mMonth) + "-" + (mDay.length() == 1 ? "0" + mDay : mDay);
    }

    /**
     * 得到当年当月的最大日期
     **/
    public static int MaxDayFromDay_OF_MONTH(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0                 
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
    }


    /**
     * 根据当前日期获得是星期几
     *  
     *
     * @return
     */
    public static int getWeek(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
           return getWeek(format.parse(time));
        } catch (ParseException e) {
            QDLogger.e(e);
        }
        return 1;
    }

    /**
     * 1 #SUNDAY
     * 2 #MONDAY
     * 3 #TUESDAY
     * 4 #WEDNESDAY
     * 5 #THURSDAY
     * 6 #FRIDAY
     * 7 #SATURDAY
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static String getWeekName(Context context, Date date) {
        String[] weeknames = context.getResources().getStringArray(R.array.weeknames);
        //国际上是以星期日为一周第一天
        return weeknames[getWeek(date)-1];
    }
    public static String getWeekName(Context context, String time) {
        String[] weeknames = context.getResources().getStringArray(R.array.weeknames);
        //国际上是以星期日为一周第一天
        return weeknames[getWeek(time)-1];
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static Date getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            QDLogger.e(e);
        }
        //Log.d("CGQ","前7天==" + dft.format(endDate));
        return endDate;//dft.format(endDate);
    }

    public static Date getPreDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            QDLogger.e(e);
        }
        //Log.d("CGQ","前7天==" + dft.format(endDate));
        return endDate;//dft.format(endDate);
    }

    //获取本周
    public static long getTimeOfWeekStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());
        return ca.getTimeInMillis();
    }

    //获取本周
    public static String getTimeOfWeekStartDate() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        //国外以周日为第一天，国内以周一为开始
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());

        mYear = String.valueOf(ca.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(ca.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(ca.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth)))) {
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth))));
        }
        // return ca.getTimeInMillis();
        return mYear + "-" + (mMonth.length() == 1 ? "0" + mMonth : mMonth) + "-" + (mDay.length() == 1 ? "0" + mDay : mDay);
    }


    public static long getTimeOfMonthStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTimeInMillis();
    }

    public static long getTimeOfYearStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_YEAR, 1);
        return ca.getTimeInMillis();
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static MyDate getToday() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
        return getToday(timeZone);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static MyDate getToday(TimeZone timeZone) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timeZone);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int hour = 0;
        if (cal.get(Calendar.AM_PM) == 0)
            hour = cal.get(Calendar.HOUR);
        else
            hour = cal.get(Calendar.HOUR) + 12;
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        //my_time_1 = year + "-" + month + "-" + day;
        //my_time_2 = hour + "-" + minute + "-" + second;
        return new MyDate(year, month, day, hour, minute, second);
    }

    public static class MyDate {
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;

        public MyDate(int year, int month, int day, int hour, int minute, int second) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }

        public String getYearMonthDay() {
            return year + "-" + StringUtil.formatNumberToStr(month, 2) + "-" + StringUtil.formatNumberToStr(day, 2);
        }

        public String getHourMinuteSecond() {
            return StringUtil.formatNumberToStr(hour, 2) + ":" + StringUtil.formatNumberToStr(minute, 2) + ":" + StringUtil.formatNumberToStr(second, 2);
        }

        public String getHourMinute() {
            return StringUtil.formatNumberToStr(hour, 2) + ":" + StringUtil.formatNumberToStr(minute, 2);
        }

        public String getDateTimeStr() {
            return getYearMonthDay() + " " + getHourMinuteSecond();
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }


}
