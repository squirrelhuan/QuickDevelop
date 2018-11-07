package cn.demomaster.huan.quickdeveloplibrary.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Set;

/**
 * Created by Squirrel桓 on 2018/9/9.
 */
public class SharedPreferencesHelper {

    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;

    private static Context context;

    public static SharedPreferencesHelper getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    //必须要在application里初始化
    public static SharedPreferencesHelper init(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    public SharedPreferencesHelper(Context context) {
        this.context = context;
        this.sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    public int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }

    public long getLong(String key, long def) {
        return sharedPreferences.getLong(key, def);
    }

    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public Set<String> getStringSet(String key, Set<String> value) {
        return sharedPreferences.getStringSet(key, value);
    }

    public <T> T getObject(Class<T> clazz) {
        String key = getKey(clazz);
        String json = getString(key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public void putObject(Object object) {
        String key = getKey(object.getClass());
        Gson gson = new Gson();
        String json = gson.toJson(object);
        putString(key, json);
    }

    public static String getKey(Class<?> clazz) {
        return clazz.getName();
    }

    //默认sampleid
    public static final String Sample_Id_Def = "Sample_Id_Def";
    //更新
    public static final String Setting_AutoUpdate = "AutoUpdate";
    //动画
    public static final String Setting_ActivityAnimation = "ActivityAnimation";
    //最近验证码发送时间
    public static final String Message_Code_Last_Time = "Message_Code_Last_Time";
}