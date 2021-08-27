package cn.demomaster.huan.quickdevelop.view.picktime.data;

import java.io.Serializable;

/**
 * @author squirrel桓
 * @date 2018/12/4.
 * description：
 */
public class AreaModel implements Serializable {
    private String areaName;
    private String areaPin;
    private String areaCode;
    private String postCode;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAreaPin() {
        return areaPin;
    }

    public void setAreaPin(String areaPin) {
        this.areaPin = areaPin;
    }
}
