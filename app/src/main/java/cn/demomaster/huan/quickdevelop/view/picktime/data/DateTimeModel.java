package cn.demomaster.huan.quickdevelop.view.picktime.data;

import java.util.List;

/**
 * @author squirrel桓
 * @date 2018/11/27.
 * description：
 */
public class DateTimeModel {


    private String title;//笔名(如周五)
    private String date;//日期
    private boolean isFree;//医生是否有空
    private boolean isSpecial;//每月初
    private boolean isAvailable;//两周内日期可选择
    private boolean isToday;//是否是今日
    private String week;

    private List<DurationTime> durationTimeList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DurationTime> getDurationTimeList() {
        return durationTimeList;
    }

    public void setDurationTimeList(List<DurationTime> durationTimeList) {
        this.durationTimeList = durationTimeList;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public static class DurationTime{
        private int from;
        private int end;

        /*public DurationTime(int from, int end) {
            this.from = from;
            this.end = end;
        }*/

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

}
