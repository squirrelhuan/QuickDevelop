package cn.demomaster.huan.quickdevelop.view.picktime;

import java.util.List;

/**
 * @author squirrel桓
 * @date 2018/12/5.
 * description：
 */
public class HourModel {
    private String date;
    private String hour;
    private List<String> child;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public List<String> getChild() {
        return child;
    }

    public void setChild(List<String> child) {
        this.child = child;
    }
}
