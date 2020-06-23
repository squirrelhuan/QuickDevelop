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
    private int textColor = -1;//-1默认不使用
    private int textSize = -1;
    private int textBackgroundColor = -1;//文字框背景色
    private int lineColor = -1;
    private int lineWidth = -1;
    private int imgResourceId =-1;//图片
    private int imgWidth =-1;//图片
    private int imgHeight =-1;//图片
    private float imgScale=1;//图片缩放比例
    private GuidActionType complateType;//结束触发类型
    private GuidActionType startType;//开始触发类型
    private TouchType touchType;

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public float getImgScale() {
        return imgScale;
    }

    public void setImgScale(float imgScale) {
        this.imgScale = imgScale;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

    public void setImgResourceId(int imgResourceId) {
        if(imgResourceId!=0&&imgResourceId!=-1){
            shape = SHAPE.img;
        }
        this.imgResourceId = imgResourceId;
    }

    public int getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(int textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
    }

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
        straight,curve//直线/曲线
    }
    public enum SHAPE{
        roundedrectangle,rectangle,oval,img
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
