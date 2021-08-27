package cn.demomaster.huan.quickdevelop.view.picktime.data;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.util.DateTimeUtil;

/**
 * @author squirrel桓
 * @date 2019/1/16.
 * description：
 */
public class CalendarTimeModel implements Serializable {


    /**
     * vacantTimeArrangement : {"2019-01-17":[{"rawIds":[],"repeatIds":[1],"startAt":"08:3030","endAt":"12:0000"}]}
     * appointmentTime : {"2019-01-16":["08:00:00"]}
     */

    private LinkedHashMap<String, List<Bean>> vacantTimeArrangement;
    private LinkedHashMap<String, List<String>> appointmentTime;

    public LinkedHashMap<String, List<Bean>> getVacantTimeArrangement() {
        return vacantTimeArrangement;
    }

    public void setVacantTimeArrangement(LinkedHashMap<String, List<Bean>> vacantTimeArrangement) {
        this.vacantTimeArrangement = vacantTimeArrangement;
    }

    public LinkedHashMap<String, List<String>> getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LinkedHashMap<String, List<String>> appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public static class Bean implements Serializable{
        /**
         * rawIds : []
         * repeatIds : [1]
         * startAt : 08:3030
         * endAt : 12:0000
         */

        private String startAt;
        private String endAt;
        private List<String> rawIds;
        private List<String> repeatIds;
        private boolean isAppointmented;//被预约的时间

        public boolean isAppointmented() {
            return isAppointmented;
        }

        public void setAppointmented(boolean appointmented) {
            isAppointmented = appointmented;
        }

        public String getStartAt() {
            return startAt;
        }

        public void setStartAt(String startAt) {
            this.startAt = startAt;
        }

        public String getEndAt() {
            return endAt;
        }

        public void setEndAt(String endAt) {
            this.endAt = endAt;
        }

        public List<String> getRawIds() {
            return rawIds;
        }

        public void setRawIds(List<String> rawIds) {
            this.rawIds = rawIds;
        }

        public List<String> getRepeatIds() {
            return repeatIds;
        }

        public void setRepeatIds(List<String> repeatIds) {
            this.repeatIds = repeatIds;
        }

        public String getStartDateTime(){
            return DateTimeUtil.getToday().getYearMonthDay()+" "+getStartAt()+":00";
        }

        public String getEndDateTime(){
            return DateTimeUtil.getToday().getYearMonthDay()+" "+getEndAt()+":00";
        }

    }

}
