package cn.demomaster.huan.quickdeveloplibrary.helper.toast;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.CircleTextView;


/**
 * Created by Squirrel桓 on 2018/10/30.
 */
public class PopToastUtil {

    static PopupWindow popupWindow;
    static View contentView;
    static CircleTextView messageView;

    public static void ShowToast(Activity context, String text) {
        ShowToast(context,text,Gravity.TOP);
    }
    public static void ShowToastBottom(Activity context, String text) {
        ShowToast(context,text,Gravity.BOTTOM);
    }
    public static void ShowToastCenter(Activity context, String text) {
        ShowToast(context,text,Gravity.CENTER);
    }

    private static void ShowToast(Activity context, String text,int gravity) {
        if(popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        initPopToast(context,text,0,gravity);
    }

    private static int backgroundColor = Color.GREEN;
    private static int textColor = Color.WHITE;
    public static void setColorStyle(int textColor1,int backgroundColor1){
        textColor = textColor1;
        backgroundColor = backgroundColor1;
    }

    private static CPopupWindow.PopBuilder builder;
    private static int destinc_Y ;
    private static void initPopToast(Activity context, String text,int time,int gravity) {

        if(builder==null) {
            builder = new CPopupWindow.PopBuilder(context);
            //contentView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_customer_toast, null, false);
            //messageView = contentView.findViewById(R.id.tv_message);
            //popupWindow = builder.setContentView(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true).build();
            messageView = new CircleTextView(context.getApplicationContext());
            messageView.setPadding(DisplayUtil.dp2px(context,25),DisplayUtil.dp2px(context,5),DisplayUtil.dp2px(context,25),DisplayUtil.dp2px(context,5));
            messageView.setTextSize(14);
            popupWindow = builder.setContentView(messageView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true).build();
            popupWindow.setTouchable(false);
            popupWindow.setFocusable(false);
            popupWindow.setAnimationStyle(R.style.cgq_pop_toast);
        }
        messageView.setText(text);
        messageView.setTextColor(textColor);
        messageView.setBackgroundColor(backgroundColor);
        switch (gravity){
            case Gravity.TOP:
                popupWindow.setAnimationStyle(R.style.cgq_pop_toast);
                if(destinc_Y==0){
                    destinc_Y = context.getResources().getDimensionPixelOffset(R.dimen.activity_actionbar_button_height)+ DisplayUtil.getStatusBarHeight(context);
                }
                break;
            case Gravity.BOTTOM:
                popupWindow.setAnimationStyle(R.anim.anim_null);
                if(destinc_Y==0){
                    destinc_Y = context.getResources().getDimensionPixelOffset(R.dimen.activity_actionbar_button_height)+ DisplayUtil.getStatusBarHeight(context);
                }
                break;
            case Gravity.CENTER:
                popupWindow.setAnimationStyle(R.anim.anim_null);
                destinc_Y = 0;
                break;
        }
        popupWindow.showAtLocation(getContentView(context), Gravity.CENTER_HORIZONTAL | gravity, 0, destinc_Y);//dp2px(context,80)
        toastRunable.setPopupWindow(popupWindow);
        mHandler.removeCallbacks(toastRunable);
        timer_t = residenceTime;
        mHandler.postDelayed(toastRunable,time);
    }

    public static View getContentView(Activity context) {
        return context.findViewById(android.R.id.content);
    }

    /**
     * 设置停留时间
     * @param time
     */
    public void setResidenceTime(int time ){
        this.residenceTime = time;
    }

    private static int residenceTime = 2500;
    private static int timer_t = 2500;
    private static Handler mHandler = new Handler();
    private static ToastRunable toastRunable = new ToastRunable() {
        @Override
        public void run() {
            if (timer_t > 0) {
                timer_t -= 100;
                mHandler.postDelayed(this, 100);
            } else {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                mHandler.removeCallbacks(this);
            }
        }
    };

    public static class ToastRunable implements Runnable {
        static PopupWindow popupWindow;
        public void setPopupWindow(PopupWindow popupWindow) {
            this.popupWindow = popupWindow;
        }
        @Override
        public void run() {

        }
    }


}