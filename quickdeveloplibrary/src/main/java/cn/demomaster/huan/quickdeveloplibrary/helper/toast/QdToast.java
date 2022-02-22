package cn.demomaster.huan.quickdeveloplibrary.helper.toast;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.demomaster.huan.quickdeveloplibrary.R;
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
        runOnUiThread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {//处理Android7.1上系统bug
                ToastCompat.makeText(mContext, text, time)
                        .setBadTokenListener(toast -> {
                            Log.e("failed toast", "" + text);
                        }).show();
            } else {
                Toast.makeText(mContext, text, time).show();
            }
        });
    }


    /**************************       自定义toast       *********************************/
    private static Toast customToast;

    public static void showToast(Context context, String msg) {
        showToast(context,msg, Toast.LENGTH_SHORT,null);
    }
    public static void showToast(Context context, String msg, int duration, QuickCustomToast.ToastInflater toastInflate) {
        try {
            if (context == null) {
                return;
            }
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            if (customToast != null) {
                customToast.cancel();
                customToast = null;
            }
            customToast = new QuickCustomToast(context, msg,duration,toastInflate);//自定义
            customToast.show();
        } catch (Exception e) {
            e.printStackTrace();
            //可能在子线程调了这个方法
        }
    }

    //使用自定义toast
    private static class QuickCustomToast extends Toast {
        ToastInflater mToastInflater;

        public QuickCustomToast(Context context, String msg, int duration, ToastInflater toastInflate) {
            super(context);
            newFtoast(context, msg, duration, toastInflate);
        }

        public QuickCustomToast(Context context, String msg, int duration) {
            super(context);
            newFtoast(context, msg, duration, null);
        }

        public void newFtoast(Context context, String msg, int duration, ToastInflater toastInflate) {
            mToastInflater = toastInflate == null ? new DefToastInflater() : toastInflate;
            View root = mToastInflater.createView(this,context);
            if (root != null) {
                mToastInflater.bindData(this,root,msg);
                setDuration(duration);
                setView(root); //这是setView。就是你的自定义View
                //必须设置Gravity.FILL_HORIZONTAL 这个选项，布局文件的宽高才会正常显示
                setGravity(Gravity.CENTER | Gravity.FILL_HORIZONTAL, 0, 0); //这是，放着顶部，然后水平放满屏幕
            }
        }

        public static class DefToastInflater implements ToastInflater {

            @Override
            public View createView(Toast toast,Context context) {
                LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflate != null;
                return inflate.inflate(R.layout.quick_toast, null);//加载自定义的XML布局
            }

            @Override
            public void bindData(Toast toast,View root, String msg) {
                TextView txtContent = (TextView) root.findViewById(R.id.txtToast);
                if (txtContent != null) {
                    txtContent.setText(msg);
                }
            }
        }

        public static interface ToastInflater {
            View createView(Toast toast,Context context);
            void bindData(Toast toast,View root, String msg);
        }
    }
}
