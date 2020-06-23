package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.constant.TAG;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;


/**
 * 系统权限管理
 * Created by huan on 2017/11/14.
 */
public class PermissionManager {

    public static final int REQUEST_PERMISS_COMMON_CODE = 32418;
    //请求悬浮
    public static final int REQUEST_PERMISS_SPECIAL_CODE = 32419;
    private static PermissionManager instance;

    public static PermissionManager getInstance() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    //普通权限
    private static Map<String, String> specialMap = new HashMap<>();
    //需要的特殊权限，需要进入设置页面才能授权的
    private static Map<String, String> commonMap = new HashMap<>();
    private static Map<String, List<String>> groupMap = new HashMap<>();

    public PermissionManager() {
        /*commonMap.put(Manifest.permission.READ_CALENDAR, "允许程序读取用户的日程信息");
        commonMap.put(Manifest.permission.WRITE_CALENDAR, "允许程序写入日程，但不可读取");
        commonMap.put(Manifest.permission.CAMERA, "允许程序访问摄像头进行拍照");
        commonMap.put(Manifest.permission.READ_CONTACTS, "允许程序访问联系人通讯录信息");
        commonMap.put(Manifest.permission.WRITE_CONTACTS, "写入联系人，但不可读取");
        commonMap.put(Manifest.permission.ACCESS_FINE_LOCATION, "允许程序通过GPS芯片接收卫星的定位信息");
        commonMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, "允许程序通过WIFI或移动基站的方式获取用户错略的经纬度信息");
        commonMap.put(Manifest.permission.RECORD_AUDIO, "允许程序录制声音通过手机或耳机的麦克");
        commonMap.put(Manifest.permission.READ_PHONE_STATE, "允许程序访问电话状态");
        commonMap.put(Manifest.permission.CALL_PHONE, "允许程序从非系统拨号器里拨打电话");
        commonMap.put(Manifest.permission.READ_CALL_LOG, "读取通话记录");
        commonMap.put(Manifest.permission.WRITE_CALL_LOG, "允许程序写入（但是不能读）用户的联系人数据");
        commonMap.put(Manifest.permission.USE_SIP, "允许程序使用SIP视频服务");
        commonMap.put(Manifest.permission.PROCESS_OUTGOING_CALLS, "允许程序监视，修改或放弃播出电话");
        commonMap.put(Manifest.permission.BODY_SENSORS, "访问与您生命体征相关的传感器数据");
        commonMap.put(Manifest.permission.SEND_SMS, "允许程序发短信");
        commonMap.put(Manifest.permission.RECEIVE_SMS, "允许程序接受短信");
        commonMap.put(Manifest.permission.READ_SMS, "允许程序读取短信内容");
        commonMap.put(Manifest.permission.RECEIVE_WAP_PUSH, "允许程序接收WAP PUSH信息");
        commonMap.put(Manifest.permission.RECEIVE_MMS, "允许程序接收彩信");
        commonMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, "可以读取设备外部存储空间");
        commonMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "允许程序写入外部存储");

        specialMap.put(Manifest.permission.SYSTEM_ALERT_WINDOW, "允许弹出悬浮窗");
        specialMap.put(Manifest.permission.INSTALL_PACKAGES, "允许安装应用");*/

        commonMap.put(Manifest.permission.READ_CALENDAR, Manifest.permission_group.CALENDAR);
        commonMap.put(Manifest.permission.WRITE_CALENDAR, Manifest.permission_group.CALENDAR);
        commonMap.put(Manifest.permission.CAMERA, Manifest.permission_group.CAMERA);
        commonMap.put(Manifest.permission.READ_CONTACTS, Manifest.permission_group.CONTACTS);
        commonMap.put(Manifest.permission.WRITE_CONTACTS, Manifest.permission_group.CONTACTS);
        commonMap.put(Manifest.permission.GET_ACCOUNTS, Manifest.permission_group.CONTACTS);
        commonMap.put(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission_group.LOCATION);
        commonMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission_group.LOCATION);
        commonMap.put(Manifest.permission.RECORD_AUDIO, Manifest.permission_group.MICROPHONE);
        commonMap.put(Manifest.permission.READ_PHONE_STATE, Manifest.permission_group.PHONE);
        commonMap.put(Manifest.permission.CALL_PHONE, Manifest.permission_group.PHONE);
        commonMap.put(Manifest.permission.READ_CALL_LOG, Manifest.permission_group.PHONE);
        commonMap.put(Manifest.permission.ADD_VOICEMAIL, Manifest.permission_group.PHONE);
        commonMap.put(Manifest.permission.WRITE_CALL_LOG, Manifest.permission_group.PHONE);
        commonMap.put(Manifest.permission.USE_SIP, Manifest.permission_group.PHONE);
        commonMap.put(Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission_group.PHONE);
        commonMap.put(Manifest.permission.BODY_SENSORS, Manifest.permission_group.SENSORS);
        commonMap.put(Manifest.permission.SEND_SMS, Manifest.permission_group.SMS);
        commonMap.put(Manifest.permission.RECEIVE_SMS, Manifest.permission_group.SMS);
        commonMap.put(Manifest.permission.READ_SMS, Manifest.permission_group.SMS);
        commonMap.put(Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission_group.SMS);
        commonMap.put(Manifest.permission.RECEIVE_MMS, Manifest.permission_group.SMS);
        //commonMap.put(Manifest.permission.READ_CALL_BROADCASTS, Manifest.permission_group.SMS);
        commonMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.CALENDAR);
        commonMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission_group.CALENDAR);

        specialMap.put(Manifest.permission.SYSTEM_ALERT_WINDOW, "允许弹出悬浮窗");
        specialMap.put(Manifest.permission.INSTALL_PACKAGES, "允许安装应用");
        specialMap.put(Manifest.permission.PACKAGE_USAGE_STATS, "数据包状态查看");
        specialMap.put(Manifest.permission.WRITE_SETTINGS, "修改系统设置");

