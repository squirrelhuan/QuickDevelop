package cn.demomaster.huan.quickdeveloplibrary.event.listener;

import android.view.View;
import android.widget.AdapterView;


/**
 * VIEW 禁止重复点击事件
 */
public abstract class OnSingleItemClickListener implements AdapterView.OnItemClickListener {

    private long clickInterval = 1000;//两次间隔
    private long lastClickTime = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime > clickInterval) {
            lastClickTime = currentTimeMillis;
            onItemClickEvent(parent,view,position,id);
        }
    }

    public abstract void onItemClickEvent(AdapterView<?> parent, View view, int position, long id);

    /**
     * 设置两次点击间隔
     *
     * @param clickInterval
     */
    public void setClickInterval(long clickInterval) {
        this.clickInterval = clickInterval;
    }
    public OnSingleItemClickListener(long clickInterval) {
        this.clickInterval = clickInterval;
    }
    public OnSingleItemClickListener() { }
}
