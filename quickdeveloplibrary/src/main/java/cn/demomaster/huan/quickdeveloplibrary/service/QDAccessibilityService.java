package cn.demomaster.huan.quickdeveloplibrary.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class QDAccessibilityService extends AccessibilityService {
    public static final String TAG = "QDAccessibilityService";
    private static QDAccessibilityService instance;

    public static AccessibilityNodeInfo rootNodeInfo;

    public static AccessibilityService getService() {
        return instance;
    }

    //初始化
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        QDLogger.i(TAG, "辅助功能已开启...");
        instance = this;

        rootNodeInfo = getRootInActiveWindow();
        resetPackage();
    }

    public static Map<String, String> packageMap = new HashMap<>();

    /**
     * 添加要监听的包名
     *
     * @param packageName
     */
    public static void addPackage(String packageName) {
        //添加之前先检查包名是否存在
        QDLogger.i("addPackage =" + packageName);
        packageMap.put(packageName, packageName);
        resetPackage();
    }

    public static void resetPackage() {
        QDLogger.i("准备重置无障碍服务包名1");
        if (getService() != null && getService().getServiceInfo() != null) {
            QDLogger.i("重置无障碍服务包名2");
            if (getService().getServiceInfo().packageNames != null) {
                for (String entry : getService().getServiceInfo().packageNames) {
                    packageMap.put(entry, entry);
                }
            }
            //String[] packageNames = instance.getServiceInfo().packageNames;
            String[] packageNames1 = new String[packageMap.size()];

            int i = 0;
            for (Map.Entry entry : packageMap.entrySet()) {
                packageNames1[i] = (String) entry.getKey();
                i++;
            }
            //packageNames1[packageNames.length] = packageName;
            AccessibilityServiceInfo serviceInfo = instance.getServiceInfo();
            if (packageNames1.length != 0) {
                serviceInfo.packageNames = packageNames1;
            }
            instance.setServiceInfo(serviceInfo);
        }
    }
    static OnAccessibilityEventListener mOnAccessibilityEventListener;

    public static void setOnAccessibilityEventListener(OnAccessibilityEventListener onAccessibilityEventListener) {
        mOnAccessibilityEventListener = onAccessibilityEventListener;
    }

    public static interface OnAccessibilityEventListener{
        void onAccessibilityEvent(AccessibilityService accessibilityService,AccessibilityEvent event);
    }

    //实现辅助功能
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(mOnAccessibilityEventListener!=null){
            mOnAccessibilityEventListener.onAccessibilityEvent(this,event);
        }
        Log.e(TAG, "TYPE_VIEW=" + event.getAction());

        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.i(TAG, "捕获到点击事件");
                AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                if (nodeInfo != null) {
                    // 查找text为Test!的控件
                    List<AccessibilityNodeInfo> button = nodeInfo.findAccessibilityNodeInfosByText("Test!");
                    nodeInfo.recycle();
                    for (AccessibilityNodeInfo item : button) {
                        Log.i(TAG, "long-click button!");
                        // 执行长按操作
                        item.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.e(TAG, "窗口切换" + event.getClassName());
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.e(TAG, "文字改变" + event.getClassName());
                break;
        }
    }

    /**
     * 打印view结构树
     * @param accessibilityService
     */
    public static void parseNodeInfo(AccessibilityService accessibilityService){
        AccessibilityNodeInfo rootNodeInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<AccessibilityWindowInfo> accessibilityNodeInfoList = accessibilityService.getWindows();
            for(AccessibilityWindowInfo accessibilityWindowInfo : accessibilityNodeInfoList){
                rootNodeInfo = accessibilityWindowInfo.getRoot();
                if (rootNodeInfo != null) {
                    getChildsInfo(rootNodeInfo);
                    Log.e(TAG, "wid=" + rootNodeInfo.getWindowId());
                }
            }
        } else {
            rootNodeInfo = accessibilityService.getRootInActiveWindow();
            if (rootNodeInfo != null) {
                getChildsInfo(rootNodeInfo);
            }
        }
    }

    /**
     * @param rootNodeInfo
     */
    public static void getChildsInfo(AccessibilityNodeInfo rootNodeInfo) {
        if (rootNodeInfo != null) {
            if (rootNodeInfo.getChildCount() != 0) {
                int count = rootNodeInfo.getChildCount();
                for (int i = 0; i < count; i++) {
                    AccessibilityNodeInfo viewChild = rootNodeInfo.getChild(i);
                    readChildInfo(viewChild);
                    getChildsInfo(viewChild);
                }
            } else {
                readChildInfo(rootNodeInfo);
            }
        }
    }

    public static void readChildInfo(AccessibilityNodeInfo rootNodeInfo) {
        if (rootNodeInfo == null) return;
        String description = "";
        if (rootNodeInfo.getContentDescription() != null) {
            description = rootNodeInfo.getContentDescription().toString();
        }
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i(TAG, "showDialog:" + rootNodeInfo.canOpenPopup());
        }
        Log.i(TAG, "description=" + description);*/
        rootNodeInfo.getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.i(TAG, "ViewId=" + rootNodeInfo.getViewIdResourceName());
        }
        Log.i(TAG, "视图类型：" + rootNodeInfo.getClassName());
        Log.i(TAG, "文本内容：" + rootNodeInfo.getText());
        Log.i(TAG, "窗口Id:" + rootNodeInfo.getWindowId());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static AccessibilityNodeInfo getNodeInfoById(AccessibilityService accessibilityService, String id) {
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityService.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && nodeInfoList.size() > 0) {
            nodeInfoList.get(0).getViewIdResourceName();
            int count = nodeInfoList.get(0).getChildCount();
            Log.d(TAG, "ChildCount="+count);
            return nodeInfoList.get(0);
        }
        return null;
    }

    /**
     * 模拟全局按键
     *
     * @param service
     */
    public static void recentApps(AccessibilityService service, int action) {
        if (Build.VERSION.SDK_INT < 16) {
            QdToast.show(service, "Android 4.1及以上系统才支持此功能.", Toast.LENGTH_SHORT);
        } else {
            service.performGlobalAction(action);
        }
    }

    @Override
    public void onInterrupt() {
        QDLogger.i(TAG, "辅助功能被迫中断");
        //instance = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QDLogger.i(TAG, "辅助功能已关闭");
        instance = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 公共方法
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 辅助功能是否启动
     */
    public static boolean isStart() {
        return instance != null;
    }

    public static void startSettintActivity(Context context){
        //跳转系统自带界面 辅助功能界面
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    /**
     * 判断是否存在置顶的无障碍服务
     *
     * @param name
     * @return
     */
    public static boolean isAccessibilityServiceRunning(Context context, String name) {
        AccessibilityManager am = (AccessibilityManager) context.getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enableServices
                = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        int accessibilityEnabled = 0;
        try {
            String enabledServices = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            Log.i("AccessibilityService", "enabledServices：" + enabledServices);
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("AccessibilityService", "accessibilityEnabled：" + accessibilityEnabled);
        if (accessibilityEnabled == 1) {
            for (AccessibilityServiceInfo enableService : enableServices) {
                Log.i("AccessibilityService", "无障碍服务id：" + enableService.getId()+",要开启的服务id：" + name);
                if (enableService.getId().trim().endsWith(name.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    //开启无障碍服务,需要WRITE_SECURE_SETTINGS权限
    public static void openAccessibilityService(Context context,String packageName,String servicesName){
        try {
            String enabledServices = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            QDLogger.i("当前已启用的辅助服务:"+enabledServices);
            String addStr = packageName+"/"+servicesName;
            if(!TextUtils.isEmpty(enabledServices)&&enabledServices.contains(addStr)){
                QDLogger.i(context.getApplicationContext(),"无障碍的辅助服务已开启1");
                return;
            }
            String newValue = "";
            if(TextUtils.isEmpty(enabledServices)){
                newValue = addStr;
            } else {
                newValue = enabledServices + ":"+addStr;
            }
            Settings.Secure.putString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, newValue);
            Settings.Secure.putString(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, "1");
            QDLogger.i(context.getApplicationContext(),"无障碍的辅助服务已开启2");
        } catch (Exception e) {
            //授权方法：开启usb调试并使用adb工具连接手机，执行 adb shell pm grant org.autojs.autojspro android.permission.WRITE_SECURE_SETTING
            //QdToast.show(context.getApplicationContext(),"请确保已给予 WRITE_SECURE_SETTINGS 权限授权代码已复制，请使用adb工具连接手机执行(重启不失效)"+ e.toString());
            QDLogger.e("adb shell pm grant "+context.getApplicationContext().getPackageName()+" android.permission.WRITE_SECURE_SETTINGS"+",e="+e.toString());
        }
    }
}
