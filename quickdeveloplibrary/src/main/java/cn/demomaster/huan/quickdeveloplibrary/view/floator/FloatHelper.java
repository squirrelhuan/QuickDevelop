package cn.demomaster.huan.quickdeveloplibrary.view.floator;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.internal.FlowLayout;

import cn.demomaster.huan.quickdeveloplibrary.helper.QDActivityManager;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;


/**
 * 伪悬浮帮助类，再每个acitvity的decorView布局中添加view
 */
public class FloatHelper {
    public static int contenId=131415;

    Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            onActivityResume(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            onActivityPause(activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            detachView(activity);
        }
    };

    public FloatHelper() {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            contenId = View.generateViewId();
        }*/
    }

   /* public int getFloatViewId(Activity activity,View flowView){
       return activity.hashCode()+flowView.hashCode();
    }*/

    private void onActivityPause(Activity activity) {
       floatView.onPause(activity);
    }

    boolean isShow = true;

    public void hideFloat(Activity activity) {
        isShow = false;
        detachView(activity);
    }

    public void hideFloatView() {
        isShow = false;
        detachView(QDActivityManager.getInstance().getCurrentActivity());
    }

    private void onActivityResume(Activity activity) {
        //QDLogger.d(Tag, "onActivityResume" + floatView);
        if (!isShow) {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View view = decorView.findViewById(contenId);
            if (view != null && view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            return;
        }
        if (floatView != null) {
            //QDLogger.d(Tag, "floatView!=null");
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View view = decorView.findViewById(contenId);
            if (view == null) {
                attachView(activity);
            } else {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                //QDLogger.d(Tag, "layoutParams="+layoutParams);
                if(layoutParams instanceof ViewGroup.MarginLayoutParams){
                    ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = pointX;
                    ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = pointY;
                }

                layoutParams.height = height;
                layoutParams.width = width;
                view.setLayoutParams(layoutParams);
                ViewGroup.LayoutParams layoutParams1 = ((ViewGroup) view).getChildAt(0).getLayoutParams();
                layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                ((ViewGroup) view).getChildAt(0).setLayoutParams(layoutParams1);
                 floatView.onResume(activity);
            }
        }
    }

    public static String Tag = "floatHelper";

    private void attachView(Activity activity) {
       // QDLogger.d(Tag, "attachView getParent=" + activity);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View view = decorView.findViewById(contenId);
        if (view == null) {
            FlowLayout flowLayout = new FlowLayout(activity);
            flowLayout.setId(contenId);
            flowLayout.setOnTouchListener(floatingOnTouchListener);
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(width, height);
            layoutParams.leftMargin = pointX;
            layoutParams.topMargin = pointY;
            flowLayout.setTag("");
            //QDLogger.e(Tag,"height="+height+",width="+width+",pointX="+pointX+"，pointY="+pointY);

            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            flowLayout.addView(floatView.onCreateView(activity), layoutParams1);
            decorView.addView(flowLayout, layoutParams);
            //QDLogger.d(Tag,"flowLayout.getId="+flowLayout.getId());
        }
    }

    private void detachView(Activity activity) {
        //QDLogger.d(Tag, "detachView");
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View view = decorView.findViewById(contenId);
        if (view != null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    Application application;
    int screenWidth;
    int screenHeight;
    FloatingOnTouchListener floatingOnTouchListener;
    public void init(Context context) {
        if (application == null) {
            application = ((Application) context.getApplicationContext());
            application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

            screenWidth = DisplayUtil.getScreenWidth(context);
            screenHeight = DisplayUtil.getScreenHeight(context);
            floatingOnTouchListener = new FloatingOnTouchListener(this);
        }
    }

    FloatView floatView;
    public void addFloatView(Activity activity, FloatView view) {
        isShow = true;
        init(activity);
        floatView =view;
        height = floatView.getSize().y;
        width = floatView.getSize().x;
        pointX = floatView.leftTopPoint().x;
        pointY = floatView.leftTopPoint().y;
        contenId = floatView.hashCode();
        //QDLogger.d(Tag,"init contenId="+contenId);
        attachView(activity);
    }

    private int width;
    private int height;
    private static int pointX;
    private static int pointY;

    public void updateSize( int width, int height) {
        this.width = width;
        this.height = height;
        ViewGroup decorView = (ViewGroup) QDActivityManager.getInstance().getCurrentActivity().getWindow().getDecorView();
        ViewGroup view = decorView.findViewById(contenId);
        if (view != null) {
            //QDLogger.d(Tag,"跟新大小="+width+","+height+"   "+view.getContext());
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
            view.requestLayout();
           ViewGroup.LayoutParams layoutParams1 = view.getChildAt(0).getLayoutParams();
            layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.getChildAt(0).setLayoutParams(layoutParams1);
        }
    }

    public ViewGroup getFloatView(Activity currentActivity) {
        ViewGroup decorView = (ViewGroup) currentActivity.getWindow().getDecorView();
        ViewGroup view = decorView.findViewById(contenId);
        return view;
    }

    public ViewGroup getCurrentContentView() {
        return getFloatView(QDActivityManager.getInstance().getCurrentActivity());
    }

    public void onWindowSizeChanged(int movedX, int movedY) {
        //ViewGroup viewGroup = getCurrentContentView();
        height = height + movedY;
        height = Math.min(screenHeight, height);
        width = width + movedX;
        width = Math.min(screenWidth, width);
        updateSize( width, height);
    }

    public static class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        private long startTime = 0;
        private long endTime = 0;
        private boolean isclick;
        FloatHelper floatHelper;

        public FloatingOnTouchListener(FloatHelper floatHelper) {
            this.floatHelper = floatHelper;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            //System.out.println("执行顺序down");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    isclick = false;//当按下的时候设置isclick为false
                    startTime = System.currentTimeMillis();
                    //System.out.println("执行顺序down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    isclick = true;//当按钮被移动的时候设置isclick为true,就拦截掉了点击事件

                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;

                    View targetView =  floatHelper.getCurrentContentView();
                    ViewGroup.MarginLayoutParams  layoutParams = (ViewGroup.MarginLayoutParams) targetView.getLayoutParams();
                        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                            pointX =  layoutParams.leftMargin + movedX;
                            layoutParams.leftMargin = pointX;
                            pointY =  layoutParams.topMargin + movedY;
                            layoutParams.topMargin = pointY;
                        }
                        targetView.setLayoutParams(layoutParams);
                       // QDLogger.e(Tag,"ACTION_MOVE setLayoutParams"+targetView.getContext()+","+targetView.getId()+","+targetView);
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.15 * 1000L) {
                        isclick = true;
                    } else {
                        isclick = false;
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

}
