package cn.demomaster.huan.quickdeveloplibrary.util;

import android.content.Context;
import android.util.Log;

import cn.demomaster.huan.quickdeveloplibrary.R;

/**
 * @author squirrel桓
 * @date 2018/11/21.
 * description：
 */
public class DisplayUtil {

    static int statusBar_Height = 0;
    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (statusBar_Height != 0) {
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
    }

    /* dp转换成px
     */
    public static int dp2px(Context context, float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
