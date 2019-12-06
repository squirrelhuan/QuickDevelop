package cn.demomaster.huan.quickdeveloplibrary.event.listener;

import android.view.View;

/**
 * VIEW 双击事件
 */
public abstract class OnDoubleClickListener extends OnMultClickListener {
    public OnDoubleClickListener() {
        super(2);
    }

    @Override
    public void onClickEvent(View v) {
        onDoubleClick(v);
    }

    public abstract void onDoubleClick(View v);
}
