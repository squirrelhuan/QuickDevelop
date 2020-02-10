package cn.demomaster.huan.quickdeveloplibrary.helper.toast;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import static cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper.runOnUiThread;

public class QdToast {

    public static void show(Context context, String text) {
        show(context,text,Toast.LENGTH_LONG);
    }
    public static void show(Context context, String text,int time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, time).show();
            }
        });
    }

}
