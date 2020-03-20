package cn.demomaster.huan.quickdeveloplibrary.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

public class QDAccessibilityService extends AccessibilityService {
    private final String TAG = getClass().getName();
    private static QDAccessibilityService instance;

    public static AccessibilityNodeInfo rootNodeInfo;

    public static AccessibilityService getService() {
        return instance;
    }

    //初始化
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        QDLogger.i("辅助功能已开启...");
        instance = this;

        rootNodeInfo = getRootInActiveWindow();
        for(String entry: getService().getServiceInfo().packageNames){
            packageMap.put(entry,entry);
        }
        for(Map.Entry  entry: packageMap.entrySet()){
            addPackage((String)entry.getKey());
        }
    }

    public static Map<String,String> packageMap = new HashMap<>();

    /**
     * 添加要监听的包名
     * @param packageName
     */
    public static void addPackage(String packageName) {
        //添加之前先检查包名是否存在
        QDLogger.i("addPackage ="+packageName);
        packageMap.put(packageName,packageName);
        if(instance!=null&&getService().getServiceInfo()!=null) {
            for (String entry : getService().getServiceInfo().packageNames) {
                packageMap.put(entry, entry);
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
            serviceInfo.packageNames = packageNames1;
            instance.setServiceInfo(serviceInfo);
        }else {
            packageMap.put(packageName,packageName);
        }
    }

    //实现辅助功能
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
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
                getViewTree(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.e(TAG, "窗口切换" + event.getClassName());
                getViewTree(event);
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.e(TAG, "文字改变" + event.getClassName());
                break;
        }
    }

    private void getViewTree(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            // 查找text为Test!的控件
            recycle(nodeInfo);
        }else {
            Log.i(TAG, "nodeInfo null");
        }
    }
    public void recycle(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {
            Log.i(TAG, "child [" + info.getClassName()+"]");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.i(TAG, "showDialog:" + info.canOpenPopup());
            }
            Log.i(TAG, "Text：" + info.getText());
            Log.i(TAG, "windowId:" + info.getWindowId());
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if(info.getChild(i)!=null){
                    recycle(info.getChild(i));
                }
            }
        }
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
        QDLogger.i("(；′⌒`)\r\n红包功能被迫中断");
        instance = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QDLogger.i("%>_<%\r\n红包功能已关闭");
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
}
