package cn.demomaster.huan.quickdeveloplibrary.helper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


/**
 * 系统权限管理
 * Created by huan on 2017/11/14.
 */
public class PermissionManager {
    private static int REQUEST_PERMISS_CODE = 3248;
    //请求悬浮
    public static int SYSTEM_ALERT_WINDOW_CODE = 11004;
    private Activity context;
    private static PermissionManager instance;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    public static PermissionManager getInstance(Activity context) {
        if (instance == null) {
            instance = new PermissionManager(context);
        }
        return instance;
    }

    public PermissionManager(Activity context) {
        this.context = context;
    }

    //普通权限
    private static String[] PERMISSIONS_ALL = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //需要的特殊权限，需要进入设置页面才能授权的
    private static String[] PERMISSIONS_SPE = {Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.INSTALL_PACKAGES};

    //初始化app所需的权限
    public void initPermission() {
        if (builder != null && dialog != null) {
            dialog.dismiss();
        }
        if (getPermissionStatus2(context, PERMISSIONS_SPE)) {//初始化特殊权限
            if (!getPermissionStatus(context, PERMISSIONS_ALL)) {//初始化普通权限
                chekPermission(context, PERMISSIONS_ALL, new PermissionManager.OnCheckPermissionListener() {
                    @Override
                    public void onPassed() {
                        //必须授权所有权限才能使用则，否则嵌套循环
                        initPermission();
                    }

                    @Override
                    public void onNoPassed() {
                        //必须授权所有权限才能使用则，否则嵌套循环
                        initPermission();
                    }
                });
            }
        }
    }

    //特殊权限弹窗后进入设置页面处理
    public void showPermissionSettingDialog(final Context context, String permission) {
        switch (permission) {
            case Manifest.permission.INSTALL_PACKAGES:
                //弹框提示用户手动打开
                showAlert(context, "安装权限", "需要打开允许来自此来源，请去设置中开启此权限", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //此方法需要API>=26才能使用
                        startInstallPermissionSettingActivity((Activity) context);
                    }
                });
                break;
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
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
        }
    }

    //跳转到设置-请求悬浮窗权限
    @TargetApi(Build.VERSION_CODES.M)
    public void getOverlayPermission(Activity context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent, SYSTEM_ALERT_WINDOW_CODE);
    }

    // 跳转到设置-允许安装未知来源-页面
    @TargetApi(Build.VERSION_CODES.O)
    public static void startInstallPermissionSettingActivity(final Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri packageURI = Uri.parse("package:" + context.getPackageName());
            //注意这个是8.0新API
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    //检查安装app权限是否可用
    public static boolean getPermissionStatusByName(Context context, String permissionName) {
        //兼容8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            switch (permissionName) {
                case Manifest.permission.INSTALL_PACKAGES:
                    return context.getPackageManager().canRequestPackageInstalls();
                case Manifest.permission.SYSTEM_ALERT_WINDOW:
                    return Settings.canDrawOverlays(context);
            }
        }
        return true;
    }

    //获取普通权限是否可用
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean getPermissionStatus(Context context, String permissionName) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(context, permissionName);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            return (i == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    //特殊权限
    public boolean getPermissionStatus2(Context context, String[] permissions) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (String str : permissions) {
                if (!getPermissionStatusByName(context, str)) {
                    showPermissionSettingDialog(context, str);
                    return false;
                }
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
                if (!getPermissionStatus(context, str)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void chekPermission(Context context, String[] permissions, OnCheckPermissionListener listener) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            boolean useful = getPermissionStatus(context, permissions);
            if (!useful) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission(context, permissions);
            } else {
                listener.onPassed();
            }
        } else {
            listener.onPassed();
        }
    }

    // 提示用户该请求权限的弹出框
    private static void showDialogTipUserRequestPermission(final Context context, final String[] permission) {
        new AlertDialog.Builder(context)
                .setTitle("权限不可用")
                .setMessage("请开启权限后，再继续操作")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission(context, permission);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "请开启权限后再操作", Toast.LENGTH_LONG).show();
                        instance.initPermission();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private static void startRequestPermission(final Context context, String[] permissions) {
        ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_PERMISS_CODE);
    }

    //部分权限需要延时后生效
    public void showProcess() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新检查所有权限是否可用
                initPermission();
            }
        }, 1000);
    }

    /**
     * 监听器，监听权限是否通过
     */
    public interface OnCheckPermissionListener {
        void onPassed();//通过

        void onNoPassed();//不通过
    }

    /**
     * alert 消息提示框显示
     *
     * @param context  上下文
     * @param title    标题
     * @param message  消息
     * @param listener 监听器
     */
    public void showAlert(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", listener);
        builder.setCancelable(false);
        //builder.setIcon(R.mipmap.ic_launcher);
        dialog = builder.create();
        dialog.show();
    }
}