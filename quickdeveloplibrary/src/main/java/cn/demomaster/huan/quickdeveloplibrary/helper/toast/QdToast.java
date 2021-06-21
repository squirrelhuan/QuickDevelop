package cn.demomaster.huan.quickdeveloplibrary.helper.toast;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import cn.demomaster.huan.quickdeveloplibrary.helper.toast.compat.ToastCompat;

import static cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper.runOnUiThread;

public class QdToast {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    public static void show(Object object) {
        show(mContext, object == null ? "null" : object.toString(), Toast.LENGTH_LONG);
    }

    public static void show(Context context, String text) {
        show(context, text, Toast.LENGTH_LONG);
    }

    public static void show(Context context, String text, int time) {
        if (context == null) {
            return;
        } else if (mContext == null) {
            mContext = context.getApplicationContext();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {//处理Android7.1上系统bug
                    ToastCompat.makeText(mContext, text, time)
                            .setBadTokenListener(toast -> {
                                Log.e("failed toast", "" + text);
                            }).show();
                } else {
                    Toast.makeText(mContext, text, time).show();
                }
            }
        });
    }

}
