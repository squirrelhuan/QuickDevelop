package cn.demomaster.huan.quickdeveloplibrary.operatguid;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * Created by Squirrel桓 on 2019/1/12.
 */
public class GuiderHelper {
    private static GuiderHelper instance;

    public static GuiderHelper getInstance() {
        if(instance==null){
            instance = new GuiderHelper();
        }
        return instance;
    }

    private List<GuiderModel> guiderModels =new ArrayList<>();
    private int position;

    public GuiderHelper(){
        guiderModels=new ArrayList<>();
    }

    private   GuiderView guiderSurfaceView;
    private  ViewGroup decorView;
    public void startGuider(Activity context, View view, String Tag){
        //initGuiderView(context,view,Tag);
        decorView = (FrameLayout) context.getWindow().getDecorView();

       guiderSurfaceView = new GuiderView(context, guiderModels.get(position), decorView, new GuiderActionDialog.OnActionFinishListener() {
            @Override
            public void onFinish() {
                if(position>guiderModels.size()-2){
                    //结束；
                    decorView.removeView(guiderSurfaceView);
                }else {
                    //继续下一个
                    position ++;
                    toNextGuider();
                }
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        decorView.addView(guiderSurfaceView, layoutParams);
    }

   public void toNextGuider(){
       guiderSurfaceView.setGuiderModel(guiderModels.get(position));
    }

    public void add(GuiderModel guiderModel){
        position =0;
        guiderModels.add(guiderModel);
    }

    private void initGuiderView(Activity context, View view, String tag) {
        GuiderModel  guiderModel = getGuiderByTag(tag,view);
        RectF rectF = getViewRectF(view);
        GuiderActionDialog guiderActionDialog = new GuiderActionDialog.Builder(context,guiderModel,view).create();
        guiderActionDialog.show();
    }

    private RectF getViewRectF(View view) {
        int[] location;
        location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        System.out.println("view--->x坐标:" + location[0] + "view--->y坐标:" + location[1]);

        float l = location[0];
        float t = location[1];
        float r = location[0]+view.getWidth();
        float b = location[1]+view.getHeight();
        return new RectF(l,t,r,b);
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
