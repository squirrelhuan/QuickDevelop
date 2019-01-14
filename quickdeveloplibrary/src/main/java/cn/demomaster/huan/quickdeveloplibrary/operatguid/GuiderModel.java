package cn.demomaster.huan.quickdeveloplibrary.operatguid;

import android.graphics.Color;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Squirrel桓 on 2019/1/11.
 */
public class GuiderModel {

    private WeakReference<View> targetView;
    private SHAPE shape;
    private LINETYPE lineType;
    private String message;
    private int textColor = Color.WHITE;
    private int textSize = 36;
    private int lineColor = Color.WHITE;
    private int lineWidth = 10;
    private GuidActionType complateType;//结束触发类型
    private GuidActionType startType;//开始触发类型
    private TouchType touchType;

    public WeakReference<View> getTargetView() {
        return targetView;
    }

    public void setTargetView(WeakReference<View> targetView) {
        this.targetView = targetView;
    }

    public SHAPE getShape() {
        return shape;
    }

    public void setShape(SHAPE shape) {
        this.shape = shape;
    }

    public LINETYPE getLineType() {
        return lineType;
    }

    public void setLineType(LINETYPE lineType) {
        this.lineType = lineType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public TouchType getTouchType() {
        return touchType;
    }

    public void setTouchType(TouchType touchType) {
        this.touchType = touchType;
    }

    public GuidActionType getComplateType() {
        return complateType;
    }

    public void setComplateType(GuidActionType complateType) {
        this.complateType = complateType;
    }

    public GuidActionType getStartType() {
        return startType;
    }

    public void setStartType(GuidActionType startType) {
        this.startType = startType;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public enum LINETYPE{
        straight,curve
    }
    public enum SHAPE{
        roundedrectangle,rectangle,oval
    }


    public static enum GuidActionType{
        TOUCH,MOVE,CLICK,LONGCLICK
    }

    /**
     * 目标触摸事件
     */
    public static enum TouchType{
        TargetView,TipView,Other
    }
}
