package cn.demomaster.huan.quickdeveloplibrary.helper.toast;

import android.content.Context;
import android.widget.Toast;

import static cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper.runOnUiThread;

public class QdToast {
    private static Context context;

    public static void setContext(Context context) {
        QdToast.context = context.getApplicationContext();
    }

    public static void show(Object object) {
        show(context,object==null?"null":object.toString(),Toast.LENGTH_LONG);
    }
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
