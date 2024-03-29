package cn.demomaster.huan.quickdeveloplibrary.helper;


import android.os.Handler;

import cn.demomaster.qdrouter_library.base.OnReleaseListener;

//import static cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences.Message_Code_Last_Time;

/**
 * @author squirrel桓
 * @date 2018/11/8.
 * description：
 */
public class QDTimer implements OnReleaseListener {

    private long startTime;
    //1.手机号，2button,3默认text,4等待text,5,onReciveSmsCode,6time
    private long totalTime = 60;
    private OnTimerListener onTimerListener;

    private QDTimer() {

    }

    public QDTimer(OnTimerListener onReceiveSmsCodeListener) {
        this.onTimerListener = onReceiveSmsCodeListener;
        init();
    }

    private void init() {
        runnable = new ValidateCodeRunnable(this);
    }

    public QDTimer(long time, OnTimerListener onReceiveSmsCodeListener) {
        this.onTimerListener = onReceiveSmsCodeListener;
        this.totalTime = time;
        init();
    }

    public void setOnTimerListener(OnTimerListener onTimerListener) {
        this.onTimerListener = onTimerListener;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        //QDLogger.i("totalTime=" + totalTime);
    }

    public long getTotalTime() {
        return totalTime;
    }

    /**
     * 立即开始计时
     */
    public void start() {
        //初始化
        startTime = System.currentTimeMillis();
        //QDSharedPreferences.getInstance().setLong(Message_Code_Last_Time, System.currentTimeMillis());
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }

    /**
     * 重置倒计时时间
     */
    public void resetTime() {
        startTime = System.currentTimeMillis();
        //QDSharedPreferences.getInstance().setLong(Message_Code_Last_Time, System.currentTimeMillis());
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }

    public void stop() {
        if(handler!=null) {
            handler.removeCallbacks(runnable);
        }
        /*if(onTimerListener!=null) {
            onTimerListener.onTimeChange(0);
        }*/
    }

    /**
     * 重置0
     */
    public void toStart() {
        if(handler!=null) {
            handler.removeCallbacks(runnable);
        }
        if(onTimerListener!=null) {
            onTimerListener.onTimeChange(0);
        }
    }

    Handler handler = new Handler();
    ValidateCodeRunnable runnable = null;
    public static class ValidateCodeRunnable implements Runnable,OnReleaseListener{
        QDTimer qdTimer;

        public ValidateCodeRunnable(QDTimer qdTimer) {
            this.qdTimer = qdTimer;
        }

        @Override
        public void run() {
            long last = qdTimer.startTime;
            long now = System.currentTimeMillis();
            long diff = getSecond(now - last);
            if (diff > qdTimer.totalTime) {
                qdTimer.handler.removeCallbacks(qdTimer.runnable);
            } else {
                if (qdTimer.onTimerListener != null) {
                    qdTimer.onTimerListener.onTimeChange(qdTimer.totalTime - diff);
                }
                qdTimer.handler.postDelayed(qdTimer.runnable, 1000);
            }
        }

        @Override
        public void onRelease(Object self) {
            qdTimer.handler.removeCallbacks(qdTimer.runnable);
        }
    }
    /*private void validateCode() {
        long last = startTime;
        long now = System.currentTimeMillis();
        long diff = getSecond(now - last);
        if (diff > totalTime) {
            handler.removeCallbacks(runnable);
        } else {
            if (onTimerListener != null) {
                onTimerListener.onTimeChange(totalTime - diff);
            }
            handler.postDelayed(runnable, 1000);
        }
    }*/

    //时间戳转字符串
    public static long getSecond(long diff) {
        //以秒为单位
        return (diff / 1000);
    }

    @Override
    public void onRelease(Object self) {
        onTimerListener = null;
        if(runnable!=null){
            runnable.onRelease(self);
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler.removeCallbacksAndMessages(null);
        }
    }

    public interface OnTimerListener {
        //读秒状态不可点击
        void onTimeChange(long time);
    }
}