package cn.demomaster.huan.quickdeveloplibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

public class QDViewUtil {
    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


}
