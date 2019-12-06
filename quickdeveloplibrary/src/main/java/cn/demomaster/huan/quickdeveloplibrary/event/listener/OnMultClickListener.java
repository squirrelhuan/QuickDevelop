package cn.demomaster.huan.quickdeveloplibrary.event.listener;

import android.view.View;


/**
 * VIEW 多次点击事件
 */
public abstract class OnMultClickListener implements View.OnClickListener {

    private long clickInterval = 1000;//两次间隔
    private long lastClickTime = 0;
    private int clickTime =2;//默认点击clickTime次触发
    private int clicktime_current =0;//默认点击clickTime次触发

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime < clickInterval) {
            clicktime_current=clicktime_current+1;
            if (clicktime_current==clickTime){
                clicktime_current = 0;
                onClickEvent(v);
            }
        }else {//点击中断
            clicktime_current = 0;
        }
        lastClickTime = currentTimeMillis;
    }
    public abstract void onClickEvent(View v);

    public int getClickTime() {
        return clickTime;
    }

    /**
     * 设置两次点击间隔
     * @param clickInterval
     */
    public void setClickInterval(long clickInterval) {
        this.clickInterval = clickInterval;
    }

    public OnMultClickListener(int clickTime) {
        this.clickTime = clickTime;
    }
    public OnMultClickListener(int clickTime,long clickInterval) {
        this.clickTime = clickTime;
        this.clickInterval = clickInterval;
    }
}
