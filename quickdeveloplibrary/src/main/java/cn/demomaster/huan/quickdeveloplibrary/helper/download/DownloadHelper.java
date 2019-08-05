package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * 文件下载帮助类
 */
public class DownloadHelper {
    // 权限
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static DownloadHelper instance;

    public static DownloadHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DownloadHelper();
        }
        instance.init(context);
        return instance;
    }


    public Map<Long, DownloadTask> taskMap = new HashMap<>();
    private static BroadcastReceiver broadcastReceiver;//广播接收者
    private static DownloadChangeObserver downloadChangeObserver;//下载变更观察者
    private DownloadManager downloadManager;

    private DownloadHelper() {
        this.onDownloadStateChangeListener = new OnDownloadStateChangeListener() {
            @Override
            public void onComplete(long downloadId, Uri downIdUri) {
                //QDLogger.i("下载完成，存储路径为 ：" + downIdUri.getPath());
                if (taskMap.containsKey(downloadId)) {
                    DownloadTask downloadTask = taskMap.get(downloadId);
                    if (downloadTask != null) {
                        downloadTask.setDownIdUri(downIdUri);
                        downloadTask.getOnProgressListener().onComplete(downloadTask);
                        taskMap.remove(downloadTask);
                    }
                }
            }
        };
        this.broadcastReceiver = new DownLoadBroadcast(onDownloadStateChangeListener);
    }

    private void init(Context context) {
        this.downloadChangeObserver = DownloadChangeObserver.getInstance(context, this);
        this.downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        //在执行下载前注册内容监听者
        registerContentObserver(context);
    }
    private void pushTask(final DownloadTask downloadTask) {
        PermissionManager.chekPermission(downloadTask.getContext(), PERMISSIONS_STORAGE, new PermissionManager.OnCheckPermissionListener() {
            @Override
            public void onPassed() {
                excute(downloadTask);
            }

            @Override
            public void onNoPassed() {
                Toast.makeText(downloadTask.getContext(), "请打开相关权限！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void excute(DownloadTask downloadTask) {
        String download_app_folder_name = downloadTask.getDownload_app_folder_name();
        //app下载名称
        String filename = (download_app_folder_name.endsWith("\\") ? download_app_folder_name : (download_app_folder_name + File.separator)) + downloadTask.getFileName();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadTask.getDownloadUrl()));

        // 存储的目录
        request.setDestinationInExternalPublicDir(download_app_folder_name, filename);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        // 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        request.setShowRunningNotification(true);
        request.setVisibleInDownloadsUi(true);
        request.setTitle(filename);
        // 不显示下载界面
        request.setVisibleInDownloadsUi(true);
        /*
         * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
         * 在/mnt/sdcard
         * /Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错
         * ，不设置，下载后的文件在/cache这个 目录下面
         */
        //request.setDestinationInExternalFilesDir(this, null, "tar.apk");
        // 下载完成后保留 下载的notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //下载任务ID
        long downloadId = downloadManager.enqueue(request);
        downloadTask.setDownloadId(downloadId);
        taskMap.put(downloadId, downloadTask);
        downloadChangeObserver.putTask(downloadTask);
        registerBroadcast(downloadTask.getContext());
    }

    /**
     * 注册广播
     *
     * @param
     * @param context
     */
    private void registerBroadcast(Context context) {
        try {

            /**注册service 广播 1.任务完成时 2.进行中的任务被点击*/
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
            context.registerReceiver(broadcastReceiver, intentFilter);
        }catch (Exception e){
            QDLogger.e(e.getMessage());
        }
    }

    /**
     * 注销广播
     */
    private static void unregisterBroadcast(Context context) {
        if (broadcastReceiver != null) {
            try {
                context.unregisterReceiver(broadcastReceiver);
            }catch (Exception e){
                QDLogger.e(e.getMessage());
            }
        }
    }

    /**
     * 注销ContentObserver
     */
    private static void unregisterContentObserver(Context context) {
        if (downloadChangeObserver != null) {
            try {
                context.getContentResolver().unregisterContentObserver(downloadChangeObserver);
            }catch (Exception e){
                QDLogger.e(e.getMessage());
            }
        }
    }

    /**
     * 注册ContentObserver
     */
    private void registerContentObserver(Context context) {
        if (downloadChangeObserver != null) {
            try {
                context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), false, downloadChangeObserver);
            }catch (Exception e){
                QDLogger.e(e.getMessage());
            }
        }
    }

    public static void unregisterReceiver(Context context) {
        unregisterBroadcast(context);
        unregisterContentObserver(context);
        downloadChangeObserver.close();
    }

    OnDownloadStateChangeListener onDownloadStateChangeListener;

    public static interface OnDownloadStateChangeListener {
        void onComplete(long downId, Uri downIdUri);
    }


    /**
     * 导航栏构建者
     */
    public static class DownloadBuilder {
        private Context context;
        private String url;//下载路径
        private String fileName;//文件名.格式
        private OnDownloadProgressListener onProgressListener;//进度监听

        public DownloadBuilder(Context context) {
            this.context = context;
        }

        public String getUrl() {
            return url;
        }

        public DownloadBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getFileName() {
            return fileName;
        }

        public DownloadBuilder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public OnDownloadProgressListener getOnProgressListener() {
            return onProgressListener;
        }

        public DownloadBuilder setOnProgressListener(OnDownloadProgressListener onProgressListener) {
            this.onProgressListener = onProgressListener;
            return this;
        }
        public DownloadHelper creat(){
            return getInstance(context);
        }
        public void start() {
            QDLogger.i("准备下载->" + fileName);
            DownloadTask downloadTask = new DownloadTask(context);
            downloadTask.setDownloadUrl(url);
            downloadTask.setFileName(fileName);
            downloadTask.setOnProgressListener(onProgressListener);
            DownloadHelper downloadHelper = DownloadHelper.getInstance(context);
            downloadHelper.pushTask(downloadTask);
        }

        public void unregister(Context context) {
            QDLogger.i("准备下载->" + fileName);
            if (context != null) {
                DownloadHelper.getInstance(context).unregisterReceiver(context);
            }
        }
    }


}
