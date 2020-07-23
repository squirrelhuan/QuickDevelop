package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;

import java.lang.reflect.Field;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * @author squirrel桓
 * @date 2018/11/21.
 * description：
 */
public class DisplayUtil {

    static int statusBar_Height = -1;
    /**
     * 获取状态栏高度
     * @return
     */
   /* public static int getStatusBarHeight(Context context) {
        if (statusBar_Height != -1) {
            return statusBar_Height;
        }
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (result == 0) {
            return (int) context.getResources().getDimension(R.dimen.activity_statebar_height);
        }
        return result;
    }*/

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if(QMUIDeviceHelper.isXiaomi()){
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId);
            }
            return 0;
        }
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            if(x > 0){
                return context.getResources().getDimensionPixelSize(x);
            }
        } catch (Exception e) {
            QDLogger.e(e);
        }
        return 0;
    }

    /**
     * 获取标题栏高度
     * @param context
     * @return
     */
    public static int getActionbarDefaultHeight(Context context) {
        if (statusBar_Height != -1) {
            return statusBar_Height;
        }
        int result = 0;
        int resourceId = context.getResources().getIdentifier("actionbar_default_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (result == 0) {
            return (int) context.getResources().getDimension(R.dimen.activity_statebar_height);
        }
        return result;
    }
    static int actionBarHeight = -1;
    /**
     * 获取ActionBar高度
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            }
        } else if (context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /* dp转换成px
     */
    public static int dp2px(Context context, float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }


    /**
     * 根据手机分辨率从DP转成PX
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static float getDimension(Context context,String value){
        float r = 0f;
        if(value!=null){
            value = value.trim();
            if(value.endsWith("dp")){

            }
            if(value.endsWith("dip")){
               String a = value.replace("dip","");
                r = dip2px(context,Float.valueOf(a));
            }
            if(value.endsWith("sp")){
                String a = value.replace("sp","");
                r = sp2px(context,Float.valueOf(a));
            }
            if(value.endsWith("pt")){

            }
            if(value.endsWith("px")){

            }
            if(value.endsWith("mm")){

            }
            if(value.endsWith("in")){

            }
        }
        return r;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

}
