package cn.demomaster.huan.quickdeveloplibrary.operatguid;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2019/1/12.
 */
public class GuiderHelper {
    private static GuiderHelper instance;

    public static GuiderHelper getInstance() {
        if (instance == null) {
            instance = new GuiderHelper();
        }
        return instance;
    }

    private List<GuiderModel> guiderModels;
    private int position;

    public GuiderHelper() {
        guiderModels = new ArrayList<>();
    }

    private GuiderView guiderSurfaceView;
    private ViewGroup decorView;

    private int backgroundColor = 0xccffffff;
    private int textColor = Color.WHITE;
    private int lineColor = Color.WHITE;
    private int lineWidth = 2;
    private int textSize = 24;
    private int textPadding = 40;
    private int textBackgroundColor = Color.TRANSPARENT;//文字框背景色

    public void setTextBackgroundColor(int textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
        if(guiderSurfaceView!=null){
            guiderSurfaceView.setTextBackgroundColor(textBackgroundColor);
            guiderSurfaceView.postInvalidate();
        }
    }

    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
        if(guiderSurfaceView!=null){
            guiderSurfaceView.setTextPadding(textPadding);
            guiderSurfaceView.postInvalidate();
        }
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        if(guiderSurfaceView!=null){
            guiderSurfaceView.setTextSize(textSize);
            guiderSurfaceView.postInvalidate();
        }
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        if(guiderSurfaceView!=null){
            guiderSurfaceView.setLineWidth(lineWidth);
            guiderSurfaceView.postInvalidate();
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if(guiderSurfaceView!=null){
            guiderSurfaceView.setTextColor(textColor);
            guiderSurfaceView.postInvalidate();
        }
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        if(guiderSurfaceView!=null){
            guiderSurfaceView.setLineColor(lineColor);
            guiderSurfaceView.postInvalidate();
        }
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        if(guiderSurfaceView!=null){
            guiderSurfaceView.setGuiderBackgroundColor(backgroundColor);
            guiderSurfaceView.postInvalidate();
        }
    }

    public void destory() {
        if (guiderSurfaceView != null && decorView != null && inParentView(decorView)) {
            decorView.removeView(guiderSurfaceView);
        }
        guiderModels.clear();
    }

    private boolean inParentView(ViewGroup decorView) {
       int count = decorView.getChildCount();
       for (int i=0;i<count;i++){
           QDLogger.d("guiderSurfaceView.getClass()="+guiderSurfaceView.getClass().getName()+",decorView.getChildAt(i).getClass()="+decorView.getChildAt(i).getClass().getName());
               if(decorView.getChildAt(i).getClass().equals(guiderSurfaceView.getClass())){
                   return true;
               }
       }
       return false;
    }

    /**
     * 开始展示引导
     * @param context
     * @param view
     * @param Tag
     */
    public void startGuider(Activity context, View view, String Tag) {
        //initGuiderView(context,view,Tag);
        decorView = (FrameLayout) context.getWindow().getDecorView();
        guiderSurfaceView = new GuiderView(context, guiderModels.get(position), decorView, new GuiderActionDialog.OnActionFinishListener() {
            @Override
            public void onFinish() {
                if (position > guiderModels.size() - 2) {
                    //结束；
                    decorView.removeView(guiderSurfaceView);
                } else {
                    //继续下一个
                    position++;
                    toNextGuider();
                }
            }
        });
        guiderSurfaceView.setGuiderBackgroundColor(backgroundColor);
        guiderSurfaceView.setLineColor(lineColor);
        guiderSurfaceView.setTextColor(textColor);
        guiderSurfaceView.setTextSize(textSize);
        guiderSurfaceView.setTextPadding(textPadding);
        guiderSurfaceView.setLineWidth(lineWidth);
        guiderSurfaceView.setTextBackgroundColor(textBackgroundColor);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        decorView.addView(guiderSurfaceView, layoutParams);
    }

    /**
     * 设置下一个引导视图
     */
    public void toNextGuider() {
        guiderSurfaceView.setGuiderModel(guiderModels.get(position));
    }

    /**
     * 添加一个引导模型
     *
     * @param guiderModel
     */
    public void add(GuiderModel guiderModel) {
        position = 0;
        guiderModels.add(guiderModel);
    }

    private void initGuiderView(Activity context, View view, String tag) {
        GuiderModel guiderModel = getGuiderByTag(tag, view);
        RectF rectF = getViewRectF(view);
        GuiderActionDialog guiderActionDialog = new GuiderActionDialog.Builder(context, guiderModel, view).create();
        guiderActionDialog.show();
    }

    private RectF getViewRectF(View view) {
        int[] location;
        location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
       QDLogger.println("view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);

        float l = location[0];
        float t = location[1];
        float r = location[0] + view.getWidth();
        float b = location[1] + view.getHeight();
        return new RectF(l, t, r, b);
    }

    private GuiderModel guiderModel;

    private GuiderModel getGuiderByTag(String tag, View view) {
        /*GuiderModel guiderModel = new GuiderModel();
        guiderModel.setLineType(GuiderModel.LINETYPE.straight);
        guiderModel.setTargetView(new WeakReference<View>(view));
        guiderModel.setMessage("test");
        guiderModel.setComplateType(GuiderModel.GuidActionType.CLICK);*/
        return guiderModel;
    }

    public void setGuiderModel(GuiderModel guiderModel) {
        this.guiderModel = guiderModel;
    }
}
