package cn.demomaster.huan.quickdevelop.view.picktime.data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author squirrel桓
 * @date 2019/1/16.
 * description：
 */
public class CalendarTimeItemModel extends DateTimeModel implements Serializable {

    /**
     * vacantTimeArrangement : {"2019-01-17":[{"rawIds":[],"repeatIds":[1],"startAt":"08:3030","endAt":"12:0000"}]}
     * appointmentTime : {"2019-01-16":["08:00:00"]}
     */

    private  List<CalendarTimeModel.Bean> vacantTimeArrangement;
    private LinkedHashMap<String, List<String>> appointmentTime;

    private String date;

    public List<CalendarTimeModel.Bean> getVacantTimeArrangement() {
        return vacantTimeArrangement;
    }

    public void setVacantTimeArrangement(List<CalendarTimeModel.Bean> vacantTimeArrangement) {
        this.vacantTimeArrangement = vacantTimeArrangement;
    }

    public LinkedHashMap<String, List<String>> getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LinkedHashMap<String, List<String>> appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
