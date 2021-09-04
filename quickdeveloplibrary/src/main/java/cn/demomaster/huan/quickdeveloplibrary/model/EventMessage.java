package cn.demomaster.huan.quickdeveloplibrary.model;

import java.io.Serializable;
import java.util.Arrays;

public class EventMessage implements Serializable {
    public int code;
    public Object obj;
    public Object[] data;

    public EventMessage(int eventType) {
        this.code = eventType;
    }
    public EventMessage(int eventCode, Object obj) {
        this.code = eventCode;
        this.obj = obj;
    }
    public EventMessage(int eventCode, Object... eventObj) {
        this.code = eventCode;
        this.data = eventObj;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "code=" + code +
                ", obj=" + obj +
                ", data=" + (data==null?"nul":Arrays.toString(data)) +
                '}';
    }
}
