package cn.demomaster.huan.quickdeveloplibrary.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class EventMessage implements Serializable {
    public int eventType;
    public Object eventObj;
    public EventMessage(int eventType) {
        this.eventType = eventType;
    }
    public EventMessage(int eventType, Object eventObj) {
        this.eventType = eventType;
        this.eventObj = eventObj;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getEventObj() {
        return eventObj;
    }

    public void setEventObj(Object eventObj) {
        this.eventObj = eventObj;
    }
}
