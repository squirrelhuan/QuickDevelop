package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
            instance = new DownloadHelper(context.getApplicationContext());
        }
        return instance;
    }


    public Map<Long, DownloadTask> taskMap = new HashMap<>();
    private static BroadcastReceiver broadcastReceiver;//广播接收者
    private static DownloadChangeObserver downloadChangeObserver;//下载变更观察者
    private DownloadManager downloadManager;

    private DownloadHelper(Context context) {
        // this.broadcastReceiver = new DownLoadBroadcast(onDownloadStateChangeListener);
        init(context);
    }

    private void init(Context context) {
        this.downloadChangeObserver = DownloadChangeObserver.getInstance(context.getApplicationContext(), this);
        this.downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
    }

    private void pushTask(final DownloadTask downloadTask) {
        PermissionManager.chekPermission(downloadTask.getContext(), PERMISSIONS_STORAGE, new PermissionManager.OnCheckPermissionListener() {
            @Override
            public void onPassed() {
                if(downloadTask.getDownloadType()== DownloadTask.DownloadType.DownloadManager) {
                    excute(downloadTask);
                }else {
                    downloadFile(downloadTask);
                }
            }

            @Override
            public void onNoPassed() {
                QdToast.show(downloadTask.getContext(), "请打开相关权限！", Toast.LENGTH_SHORT);
            }
        });
    }

    public void downloadFile(DownloadTask downloadTask){
        final long startTime = System.currentTimeMillis();
        QDLogger.i("DOWNLOAD","startTime="+startTime+"，URL="+downloadTask.getDownloadUrl());

        //获取要下载的文件存放路径
        String download_app_folder_name = downloadTask.getDownload_app_folder_name();
        QDLogger.i("下载文件存放路径：" + download_app_folder_name + "，文件名：" + downloadTask.getFileName());

        String sdpath = Environment.getExternalStorageDirectory() + "";
        if (TextUtils.isEmpty(sdpath)) {
            sdpath = downloadTask.getContext().getFilesDir().getAbsolutePath();
            QDLogger.e("外置内存卡不存在,下载存储路径已改为：" + sdpath);
        }
        String download_app_folder =(download_app_folder_name.startsWith(File.separator) ? download_app_folder_name : (File.separator + download_app_folder_name));
        File dir = new File(sdpath + download_app_folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(downloadTask.getDownloadUrl()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                QDLogger.i("DOWNLOAD","download failed");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        downloadTask.getOnProgressListener().onDownloadFail();
                    }
                });//在子线程中直接去new 一个handler
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath =Environment.getExternalStorageDirectory().getAbsolutePath()+  download_app_folder;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, downloadTask.getFileName());
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        float progress =  (sum * 1.0f / total );
                        // 下载中
                        downloadTask.getOnProgressListener().onDownloadRunning(downloadTask.getDownloadId(),downloadTask.getFileName(),progress);
                    }
                    fos.flush();
                    downloadTask.setDownUriStr(file.getAbsolutePath());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // 下载完成
                            downloadTask.getOnProgressListener().onDownloadSuccess(downloadTask);
                        }
                    });//在子线程中直接去new 一个handler
                    Log.i("DOWNLOAD","download success");
                    Log.i("DOWNLOAD","totalTime="+ (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("DOWNLOAD","download failed 1");
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            downloadTask.getOnProgressListener().onDownloadFail();
                        }
                    });//在子线程中直接去new 一个handler
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    //执行任务
    private void excute(DownloadTask downloadTask) {
        //获取要下载的文件存放路径
        String download_app_folder_name = downloadTask.getDownload_app_folder_name();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadTask.getDownloadUrl()));
        QDLogger.i("下载文件存放路径：" + download_app_folder_name + "，文件名：" + downloadTask.getFileName());

        String sdpath = Environment.getExternalStorageDirectory() + "";
        if (TextUtils.isEmpty(sdpath)) {
            sdpath = downloadTask.getContext().getFilesDir().getAbsolutePath();
            QDLogger.e("外置内存卡不存在,下载存储路径已改为：" + sdpath);
        }
        String download_app_folder = download_app_folder_name.startsWith(File.separator) ? download_app_folder_name : (File.separator + download_app_folder_name);
        File dir = new File(sdpath + download_app_folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 设置存储的目录 文件夹 文件名
        request.setDestinationInExternalPublicDir(download_app_folder, downloadTask.getFileName());
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        // 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        request.setShowRunningNotification(true);
        request.setVisibleInDownloadsUi(true);
        request.setTitle(downloadTask.getFileName());
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

        //在执行下载前注册内容监听者
        boolean registerResult = registerContentObserver(downloadTask.getContext());
        //下载任务ID
        long downloadId = downloadManager.enqueue(request);
        if (registerResult) {
            //contextMap.put(downloadId, downloadTask.getContext());
            downloadTask.setDownloadId(downloadId);
            taskMap.put(downloadId, downloadTask);
            downloadChangeObserver.putTask(downloadTask);
            registerBroadcast(downloadTask.getContext());
        } else {
            QDLogger.e("注册下载监听器失败了");
        }
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
            //context.registerReceiver(broadcastReceiver, intentFilter);
        } catch (Exception e) {
            QDLogger.e(e.getMessage());
        }
    }

    /**
     * 注销ContentObserver
     */
    private static void unregisterContentObserver(Context context) {
        if (downloadChangeObserver != null) {
            try {
                context.getContentResolver().unregisterContentObserver(downloadChangeObserver);
            } catch (Exception e) {
                QDLogger.e(e.getMessage());
            }
        }
    }

    //public static Map<Long, Context> contextMap = new HashMap<>();

    /**
     * 注册ContentObserver
     */
    private boolean registerContentObserver(Context context) {
        if (downloadChangeObserver != null) {
            try {
                //if(!contextMap.containsValue(context)) {//如果没注册过
                context.getApplicationContext().getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), false, downloadChangeObserver);
                return true;
                //}
            } catch (Exception e) {
                QDLogger.e(e.getMessage());
            }
        }
        return false;
    }

    /**
     * 解绑activity
     *
     * @param context
     */
    public static void unregisterReceiver(Context context) {
        //TODO
        /*unregisterContentObserver(context);
        if (contextMap.containsValue(context)) {
            contextMap.remove(context);
        }
        if (contextMap.size() <= 0) {
            downloadChangeObserver.close();
        }*/
    }

    /**
     * 解绑activity
     *
     * @param
     */
    public static void unregisterReceiver(Long downloadId) {
       /* if (contextMap.containsKey(downloadId) && contextMap.get(downloadId) != null) {
            unregisterReceiver(contextMap.get(downloadId));
        }*/
    }

    //private OnDownloadStateChangeListener onDownloadStateChangeListener;

    /**
     * 下载状态监听器
     */
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
        private DownloadTask.DownloadType downloadType= DownloadTask.DownloadType.DownloadManager;
        private OnDownloadProgressListener onProgressListener;//进度监听

        public DownloadBuilder(Context context) {
            this.context = context.getApplicationContext();
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

        public DownloadBuilder setDownloadType(DownloadTask.DownloadType downloadType) {
            this.downloadType = downloadType;
            return this;
        }

        public DownloadHelper creat() {
            return getInstance(context);
        }

        public void start() {
            QDLogger.i("准备下载->" + fileName);
          /*  if(!checkDownloadManagerEnable(context)){
                QDLogger.i("下载管理器开启失败" + fileName);
                return;
            }*/
            DownloadTask downloadTask = new DownloadTask(context);
            downloadTask.setDownloadUrl(url);
            downloadTask.setFileName(fileName);
            downloadTask.setOnProgressListener(onProgressListener);
            DownloadHelper.getInstance(context).pushTask(downloadTask);
        }

        public void unregister(Context context) {
            QDLogger.i("注销");
            if (context != null) {
                DownloadHelper.getInstance(context).unregisterReceiver(context);
            }
        }
    }


}
