package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import android.graphics.Color;
import android.view.Gravity;

public class AdsResource {
    public static final int textColorDefault = Color.WHITE;
    public static final int textBackgroundColorDefault = 0x99000000;
    public static final int textSizeDefault = 18;
    public static final int backgroundColorDefault = Color.TRANSPARENT;
    public static final int textGravityDefault = Gravity.CENTER;
    public static final int layout_gravityDefault = Gravity.BOTTOM;

    int type = -1;//参考BannerContentType
    int from = 0;//参考BannerFileType
    int duration = 0;
    String url;
    String filePath;
    String desc;
    long stayTime;
    int textColor = textColorDefault;
    int textBackgroundColor = textBackgroundColorDefault;
    int backgroundColor = backgroundColorDefault;
    float textSize = 18;
    int resId;

    int layout_gravity = layout_gravityDefault;//UNSPECIFIED_GRAVITY;
    int textGravity = textGravityDefault;//UNSPECIFIED_GRAVITY;
    float[] radius = new float[8];

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLayout_gravity() {
        return layout_gravity;
    }

    public void setLayout_gravity(int layout_gravity) {
        this.layout_gravity = layout_gravity;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
    }

    public int getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(int textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public long getStayTime() {
        return stayTime;
    }

    public void setStayTime(long stayTime) {
        this.stayTime = stayTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getFrom() {
        return from;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public float[] getRadius() {
        return radius;
    }

    public void setRadius(float[] radius) {
        this.radius = radius;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
