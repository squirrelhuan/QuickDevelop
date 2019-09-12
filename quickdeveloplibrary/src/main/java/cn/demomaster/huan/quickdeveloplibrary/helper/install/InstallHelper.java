package cn.demomaster.huan.quickdeveloplibrary.helper.install;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadTask;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.OnDownloadProgressListener;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadHelper.PERMISSIONS_STORAGE;

public class InstallHelper {

    public static void downloadAndInstall(final Activity context, final String name, final String url) {
        //兼容8.0 安装权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                // Toast.makeText(context, "请先开启应用安装权限", Toast.LENGTH_SHORT).show();
                //弹框提示用户手动打开
                showAlert(context, "安装权限", "需要打开允许来自此来源，请去设置中开启此权限", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //此方法需要API>=26才能使用
                        startInstallPermissionSettingActivity(context);
                    }
                });
                return;
            }
        }

        //存储权限
        PermissionManager.chekPermission(context, PERMISSIONS_STORAGE, new PermissionManager.OnCheckPermissionListener() {
            @Override
            public void onPassed() {
                DownloadHelper.DownloadBuilder downloadBuilder = new DownloadHelper.DownloadBuilder(context)
                        .setFileName(name)
                        .setUrl(url)
                        .setOnProgressListener(new OnDownloadProgressListener() {
                            @Override
                            public void onComplete(DownloadTask downloadTask) {
                                QDLogger.i(downloadTask.getFileName() + "下载完成，开始安装" + downloadTask.getDownIdUri().getPath());
                                openAPKFile(context, downloadTask.getDownIdUri());
                                //runInstall(context,downloadTask.getDownIdUri());
                            }

                            @Override
                            public void onProgress(long downloadId, String name, float fraction) {
                                QDLogger.i("下载进度" + fraction);
                            }
                        });
                downloadBuilder.start();
            }

            @Override
            public void onNoPassed() {


            }
        });
    }

    /**
     * 打开安装包
     *
     * @param mContext
     * @param fileUri
     */
    public static void openAPKFile(Activity mContext, Uri fileUri) {
        // DataEmbeddingUtil.dataEmbeddingAPPUpdate(fileUri);
        // 核心是下面几句代码
        if (null != fileUri) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //File apkFile = new File(fileUri);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            Toast.makeText(mContext, "请开启应用安装权限", Toast.LENGTH_SHORT).show();
                            startInstallPermissionSettingActivity(mContext);
                            return;
                        }
                    }
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    mContext.startActivity(intent);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void install(final Activity context, File apkFile) {
        //兼容8.0 安装权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                //弹框提示用户手动打开
                showAlert(context, "安装权限", "需要打开允许来自此来源，请去设置中开启此权限", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //此方法需要API>=26才能使用
                        startInstallPermissionSettingActivity(context);
                    }
                });
                return;
            }
        }
        //安裝
    }

    public static final int INSTALL_PERMISS_CODE = 28757;

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity(Activity context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent, INSTALL_PERMISS_CODE);
    }

    /**
     * alert 消息提示框显示
     *
     * @param context  上下文
     * @param title    标题
     * @param message  消息
     * @param listener 监听器
     */
    public static void showAlert(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", listener);
        builder.setCancelable(false);
        builder.setIcon(R.mipmap.quickdevelop_ic_launcher);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty())
            return false;
        for (int i = 0; i < info.size(); i++) {
            if (pkgName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void runInstall(final Context context, final File file) {
        Uri uri  = Uri.fromFile(file);
        runInstall(context,uri);
    }

    public static void runInstall(final Context context, final Uri uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = installByRoot();
                if (!success){
                    if(isAccessBilityOn(context)){
                        //Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.setDataAndType(uri,"application/vnd.android.package-archive");
                        //context.startActivity(intent);

                        openAPKFile((Activity) context,uri);
                    }else {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        context.startActivity(intent);
                    }
                }
            }
        }).start();
    }

    public static boolean isAccessBilityOn(Context context) {
        int i=0;
        String service = context.getPackageName()+"/"+InstallService.class.getCanonicalName();
        try {
            i=Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),Settings.Secure.ACCESSIBILITY_ENABLED);
            if(i==1){
                String settingVlue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

                String[] arr = settingVlue.split(":");
                if(arr!=null&&arr.length>0){
                    for (String name:arr){
                        if(service.equalsIgnoreCase(name)){
                            return true;
                        }
                    }
                }
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean installByRoot() {
        boolean result = false;
        Process process = null;
        OutputStream os = null;
        String commond = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = process.getOutputStream();
            //
            os.write("pm install -r".getBytes());
            os.flush();
            os.write("exit\n".getBytes());
            process.waitFor();
            br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            if (!sb.toString().contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                os=null;
                br=null;
                process.destroy();
            }
        }
        return result;
    }
}
