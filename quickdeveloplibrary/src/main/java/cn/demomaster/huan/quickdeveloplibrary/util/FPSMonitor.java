package cn.demomaster.huan.quickdeveloplibrary.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Choreographer;

public class FPSMonitor implements Choreographer.FrameCallback, Runnable {
    //监控1秒内的帧数
    private static final int MONITOR_TIME = 1000;

    private HandlerThread handlerThread;

    private long startTime = -1;
    private long endTime = -1;

    private long vSyncCount = 0;
    private Handler workHandler;

    public FPSMonitor(){

    }

    public static interface OnFramChangedListener{
        void onChanged(float frame);
    }
    OnFramChangedListener onFramChangedListener;

    public void setOnFramChangedListener(OnFramChangedListener onFramChangedListener) {
        this.onFramChangedListener = onFramChangedListener;
    }

    @Override
    public void run() {
        long duration = (endTime - startTime) / 1000000L;
        float frame = 1000.0f * vSyncCount / duration;
        Log.d("harish", "frame = " + frame + " duration = " + duration);
        if(onFramChangedListener!=null){
            onFramChangedListener.onChanged(frame);
        }
        start();
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        if (startTime == -1){
            startTime = frameTimeNanos;
        }

        vSyncCount++;

        long duration = (frameTimeNanos - startTime) / 1000000L;

        if (duration >= MONITOR_TIME){
            endTime = frameTimeNanos;

            workHandler.post(this);
        }else{
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    public void start(){
        Log.d("harish", "FPSMonitor -- start");

        if (handlerThread == null){
            handlerThread = new HandlerThread("fps monitor thread");
            handlerThread.start();

            workHandler = new Handler(handlerThread.getLooper());
        }

        startTime = -1;
        endTime = -1;
        vSyncCount = 0;

        Choreographer.getInstance().postFrameCallback(this);
    }
}
