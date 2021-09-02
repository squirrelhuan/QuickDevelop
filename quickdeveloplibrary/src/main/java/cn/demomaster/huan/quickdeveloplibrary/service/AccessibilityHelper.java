package cn.demomaster.huan.quickdeveloplibrary.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.helper.install.InstallService;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 无障碍服务帮助类
 */
public class AccessibilityHelper {
    public static final String TAG = AccessibilityHelper.class.getSimpleName();
    final static Map<Integer, QDAccessibilityService.OnAccessibilityListener> listenerMap = new HashMap<>();

    public static void registerAccessibilityEventListener(int code, QDAccessibilityService.OnAccessibilityListener listener) {
        listenerMap.put(code, listener);
    }

    public static void unRegisterAccessibilityEventListener(int code) {
        listenerMap.remove(code);
    }

    public static void unRegisterAccessibilityEventListener(QDAccessibilityService.OnAccessibilityListener listener) {
        listenerMap.remove(listener);
    }

    public static void onAccessibilityEvent(AccessibilityService accessibilityService, AccessibilityEvent event) {
        for (Map.Entry entry : listenerMap.entrySet()) {
            ((QDAccessibilityService.OnAccessibilityListener) entry.getValue()).onAccessibilityEvent(accessibilityService, event);
        }
    }

    /**
     * 服务开启时回调
     *
     * @param service
     */
    public static void onServiceConnected(QDAccessibilityService service) {
        qdAccessibilityService = service;
        resetPackage(qdAccessibilityService);
        for (Map.Entry entry : listenerMap.entrySet()) {
            ((QDAccessibilityService.OnAccessibilityListener) entry.getValue()).onServiceConnected(qdAccessibilityService);
        }
    }

    public static void onServiceDestroy() {
        for (Map.Entry entry : listenerMap.entrySet()) {
            ((QDAccessibilityService.OnAccessibilityListener) entry.getValue()).onServiceDestroy();
        }
    }

    static QDAccessibilityService qdAccessibilityService;

    public static QDAccessibilityService getService() {
        return qdAccessibilityService;
    }

    public final static Map<String, String> packageMap = new HashMap<>();

    static void addPackagesToMap(String[] packages) {
        if (packages != null) {
            for (String packageName : packages) {
                packageMap.put(packageName, packageName);
            }
        }
    }

    static String[] getPackagesFromMap() {
        String[] packageNameNew = new String[packageMap.size()];
        int i = 0;
        for (Map.Entry entry : packageMap.entrySet()) {
            packageNameNew[i] = (String) entry.getKey();
            i++;
        }
        return packageNameNew;
    }

    /**
     * 添加要监听的包名
     *
     * @param packageName
     */
    public static void addPackage(String packageName) {
        packageMap.put(packageName, "");
        resetPackage(qdAccessibilityService);
    }

    /**
     * 重置服务包名（需要服务启动时执行）
     *
     * @param qdAccessibilityService
     */
    public static void resetPackage(QDAccessibilityService qdAccessibilityService) {
        if (qdAccessibilityService == null) {
            return;
        }
        AccessibilityServiceInfo serviceInfo = qdAccessibilityService.getServiceInfo();
        if (serviceInfo != null) {
            addPackagesToMap(serviceInfo.packageNames);
            serviceInfo.packageNames = getPackagesFromMap();
            qdAccessibilityService.setServiceInfo(serviceInfo);
        }
    }

