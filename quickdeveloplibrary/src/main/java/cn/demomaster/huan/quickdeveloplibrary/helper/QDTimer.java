package cn.demomaster.huan.quickdeveloplibrary.helper;


import android.os.Handler;

import cn.demomaster.huan.quickdeveloplibrary.base.OnReleaseListener;

//import static cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences.Message_Code_Last_Time;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：
 */
public class QDTimer implements OnReleaseListener {

    //1.手机号，2button,3默认text,4等待text,5,onReciveSmsCode,6time
    private long totalTime = 60;
    private OnTimerListener onTimerListener;
    public QDTimer() {

    }
    public QDTimer(OnTimerListener onReceiveSmsCodeListener) {
        this.onTimerListener = onReceiveSmsCodeListener;
    }
    public QDTimer(long time, OnTimerListener onReceiveSmsCodeListener) {
        this.onTimerListener = onReceiveSmsCodeListener;
        this.totalTime = time;
    }

    public void setOnTimerListener(OnTimerListener onTimerListener) {
        this.onTimerListener = onTimerListener;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    /**
     * 立即开始计时
     */
    public void start(){
        //初始化
        startTime = System.currentTimeMillis();
        //QDSharedPreferences.getInstance().setLong(Message_Code_Last_Time, System.currentTimeMillis());
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }
    public void resetTime(){
        startTime = System.currentTimeMillis();
        //QDSharedPreferences.getInstance().setLong(Message_Code_Last_Time, System.currentTimeMillis());
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }
    public void stop(){
        handler.removeCallbacks(runnable);
        /*if(onTimerListener!=null) {
            onTimerListener.onTimeChange(0);
        }*/
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            validateCode();
        }
    };
    private long startTime;
    private void validateCode() {
        long last = startTime;
        long now = System.currentTimeMillis();
        long diff = getSecond(now - last);
        if (diff > totalTime) {
            handler.removeCallbacks(runnable);
        } else {
            if(onTimerListener!=null) {
                onTimerListener.onTimeChange(totalTime - diff);
            }
            handler.postDelayed(runnable, 1000);
        }
    }

    //时间戳转字符串
    public static long getSecond(long diff) {
        //以秒为单位
        Long second = (diff / 1000);
        return second;
    }

    @Override
    public void onRelease() {
        handler.removeCallbacksAndMessages(null);
    }

    public static interface OnTimerListener{
        //读秒状态不可点击
        void onTimeChange(long time);
    }

    public void destory(){
        if(onTimerListener!=null) {
            onTimerListener = null;
        }
        if(handler!=null){
            handler.removeCallbacks(runnable);
            handler.removeCallbacksAndMessages(null);
            runnable = null;
        }
        handler=null;
    }

}