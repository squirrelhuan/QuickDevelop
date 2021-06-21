package cn.demomaster.huan.quickdeveloplibrary.model;

import java.io.Serializable;

public class EventMessage implements Serializable {
    public int code;
    public Object obj;

    public EventMessage(int eventType) {
        this.code = eventType;
    }

    public EventMessage(int eventCode, Object eventObj) {
        this.code = eventCode;
        this.obj = eventObj;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
