package cn.demomaster.huan.quickdeveloplibrary.constant;

import android.content.Context;
import android.text.TextUtils;

//import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * app xml配置信息
 *
 * @author squirrel桓
 * @date 2018/12/10.
 * description：
 */
public class AppConfig {

    private String configPath;
    private Context mContext;
    private static AppConfig instance;
    private Map<String, Object> configMap;

    //要初始化
    public void load(Context context, String pathName) {
        mContext = context.getApplicationContext();
        configPath = pathName;
        initConfigMap(context);
    }

    public Map<String, Object> getConfigMap(Context context) {
        if (configMap == null) {
            initConfigMap(context);
        }
        return configMap;
    }

    public void initConfigMap(Context context) {
        String conf = QDFileUtil.getFromAssets(context, configPath);
        if (TextUtils.isEmpty(conf)) {
            configMap = null;
            QDLogger.e(new IllegalArgumentException("配置文件初始化失败，未找到指定配置文件/或为空 ，路径：" + configPath));
        }
        //configMap = JSON.parseObject(conf, Map.class);
        Gson gson = new Gson();
        configMap = gson.fromJson(conf, Map.class);
    }

    private AppConfig() {
       /* String conf = FileUtil.getFromAssets(mContext, "project.conf");
        Map<String,Object> map =new HashMap<>();
        map.put("isPatient",true);
        map.put("version",1);
        String cof =  JSON.toJSONString(map);
        Log.i(TAG,cof);
        PopToastUtil.ShowToast(mContext,conf);*/
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public Class getClassFromClassMap(String classNameKey) {
        if (configMap == null) {
            return null;
        }
        String className = (String) configMap.get(classNameKey);
        return getClassByClassName(className);
    }

    public static Class getClassByClassName(String className) {
        Class catClass = null; // 根据给定的类名初始化类
        try {
            catClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            QDLogger.e(e);
        }
        return catClass;
    }

    public Object getConfig(Context context,String key) {
        return getConfigMap(context).get(key);
    }
}
