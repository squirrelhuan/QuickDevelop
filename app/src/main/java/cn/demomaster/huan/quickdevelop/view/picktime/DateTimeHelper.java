package cn.demomaster.huan.quickdevelop.view.picktime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.demomaster.huan.quickdevelop.view.picktime.data.CalendarTimeItemModel;
import cn.demomaster.huan.quickdevelop.view.picktime.data.DateTimeModel;
import cn.demomaster.huan.quickdeveloplibrary.util.DateTimeUtil;


/**
 * @author squirrel桓
 * @date 2019/2/11.
 * description：时间帮助类
 */
public class DateTimeHelper {

    public static List<DateTimeModel> getDateByCount(int dayCount) {
        List<DateTimeModel> lists = new ArrayList<>();
        //获取当前日期
        /*String today = DateTimeUtil.StringData();
        //获取是星期几
        int weekIndex = DateTimeUtil.getWeek(today);
        int oldCount = weekIndex - 2;
        Date oldDate = DateTimeUtil.getOldDate(-oldCount);//获取前-n天的日期*/
        String week = "";
        Date date = new Date();
        List<String> dateList = DateTimeUtil.getDateAsCount(date, dayCount);
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


    public static List<DateTimeModel> getDate() {
        List<DateTimeModel> dateTimeModels;
        dateTimeModels = DateTimeHelper.getDateByCount(15);//（获取两周是14天，多向后获取一天以作备用）
        DateTimeUtil.MyDate myDate = DateTimeUtil.getToday();
        if (myDate.getHour() >= 20) {//晚上八点以后不可预约了，删掉当日日期向后推一天
            dateTimeModels.remove(0);
        } else {//没有晚上八点当日可预约了，删掉多出来的最后一天
            DateTimeModel dateTimeModel = dateTimeModels.get(0);
            dateTimeModel.setToday(true);
            dateTimeModels.set(0, dateTimeModel);
            dateTimeModels.remove(dateTimeModels.size() - 1);
        }
        return dateTimeModels;
    }

    public static List<CalendarTimeItemModel> getDateTimeList() {
        return getDateTimeList(0);
    }

   /* public static List<CalendarTimeItemModel> getDateTimeList2Len() {
        return getDateTimeList(1);
    }*/

    /**
     * 日历
     *
     * @return
     */
    public static List<CalendarTimeItemModel> getDateTimeList(int type) {
        int todayIndex;
        List<CalendarTimeItemModel> lists = new ArrayList<>();
        //获取当前日期
        String today = DateTimeUtil.StringData();
        //获取是星期几
        int weekIndex = DateTimeUtil.getWeek(today);
        //int oldCount = weekIndex - 2;
        int oldCount = (weekIndex == 1 ? 6 : (weekIndex - 2));
        Date oldDate = DateTimeUtil.getOldDate(-oldCount);//获取前-n天的日期
        String week = "";
        List<String> dateList = DateTimeUtil.getDateAsCount(oldDate, 21);
        boolean isFree = false;
        boolean isAvailable = false;
        int availableCount = 0;
        int i = 0;
        for (String s : dateList) {
            CalendarTimeItemModel dateTimeModel = new CalendarTimeItemModel();
            System.out.println(s);
            if (s.equals(DateTimeUtil.StringData())) {
                todayIndex = i;
                dateTimeModel.setToday(true);
                // week = "今天";
                week = s.substring(s.length() - 5, s.length());
                if (type == 1) {
                    week = week.replace("-", "月") + "日";
                } else {
                    week = Integer.valueOf(week.split("-")[0]) + "月" + Integer.valueOf(week.split("-")[1]) + "日";
                }
                isAvailable = true;
                isFree = true;
                dateTimeModel.setSpecial(true);//特殊日期需要标红
            } else {
                //每逢15或初一 ，显示为 YY-DD
                int day = Integer.valueOf(s.substring(s.length() - 2, s.length()));
                if (day == 1) {
                    week = s.substring(s.length() - 5, s.length());
                    if (type == 1) {
                        week = week.replace("-", "月") + "日";
                    } else {
                        week = Integer.valueOf(week.split("-")[0]) + "月" + Integer.valueOf(week.split("-")[1]) + "日";
                    }
                    dateTimeModel.setSpecial(false);//特殊日期需要标红
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
            }
            dateTimeModel.setTitle(week);
            dateTimeModel.setDate(s);
            dateTimeModel.setFree(isFree);
            lists.add(dateTimeModel);
            i = i + 1;
        }
        return lists;
    }


}
