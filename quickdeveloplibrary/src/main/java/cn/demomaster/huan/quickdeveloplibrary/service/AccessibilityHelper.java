package cn.demomaster.huan.quickdeveloplibrary.service;

import static cn.demomaster.qdrouter_library.base.activity.QuickActivity.TAG;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.install.InstallService;
import cn.demomaster.huan.quickdeveloplibrary.view.floatview.ServiceHelper;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * 无障碍服务帮助类
 */
public class AccessibilityHelper {
    
    //开启无障碍服务,需要WRITE_SECURE_SETTINGS权限
    public static void openAccessibilityService(Context context, Class<? extends AccessibilityService> service) {
        String serviceClassName = service.getCanonicalName();
        try {
            QDLogger.i(ServiceHelper.serverIsRunning(context,service.getName())?"无障碍服务【已开启】":"无障碍服务【未开启】");
            String targetID = context.getPackageName() + "/" + serviceClassName;
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
            QDLogger.println("无障碍辅助服务值：" + strs);
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
            QDLogger.i("已开启的无障碍服务：" + enabledServices);
            accessibilityEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED);//"1开启":"0关闭"
        } catch (Settings.SettingNotFoundException e) {
            QDLogger.e(e);
        }
        List<AccessibilityServiceInfo> enableServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        if (accessibilityEnabled == 1) {
            if (enableServices != null) {
                for (AccessibilityServiceInfo enableService : enableServices) {
                    QDLogger.i("当前服务：" + enableService.getId() + ",目标服务：" + targetId);
                    if (enableService.getId().trim().equalsIgnoreCase(targetId.trim())) {
                        QDLogger.i( "服务已开启：" + targetId);
                        return true;
                    }
                }
            } else {
                QDLogger.e( "FEEDBACK_GENERIC " + enableServices.toString() + "不包含" + targetId);
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
                if (arr.length > 0) {
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

    public static void autoInput(Context context, AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Android 5.0 版本及以上：
            QdThreadHelper.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    boolean b = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    Log.e("auto", "自动输入:" + text + "," + b);
                    //nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {//Android 4.3 版本及以上：
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            CharSequence txt = nodeInfo.getText();
            Bundle arguments = new Bundle();
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, text.length());
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void clickView(AccessibilityEvent event, List<AccessibilityNodeInfo> button) {
        if (button != null) {
            for (AccessibilityNodeInfo item : button) {
                String name2 = event.getClassName().toString();
                //Log.i(TAG,  "eventType="+eventType+",packages:" + item.getPackageName() + ",name:" + item.getClassName()+",text:"+event.getText());
                Log.i(TAG, "找到控件[" + item.getText() + "]:" + item.getViewIdResourceName() + ",enable:" + item.isEnabled() + ",isClickable:" + item.isClickable() + ",类型:" + name2);
                AccessibilityNodeInfo nodeInfo = findClickableView(item);
                click(nodeInfo);
            }
        }
    }

    public static void click(AccessibilityNodeInfo nodeInfo) {
        // 执行长按操作
        if (nodeInfo != null) {
            //Log.i(TAG, "点击[" + item.getText() + "]:" + item.getViewIdResourceName());
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            Log.i(TAG, "未找到可点击按钮");
        }
    }

    public static AccessibilityNodeInfo findClickableView(AccessibilityNodeInfo item) {
        if (item == null) {
            return null;
        }
        if (item.isClickable() && item.isEnabled()) {
            return item;
        } else {
            AccessibilityNodeInfo nodeInfo = item.getParent();
            if (nodeInfo != null) {
                if (nodeInfo.isEnabled() && nodeInfo.isClickable()) {
                    return nodeInfo;
                } else {
                    return findClickableView(nodeInfo.getParent());
                }
            }
            return null;
        }
    }
}
