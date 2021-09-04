package cn.demomaster.huan.quickdeveloplibrary.network;

import android.content.Context;
import android.content.Intent;

public interface NetStateChangedListener {
    void onConnected(Context context, Intent intent);
    void onDisConnected(Context context, Intent intent);
}
