package cn.demomaster.huan.quickdeveloplibrary.service;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.model.EventMessage;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickevent_library.core.QuickEvent;
import cn.demomaster.quickevent_library.core.Subscribe;
import cn.demomaster.quickevent_library.core.ThreadMode;
import cn.demomaster.quickpermission_library.PermissionHelper;

/**
 * 无障碍辅助服务
 */
public class QDAccessibilityService extends AccessibilityService {
    public static final String TAG = QDAccessibilityService.class.getSimpleName();
    public static QDAccessibilityService instance;
    public static final int QuickAccessibilityService_Event = 1035445;//无障碍服务事件标识

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        QuickEvent.getDefault().register(this);
    }

    //初始化
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        QDLogger.println(TAG, "无障碍服务【开启】");
       // AccessibilityHelper.onServiceConnected(this);
    }

    String currentActivityName;
    public String getCurrentActivityName() {
        return currentActivityName;
    }

    //实现辅助功能
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (event.getClassName() != null) {
                    currentActivityName = event.getClassName().toString();
                }
                break;
        }
        //QDLogger.println("onAccessibilityEvent...");
        QuickEvent.getDefault().post(new EventMessage(QuickAccessibilityService_Event,event));
        //AccessibilityHelper.onAccessibilityEvent(this, event);
        /*
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
                        item.getParent().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                //Log.e(TAG, "窗口切换" + event.getClassName());
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                //Log.e(TAG, "文字改变" + event.getClassName());
                break;
        }*/
    }

    /**
     * 打印view结构树
     *
     * @param accessibilityService
     */
    public static void parseNodeInfo(AccessibilityService accessibilityService) {
        AccessibilityNodeInfo rootNodeInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<AccessibilityWindowInfo> accessibilityNodeInfoList = accessibilityService.getWindows();
            for (AccessibilityWindowInfo accessibilityWindowInfo : accessibilityNodeInfoList) {
                rootNodeInfo = accessibilityWindowInfo.getRoot();
                if (rootNodeInfo != null) {
                    getChildsInfo(rootNodeInfo);
                    //Log.e(TAG, "wid=" + rootNodeInfo.getWindowId());
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
        }*/
        rootNodeInfo.getPackageName();
       /*Log.i(TAG, "窗口Id:" + rootNodeInfo.getWindowId());*/
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static AccessibilityNodeInfo getNodeInfoById(String id) {
        return getNodeInfoById(instance,id);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static AccessibilityNodeInfo getNodeInfoById(AccessibilityService accessibilityService, String id) {
        if (accessibilityService==null||TextUtils.isEmpty(id) || accessibilityService.getRootInActiveWindow() == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityService.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && nodeInfoList.size() > 0) {
            nodeInfoList.get(0).getViewIdResourceName();
            //int count = nodeInfoList.get(0).getChildCount();
            //Log.d(TAG, "ChildCount="+count);
            return nodeInfoList.get(0);
        }
        return null;
    }

    /**
     * 模拟全局按键 performGlobalAction "Android 4.1及以上系统才支持此功能
     */
    @Override
    public void onInterrupt() {
        QDLogger.i(TAG, "辅助功能被迫中断");
    }

    @Override
    public void onDestroy() {
        QuickEvent.getDefault().unRegister(this);
        instance =null;
        super.onDestroy();
        QDLogger.i(TAG, "辅助功能已关闭");
    }

    //跳转系统自带界面 辅助功能界面
    public static void startSettintActivity(Context context) {
        /*Intent intent = new Intent(context, DialogWindowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("permission", Manifest.permission.BIND_ACCESSIBILITY_SERVICE);
        intent.putExtras(bundle);
        (context).startActivity(intent);*/
        PermissionHelper.requestPermission(context,
                new String[]{Manifest.permission.BIND_ACCESSIBILITY_SERVICE}, null);
    }

    /**
     * 添加要监听的包名
     * @param packageName
     */
    public static void addPackage(String packageName) {
        packageMap.put(packageName, "");
        resetPackage();
    }

    /**
     * 重置服务包名（需要服务启动时执行）
     */
    public static void resetPackage() {
        if(instance!=null) {
            AccessibilityServiceInfo serviceInfo = instance.getServiceInfo();
            if (serviceInfo != null) {
                if (serviceInfo.packageNames != null) {
                    for (String packageName : serviceInfo.packageNames) {
                        packageMap.put(packageName, packageName);
                    }
                }
                String[] arr = new String[packageMap.size()];
                int i =0;
                for(Map.Entry entry:packageMap.entrySet()){
                    arr[i] = (String) entry.getKey();
                    i++;
                }
                serviceInfo.packageNames = arr;
                instance.setServiceInfo(serviceInfo);
            }
        }
    }

    public static final Map<String, String> packageMap = new HashMap<>();
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void performGlobalAction(EventMessage eventMessage){
        if(eventMessage!=null) {
            if(!TextUtils.isEmpty(eventMessage.key)&&eventMessage.key.equals("performGlobalAction")) {
                //new EventMessage(1, "performGlobalAction", AccessibilityService.GLOBAL_ACTION_HOME);
                //QuickEvent.getDefault().post(QDAccessibilityService.class, eventMessage);
                performGlobalAction((int) eventMessage.data[0]);
            }
        }
    }

}
