package cn.demomaster.huan.quickdeveloplibrary.util.system;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

import cn.demomaster.huan.quickdeveloplibrary.constant.SharedPreferencesConstant;

/**
 * @author squirrel桓
 * @date 2019/4/17.
 * description：
 */
public class QDLanguageUtil {
    public static void setLanguageLocal(Context context, String language){
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(SharedPreferencesConstant.System_Language_Setting, language);
        editor.commit();
    }

    public static String getLanguageLocal(Context context){
        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString(SharedPreferencesConstant.System_Language_Setting, "");
        return language;
    }

    public static void changeAppLanguage(Context context) {
        String sta = getLanguageLocal(context);
        if (sta != null && !"".equals(sta)) {
            // 本地语言设置
            Locale myLocale = new Locale(sta);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                conf.setLocale(myLocale);
            } else {
                conf.locale = myLocale;
            }
            res.updateConfiguration(conf, dm);
        }
    }

    /**
     * 切换语言并且立马应用
     * @param context
     */
    public static void changeAppLanguageAndRefreshUI(Activity context) {
        changeAppLanguage(context);
        context.recreate();//刷新界面
    }
}