/*
        List<String> group7 = new ArrayList<>();
        group7.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        group7.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        groupMap.put(Manifest.permission_group.STORAGE,group7);

        List<String> group8 = new ArrayList<>();
        group8.add(Manifest.permission.SEND_SMS);
        group8.add(Manifest.permission.RECEIVE_SMS);
        group8.add(Manifest.permission.READ_SMS);
        group8.add(Manifest.permission.RECEIVE_WAP_PUSH);
        group8.add(Manifest.permission.RECEIVE_MMS);
        groupMap.put(Manifest.permission_group.SMS,group8);

        List<String> group7 = new ArrayList<>();
        group7.add(Manifest.permission.BODY_SENSORS);
        groupMap.put(Manifest.permission_group.SENSORS,group7);

        List<String> group6 = new ArrayList<>();
        group6.add(Manifest.permission.READ_PHONE_STATE);
        group6.add(Manifest.permission.CALL_PHONE);
        group6.add(Manifest.permission.READ_CALL_LOG);
        group6.add(Manifest.permission.WRITE_CALL_LOG);
        group6.add(Manifest.permission.ADD_VOICEMAIL);
        group6.add(Manifest.permission.USE_SIP);
        group6.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        groupMap.put(Manifest.permission_group.PHONE,group6);

        List<String> group5 = new ArrayList<>();
        group5.add(Manifest.permission.RECORD_AUDIO);
        groupMap.put(Manifest.permission_group.MICROPHONE,group5);

        List<String> group4 = new ArrayList<>();
        group4.add(Manifest.permission.ACCESS_FINE_LOCATION);
        group4.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        groupMap.put(Manifest.permission_group.LOCATION,group4);

        List<String> group3 = new ArrayList<>();
        group3.add(Manifest.permission.READ_CONTACTS);
        group3.add(Manifest.permission.WRITE_CONTACTS);
        group3.add(Manifest.permission.GET_ACCOUNTS);
        groupMap.put(Manifest.permission_group.CONTACTS,group3);

        List<String> group2 = new ArrayList<>();
        group2.add(Manifest.permission.CAMERA);
        groupMap.put(Manifest.permission_group.CAMERA,group2);

        List<String> group1 = new ArrayList<>();
        group1.add(Manifest.permission.READ_CALENDAR);
        group1.add(Manifest.permission.WRITE_CALENDAR);
        groupMap.put(Manifest.permission_group.CALENDAR,group1);*/

    }

    //特殊权限弹窗后进入设置页面处理
    public static void showPermissionSettingDialog(final Context context, String permission, PermissionListener onCheckPermissionListener) {
        switch (permission) {
            case Manifest.permission.INSTALL_PACKAGES:
                //弹框提示用户手动打开
                showAlert(context, "安装权限", "需要打开允许来自此来源，请去设置中开启此权限", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //此方法需要API>=26才能使用
                        startInstallPermissionSettingActivity(context);
                    }
                });
                break;
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                requestMap.put(REQUEST_PERMISS_SPECIAL_CODE, onCheckPermissionListener);
                //弹框提示用户手动打开
                showAlert(context, "悬浮窗权限", "需要打开在其他应用中显示", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //此方法需要API>=26才能使用
                        getOverlayPermission((Activity) context);
                    }
                });
                break;
            case Manifest.permission.PACKAGE_USAGE_STATS:
                requestMap.put(REQUEST_PERMISS_SPECIAL_CODE, onCheckPermissionListener);
                //弹框提示用户手动打开
                showAlert(context, "查看网络访问状态", "需要打开在其他应用中显示", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent3 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        ((Activity) context).startActivityForResult(intent3, REQUEST_PERMISS_SPECIAL_CODE);
                    }
                });
                break;
            case Manifest.permission.WRITE_SETTINGS:
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
        }
    }

    static AlertDialog.Builder builder;
    static AlertDialog dialog;

    /**
     * alert 消息提示框显示
     *
     * @param context  上下文
     * @param title    标题
     * @param message  消息
     * @param listener 监听器
     */
    public static void showAlert(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", listener);
        builder.setCancelable(false);
        //builder.setIcon(R.mipmap.quickdevelop_ic_launcher);
        dialog = builder.create();
        dialog.show();
    }

    //跳转到设置-请求悬浮窗权限
    @TargetApi(Build.VERSION_CODES.M)
    public static void getOverlayPermission(Activity context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent, REQUEST_PERMISS_SPECIAL_CODE);
    }

    // 跳转到设置-允许安装未知来源-页面
    @TargetApi(Build.VERSION_CODES.O)
    public static void startInstallPermissionSettingActivity(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri packageURI = Uri.parse("package:" + context.getPackageName());
            //注意这个是8.0新API
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isGrantedUsagePremission(Context context) {
        boolean granted;
        AppOpsManager appOps = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(
                    android.Manifest.permission.PACKAGE_USAGE_STATS)
                    == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }

    //获取普通权限是否可用
    public static boolean getPermissionStatus(Context context, String permissionName) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switch (permissionName) {
                case Manifest.permission.INSTALL_PACKAGES:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        return context.getPackageManager().canRequestPackageInstalls();
                    }
                    break;
                case Manifest.permission.SYSTEM_ALERT_WINDOW:
                    return Settings.canDrawOverlays(context);
                case Manifest.permission.PACKAGE_USAGE_STATS:
                    return isGrantedUsagePremission(context);
                case Manifest.permission.WRITE_SETTINGS:
                    return Settings.System.canWrite(context);
                case Manifest.permission.WRITE_SECURE_SETTINGS:
                    return Settings.System.canWrite(context);
                default:
                    //Toast.makeText(context,"unknow permission",Toast.LENGTH_SHORT).show();
                    int r = ContextCompat.checkSelfPermission(context.getApplicationContext(), permissionName);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    return (r == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    //普通权限
    public static boolean getPermissionStatus(Context context, String[] permissions) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (String str : permissions) {
                if (specialMap.containsKey(str)) {//特殊权限
                    if (!getPermissionStatus(context, str)) {
                        return false;
                    }
                } else {//普通权限
                    if (!getPermissionStatus(context, str)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void chekPermission(Context context, String[] permissions, PermissionListener listener) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //用map去重
            Map<String, String> map = new HashMap<>();
            for (String str : permissions) {
                map.put(str, str);
            }
            List<String> list = new ArrayList(map.keySet());
            if (list != null && list.size() > 0) {
                String str = list.get(0);
                //TODO 获取统一分组
                List<String> list1 = getStringListGroup(map, str);
                if (specialMap.containsKey(str)) {//处理特殊权限
                    //permissions1.add(str);
                    if (getPermissionStatus(context, str)) {//检查特殊权限
                        List<String> list3 = removePassedFromList2(list, str);
                        if (list3 == null || list3.size() == 0) {
                            if (listener != null)
                                listener.onPassed();
                        } else {
                            chekPermission(context, list3.toArray(new String[list3.size()]), listener);
                        }
                    } else {//弹窗申请权限
                        showPermissionSettingDialog(context, str, new PermissionListener() {
                            @Override
                            public void onPassed() {
                                List<String> list3 = removePassedFromList2(list, str);
                                if (list3 == null || list3.size() == 0) {
                                    if (listener != null)
                                        listener.onPassed();
                                } else {
                                    chekPermission(context, list3.toArray(new String[list3.size()]), listener);
                                }
                            }

                            @Override
                            public void onRefused() {
                                chekPermission(context, list.toArray(new String[list.size()]), listener);
                            }
                        });
                    }
                } else {//处理普通动态权限
                    String[] targest = list1.toArray(new String[list1.size()]);
                    QDLogger.i(TAG.PERMISSION, Arrays.asList(targest));
                    chekSignPermission(context, targest, new PermissionListener() {
                        @Override
                        public void onPassed() {
                            List<String> list3 = removePassedFromList(list, targest);
                            if (list3.size() == 0) {
                                if (listener != null)
                                    listener.onPassed();
                            } else {
                                chekPermission(context, list3.toArray(new String[list3.size()]), listener);
                            }
                        }

                        @Override
                        public void onRefused() {
                            if (listener != null)
                                listener.onRefused();
                        }
                    });
                }
            }
        } else {
            if (listener != null)
                listener.onPassed();
        }
    }

    private List<String> removePassedFromList2(List<String> list, String str) {
        for (int j = 0; j < list.size(); j++) {
            if (str.equals(list.get(j))) {
                list.remove(j);
                return list;
            }
        }
        return null;
    }

    /**
     * 移除已通过的权限
     *
     * @param list
     * @param strings
     * @return
     */
    private static List<String> removePassedFromList(List<String> list, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (strings[i].equals(list.get(j))) {
                    list.remove(j);
                    return removePassedFromList(list, strings);
                }
            }
        }
        return list;
    }

    /**
     * 获取同一个组的权限
     *
     * @param map
     * @param str
     * @return
     */
    private static List<String> getStringListGroup(Map<String, String> map, String str) {
        List<String> stringList = new ArrayList<>();
        String groupName = commonMap.get(str);
        for (Map.Entry entry : commonMap.entrySet()) {
            if (entry.getValue().equals(groupName)) {//同一个组
                if (map.containsKey(entry.getKey())) {
                    stringList.add((String) entry.getKey());
                }
            }
        }
        return stringList;
    }

    public void chekSignPermission(Context context, String[] permissions, PermissionListener listener) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            boolean useful = getPermissionStatus(context, permissions);
            if (!useful) {
                // 如果没有授予该权限，就去提示用户请求
                //showDialogTipUserRequestPermission(context, permissions, listener);
                startRequestPermission(context, permissions, listener);
            } else {
                listener.onPassed();
            }
        } else {
            listener.onPassed();
        }
    }

    private static Map<Integer, PermissionListener> requestMap = new HashMap<>();

    // 开始提交请求权限
    private void startRequestPermission(final Context context, String[] permissions, PermissionListener listener) {
        requestMap.put(REQUEST_PERMISS_COMMON_CODE, listener);
        ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_PERMISS_COMMON_CODE);
    }

    //部分权限需要延时后生效
    public void showProcess() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新检查所有权限是否可用
                //initPermission();
            }
        }, 1000);
    }

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults) {
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (REQUEST_PERMISS_COMMON_CODE == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            PermissionListener listener = requestMap.get(REQUEST_PERMISS_COMMON_CODE);
            requestMap.remove(REQUEST_PERMISS_COMMON_CODE);
            if (listener != null) {
                //如果有权限没有被允许
                if (hasPermissionDismiss) {
                    listener.onRefused();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                } else {
                    //全部权限通过，可以进行下一步操作。。。
                    listener.onPassed();
                }
            }
        }
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISS_SPECIAL_CODE:
                PermissionListener listener = requestMap.get(REQUEST_PERMISS_SPECIAL_CODE);
                if (listener != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(context)) {
                            listener.onPassed();
                        } else {
                            listener.onRefused();
                        }
                    } else {
                        listener.onPassed();
                    }
                }
                requestMap.remove(REQUEST_PERMISS_SPECIAL_CODE);
                break;
        }
    }

    /**
     * 监听器，监听权限是否通过
     */
    public static interface PermissionListener {
        void onPassed();//通过

        void onRefused();//不通过
    }
}