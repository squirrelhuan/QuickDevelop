package cn.demomaster.huan.quickdeveloplibrary.view.floator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

public interface FloatView {

    View onCreateView(Activity activity);
    void onResume(Activity activity);

    Point getSize();
    Point leftTopPoint();

    void onPause(Activity activity);
    //void onPause(Activity activity, View windowView);
    //void onDestory(Activity activity, View windowView);

}
