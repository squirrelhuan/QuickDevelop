package cn.demomaster.huan.quickdeveloplibrary.helper.install;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadTask;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.OnDownloadProgressListener;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static cn.demomaster.huan.quickdeveloplibrary.service.AccessibilityHelper.isAccessBilityOn;
import static cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil.uriToFile;

public class InstallHelper {

    /**
     * 检查下载安装权限
     *
     * @param context
     * @return
     */
    public static boolean checkDownloadPermission(Context context) {
        PermissionHelper.requestPermission(context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, null);
        //兼容8.0 安装权限
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                // QdToast.show(context, "请先开启应用安装权限", Toast.LENGTH_SHORT).show();
                //弹框提示用户手动打开
                showAlert(context, "安装权限", "需要打开允许来自此来源，请去设置中开启此权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //此方法需要API>=26才能使用
                        startInstallPermissionSettingActivity(context);
                    }
                });
                return false;
            }
        }*/
        return true;
    }
    
    public static void downloadAndInstall(final Context context, final String name, final String url) {
        if (checkDownloadPermission(context)) {
            OnDownloadProgressListener onDownloadProgressListener = new OnDownloadProgressListener() {
                @Override
                public void onDownloadRunning(long downloadId, String name, float fraction) {
                    QDLogger.i("下载进度[" + downloadId + "]:" + fraction);
                }
                
                @Override
                public void onDownloadSuccess(DownloadTask downloadTask) {
                    openAPKFile(context, downloadTask.getDownloadUri());
                    //runInstall(context,downloadTask.getDownIdUri());
                }

                @Override
                public void onDownloadFail() {

                }

                @Override
                public void onDownloadPaused() {

                }
            };

            DownloadHelper.DownloadBuilder downloadBuilder = new DownloadHelper.DownloadBuilder(context)
                    .setFileName(name)
                    .setUrl(url)
                    .setOnProgressListener(onDownloadProgressListener);
            int downloadId = downloadBuilder.start();
        }
    }

    /**
     * 打开安装包
     *
     * @param mContext
     * @param fileUri
     */
    public static void openAPKFile(Context mContext, Uri fileUri) {
        QDLogger.i("开始安装:" + fileUri.getPath());
        if (checkDownloadPermission(mContext)) {
            // DataEmbeddingUtil.dataEmbeddingAPPUpdate(fileUri);
            if (null != fileUri) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//兼容7.0
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(mContext,mContext.getPackageName()+".fileprovider",new File(fileUri.getPath()));
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                    }
                    if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                        mContext.startActivity(intent);
                    }
                } catch (Throwable e) {
                    QDLogger.e(e);
                }
            }
        }
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

    public static void downloadAndSilenceInstall(final Context context, boolean autoReStartApp, final String name, final String url) {
        downloadAndSilenceInstall(context, autoReStartApp, name, url, null);
    }

    /**
     * okhttp 下载
     *
     * @param context
     * @param name
     * @param url
     * @param listener
     * @return
     */
    public static DownloadHelper.DownloadBuilder downloadAndSilenceInstall(final Context context, boolean autoReStartApp, final String name, final String url, OnDownloadProgressListener listener) {
        return downloadAndSilenceInstall(context, autoReStartApp, name, url, DownloadTask.DownloadType.Okhttp, listener);
    }

    //下载前需要
    public static DownloadHelper.DownloadBuilder downloadAndSilenceInstall(final Context context, boolean autoReStartApp, final String name, final String url, DownloadTask.DownloadType downloadType, OnDownloadProgressListener listener) {
        DownloadHelper.DownloadBuilder downloadBuilder = new DownloadHelper.DownloadBuilder(context);
        OnDownloadProgressListener onDownloadProgressListener = new OnDownloadProgressListener() {
            @Override
            public void onDownloadRunning(long downloadId, String name, float progress) {
                if (listener != null && listener.canCallBack()) {
                    listener.onDownloadRunning(downloadId, name, progress);
                }
            }

            @Override
            public void onDownloadSuccess(DownloadTask downloadTask) {
                if (listener != null) {
                    listener.onDownloadSuccess(downloadTask);
                }
                //QDLogger.i(downloadTask.getFileName() + "下载完成，开始安装" + downloadTask.getDownloadUri().getPath());
                try {
                    //File file = new File(new URI(downloadTask.getDownIdUri().toString()));
                    File file = uriToFile(downloadTask.getDownloadUri(), context);
                    if (file == null || !file.exists()) {
                        QDLogger.e("下载文件uri 路径不存在:" + downloadTask.getDownloadUri());
                        return;
                    }
                    QDLogger.i("文件已保存:" + file.getAbsolutePath());
                    silenceInstall(context, autoReStartApp, file);
                } catch (Exception e) {
                    QDLogger.e(e);
                }
                //runInstall(context,downloadTask.getDownIdUri());
            }

            @Override
            public void onDownloadFail() {
                if (listener != null) {
                    listener.onDownloadFail();
                }
            }

            @Override
            public void onDownloadPaused() {
                if (listener != null) {
                    listener.onDownloadPaused();
                }
            }
        };

        downloadBuilder.setDownloadType(downloadType)
                .setFileName(name)
                .setUrl(url)
                .setOnProgressListener(onDownloadProgressListener);
        int downloadId = downloadBuilder.start();
        return downloadBuilder;
    }

    /**
     * 静默安装
     *
     * @param context
     * @param autoReStartApp 是否重启app
     * @param file
     * @throws Exception
     */
    public static void silenceInstall(@NonNull Context context, boolean autoReStartApp, @NonNull File file) throws Exception {
        silenceInstall(context, autoReStartApp, file, null);
    }

    /**
     * 静默安装
     *
     * @param context
     * @param autoReStartApp
     * @param file
     * @param sessionCallback
     * @throws Exception
     */
    public static void silenceInstall(@NonNull Context context, boolean autoReStartApp, @NonNull File file, PackageInstaller.SessionCallback sessionCallback) throws Exception {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (!file.exists()) {
                throw new Exception("安装文件包不存在");
            }
            String path = file.getPath();
            String apkName = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(".apk"));
            //获取PackageInstaller
            PackageInstaller packageInstaller = context.getPackageManager()
                    .getPackageInstaller();
            PackageInstaller.SessionParams params = new PackageInstaller
                    .SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            //params = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_INHERIT_EXISTING);
            PackageInstaller.Session session = null;
            OutputStream outputStream = null;
            FileInputStream inputStream = null;
            if (sessionCallback == null) {
                packageInstaller.registerSessionCallback(new PackageInstaller.SessionCallback() {
                    @Override
                    public void onCreated(int sessionId) {
                        QDLogger.i("install created sessionId=" + sessionId);
                    }

                    @Override
                    public void onBadgingChanged(int sessionId) {
                        QDLogger.i("onBadgingChanged sessionId=" + sessionId);
                    }

                    @Override
                    public void onActiveChanged(int sessionId, boolean active) {
                        QDLogger.i("onActiveChanged sessionId=" + sessionId + ",active =" + active);
                    }

                    @Override
                    public void onProgressChanged(int sessionId, float progress) {
                        QDLogger.i("onProgressChanged sessionId=" + sessionId + ",progress =" + progress);
                    }

                    @Override
                    public void onFinished(int sessionId, boolean success) {
                        if (success) {
                            QDLogger.i("Silent Install Success,sessionId=" + sessionId);
                        } else {
                            QDLogger.i("Silent Install Fail,sessionId=" + sessionId);
                        }
                    }
                });
            } else {
                packageInstaller.registerSessionCallback(sessionCallback);
            }
            try {
                //创建Session
                int sessionId = packageInstaller.createSession(params);
                //开启Session
                session = packageInstaller.openSession(sessionId);
                //获取输出流，用于将apk写入session
                outputStream = session.openWrite(apkName, 0, -1);
                inputStream = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int n;
                //读取apk文件写入session
                while ((n = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, n);
                }
                //写完需要关闭流，否则会抛异常“files still open”
                inputStream.close();
                inputStream = null;
                outputStream.flush();
                outputStream.close();
                outputStream = null;

                //配置安装完成后发起的intent，通常是打开activity
                Intent intent = new Intent();
                PendingIntent restartIntent = null;
                if (autoReStartApp) {
                    intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                    restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                /*AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0及以上
                    mgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, restartIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
                    mgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, restartIntent);
                }*/
                    //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                } else {
                    restartIntent = PendingIntent.getActivity(context, 0, intent, 0);
                }
                IntentSender intentSender = restartIntent.getIntentSender();
                //提交启动安装
                session.commit(intentSender);
            } catch (IOException e) {
                throw new RuntimeException("Couldn't install package", e);
            } catch (RuntimeException e) {
                if (session != null) {
                    session.abandon();
                }
                throw e;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }

    public static void installSlient(Context context, File apk) {
        String cmd = "pm install -r " + apk.getPath();
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            //静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();

            //执行命令
            process.waitFor();

            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();

            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }

            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            QDLogger.e(e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                QDLogger.e(e);
            }
        }
        QDLogger.e("installMsg: " + successMsg.toString() + ", errorMsg: " + errorMsg.toString());
        //安装成功
        if ("Success".equals(successMsg.toString())) {
            QDLogger.d("apk install success");
        }
        launchAPK(context, apk);
    }

    public static int installSilent(File file) {
        //File file = new File(filePath);
        if (file == null || !file.exists()) {
            return 1;
        }
        String filePath = file.getAbsolutePath();
        String[] args = {"pm", "install", "-r", filePath};
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            QDLogger.e(e);
        } catch (Exception e) {
            QDLogger.e(e);
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                QDLogger.e(e);
            }
            if (process != null) {
                process.destroy();
            }
        }
        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            result = 0;
        } else {
            result = 2;
        }
        Log.d("test-test", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return result;
    }

    public static void runInstall(final Context context, final File file) {
        Uri uri = Uri.fromFile(file);
        runInstall(context, uri);
    }

    /* 卸载apk */
    public static void unInstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
        //int REQUEST_CODE = 4567;
        // context.startActivityForResult(intent, REQUEST_CODE);
    }

    public static void runInstall(final Context context, final Uri uri) {
        new Thread(() -> {
            boolean success = installByRoot();
            if (!success) {
                if (isAccessBilityOn(context)) {
                    //Intent intent = new Intent(Intent.ACTION_VIEW);
                    //intent.setDataAndType(uri,"application/vnd.android.package-archive");
                    //context.startActivity(intent);
                    openAPKFile(context, uri);
                } else {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    context.startActivity(intent);
                }
            }
        }).start();
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
            QDLogger.e(e);
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
                QDLogger.e(e);
                os = null;
                br = null;
                process.destroy();
            }
        }
        return result;
    }


    /**
     * 启动第三方apk
     * 直接打开  每次都会启动到启动界面，每次都会干掉之前的，从新启动
     * XXXXX ： 包名
     */
    public static void launchAPK(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent it = packageManager.getLaunchIntentForPackage(packageName);
        context.startActivity(it);
    }

    public static void launchAPK(Context context, File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取apk包的信息：版本号，名称，图标等
     *
     * @param absPath apk包的绝对路径
     * @param context
     */
    public void getApkInfo(String absPath, Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
            String packageName = appInfo.packageName; // 得到包名
            String version = pkgInfo.versionName; // 得到版本信息
            /* icon1和icon2其实是一样的 */
            Drawable icon1 = pm.getApplicationIcon(appInfo);// 得到图标信息
            Drawable icon2 = appInfo.loadIcon(pm);
            String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);
            QDLogger.i(String.format("PkgInfo: %s", pkgInfoStr));
        }
    }

}
