package cn.demomaster.huan.quickdeveloplibrary.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Set;

import cn.demomaster.huan.quickdeveloplibrary.exception.QDException;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * Created by Squirrel桓 on 2018/9/9.
 */
public class QDSharedPreferences {
    
    private static QDSharedPreferences instance;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    private SharedPreferences sharedPreferences;

    private static Context context;

    public static QDSharedPreferences getInstance() {
        if (instance == null) {
            instance = new QDSharedPreferences(context);
        }
        return instance;
    }

    //必须要在application里初始化
    public static QDSharedPreferences init(Context context) {
        if (instance == null) {
            instance = new QDSharedPreferences(context);
        }
        return instance;
    }

    public QDSharedPreferences(Context context) {
        if (context == null) {
            try {
                throw new QDException("SharedPreferences 创建失败:context为空");
            } catch (QDException e) {
                QDLogger.e(e);
            }
            return;
        }
        QDSharedPreferences.context = context.getApplicationContext();
        this.sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(QDSharedPreferences.context);
        /*context.getSharedPreferences(String name,int mode)
        MODE_PRIVATE:私有
        MODE_APPED:追加
        MODE_ENABLE_WRITE_AHEAD_LOGGING:
        MODE_MULTI_PROCESS：
        其中MODE_WORLD_READABLE(全局读)和MODE_WORLD_WRITEABLE(全局写)已经过时了。*/
    }

    public void putBoolean(String key, boolean value) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key, boolean value) {
        try {
            return sharedPreferences.getBoolean(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public int getInt(String key, int def) {
        try {
            return sharedPreferences.getInt(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public float getFloat(String key, float def) {
        try {
            return sharedPreferences.getFloat(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public long getLong(String key, long def) {
        try {
            return sharedPreferences.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void setLong(String key, long value) {
        putLong(key, value);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String getString(String key, String def) {
        try {
            return sharedPreferences.getString(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    public Set<String> getStringSet(String key, Set<String> def) {
        try {
            return sharedPreferences.getStringSet(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public <T> T getObject(Class<T> clazz) {
        String key = getKey(clazz);
        return getObject(key, clazz);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String json = getString(key, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            QDLogger.e(e);
            return null;
        }
    }

    public void putObject(Object object) {
        String key = getKey(object.getClass());
        putObject(key, object);
    }

    public void putObject(String key, Object object) {
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
    //public static final String Message_Code_Last_Time = "Message_Code_Last_Time";


    /**
     * 清空存储
     */
    public void clear() {
        //Map<String,?> objectMap = QDSharedPreferences.getInstance().getSharedPreferences().getAll();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}