    //开启无障碍服务,需要WRITE_SECURE_SETTINGS权限
    public static void openAccessibilityService(Context context, Class<? extends AccessibilityService> accessibilityService) {
        String serviceClassName = accessibilityService.getCanonicalName();
        try {
            String targetID = context.getPackageName() + "/" + serviceClassName;
            if (getService() != null) {
                QDLogger.i( "无障碍服务已开启" + targetID);
                return;
            }
            ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
            String enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            //QDLogger.i(TAG,"无障碍的辅助服务 enabledServices："+enabledServices);
            String newValue = "";
            if (TextUtils.isEmpty(enabledServices)) {
                newValue = targetID;
            } else if (!enabledServices.contains(targetID)) {
                newValue = enabledServices + ":" + targetID;
            }
            if (enabledServices.contains(targetID)) {
                newValue = enabledServices;
            }
            //QDLogger.i(TAG,"无障碍的辅助服务 newValue："+newValue);
            Settings.Secure.putString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, newValue);
            Settings.Secure.putString(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, "1");
            String strs = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            QDLogger.i(TAG, "无障碍的辅助服务设置值：" + strs);
        } catch (Exception e) {
            //授权方法：开启usb调试并使用adb工具连接手机，执行 adb shell pm grant org.autojs.autojspro android.permission.WRITE_SECURE_SETTING
            //QdToast.show(context.getApplicationContext(),"请确保已给予 WRITE_SECURE_SETTINGS 权限授权代码已复制，请使用adb工具连接手机执行(重启不失效)"+ e.toString());
            //QDLogger.e("adb shell pm grant "+context.getApplicationContext().getPackageName()+" android.permission.WRITE_SECURE_SETTINGS"+",e="+e.toString());
            QDLogger.e(e);
        }
    }

    /**
     * 判断是否存在置顶的无障碍服务
     *
     * @param clazz
     * @return
     */
    public boolean isAccessibilityServiceRunning(Context context, Class<? extends QDAccessibilityService> clazz) {
        String name = clazz.getCanonicalName();
        String targetId = context.getPackageName() + "/" + name;
        AccessibilityManager am = (AccessibilityManager) context.getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        int accessibilityEnabled = 0;
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        try {
            String enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            Log.i(TAG, "已开启的无障碍服务：" + enabledServices);
            accessibilityEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED);//"1开启":"0关闭"
        } catch (Settings.SettingNotFoundException e) {
            QDLogger.e(e);
        }
        List<AccessibilityServiceInfo> enableServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        if (accessibilityEnabled == 1) {
            if (enableServices != null) {
                for (AccessibilityServiceInfo enableService : enableServices) {
                    Log.i(TAG, "当前服务：" + enableService.getId() + ",目标服务：" + targetId);
                    if (enableService.getId().trim().equalsIgnoreCase(targetId.trim())) {
                        Log.e(TAG, "服务已开启：" + targetId);
                        return true;
                    }
                }
            } else {
                Log.e(TAG, "FEEDBACK_GENERIC " + enableServices.toString() + "不包含" + targetId);
            }
        }
        return false;
    }

    /**
     * 判断无障碍辅助功能是否开启
     *
     * @param context
     * @return
     */
    public static boolean isAccessBilityOn(Context context) {
        String service = context.getPackageName() + "/" + InstallService.class.getCanonicalName();
        try {
            int i = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            if (i == 1) {
                String settingVlue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                String[] arr = settingVlue.split(":");
                if (arr != null && arr.length > 0) {
                    for (String name : arr) {
                        if (service.equalsIgnoreCase(name)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Settings.SettingNotFoundException e) {
            QDLogger.e(e);
        }
        return false;
    }

    /**
     * 判断服务是否开启
     *
     * @param context
     * @param clazz
     * @return
     */
    public static boolean isEnable(Context context, Class<? extends AccessibilityService> clazz) {
        String name = context.getPackageName() + "/" + clazz.getCanonicalName();
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);

        //List<AccessibilityServiceInfo> installedAccessibilityServiceList = am.getInstalledAccessibilityServiceList();
        if (serviceInfos != null) {
            for (AccessibilityServiceInfo serviceInfo : serviceInfos) {
                if (name.equals(serviceInfo.getId())) {
                    //QDLogger.e("无障碍服务已开启："+name);
                    return true;
                }
            }
        }
        return false;
    }

    public static List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewClass(AccessibilityNodeInfo nodeInfo, String viewClass) {
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = new ArrayList<>();
            if (nodeInfo.getClassName().equals(viewClass)) {
                list.add(nodeInfo);
            }
            if (nodeInfo.getChildCount() > 0) {
                for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                    List<AccessibilityNodeInfo> list2 = findAccessibilityNodeInfosByViewClass(nodeInfo.getChild(i), viewClass);
                    if (list2 != null) {
                        list.addAll(list2);
                    }
                }
            }
            return list;
        }
        return null;
    }
}
