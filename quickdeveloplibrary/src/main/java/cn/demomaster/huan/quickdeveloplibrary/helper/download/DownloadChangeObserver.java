package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.umeng.commonsdk.debug.E;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

import static android.content.Context.DOWNLOAD_SERVICE;


/**
 * 监听下载进度
 */
public class DownloadChangeObserver extends ContentObserver {
    public static DownloadHelper downloadHelper;

    public void setDownloadHelper(DownloadHelper downloadHelper) {
        this.downloadHelper = downloadHelper;
    }

    private static DownloadChangeObserver instance;

    public static DownloadChangeObserver getInstance(Context context, DownloadHelper downloadHelper) {
        if (instance == null) {
            instance = new DownloadChangeObserver(context, new DownloadHandler());
            instance.downloadHelper = downloadHelper;
        }
        return instance;
    }

    private DownloadManager downloadManager;
    /* private ScheduledExecutorService executorService;*/
    private Runnable downloadProgressRunnable = new Runnable() {
        @Override
        public void run() {
            QDLogger.i("下载进度");
            try {
                if (downloadTaskMap.size() > 0) {
                    QDLogger.i("下载进度"+downloadTaskMap.size());
                    for (Map.Entry entry : downloadTaskMap.entrySet()) {
                        long id =(Long) entry.getKey();
                       int r= updateProgress(id);
                       if(r==-1){
                           QDLogger.i(id+"下载终止了");
                       }else if(r==1){
                           QDLogger.i(id+"下载id在列表");
                           continue;
                       }else if(r==0){
                           QDLogger.i(id+"下载id不存在");
                           break;
                       }
                    }
                    downLoadHandler.postDelayed(downloadProgressRunnable, 1000);
                }else {
                    QDLogger.i("下载进度已经为空");
                    downLoadHandler.removeCallbacks(downloadProgressRunnable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     * @param downloadId
     * @return -1下载结束了，1下载进度中，0下载不存在
     */
    private int updateProgress(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);//.setFilterByStatus(DownloadManager.STATUS_RUNNING)
        Cursor cursor = null;
        boolean isExists = false;//该记录是否在下载列表
        try {
            cursor = downloadManager.query(query);
            //遍历游标
            while (cursor != null && cursor.moveToNext()) {
                isExists = true;
                //下载文件的总大小
                int file_total_size = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                if (file_total_size > 0) {
                    //已经下载文件大小
                    int download_so_far_size = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    //下载状态
                    int task_status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    String fileName = null;
                    String fileUri = null;
                    if (task_status == DownloadManager.STATUS_SUCCESSFUL) {
                        //fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                        //fileUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                        int fileUriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                        fileUri = cursor.getString(fileUriIdx);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if (fileUri != null) {
                                fileName = Uri.parse(fileUri).getPath();
                            }
                        } else {
                            //Android 7.0以上的方式：请求获取写入权限，这一步报错
                            //过时的方式：DownloadManager.COLUMN_LOCAL_FILENAME
                            int fileNameIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                            fileName = cursor.getString(fileNameIdx);
                        }
                    }
                    long column_id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                     QDLogger.i("下载编号：" + column_id+",总大小："+file_total_size+",已下载："+download_so_far_size+"，状态："+task_status);
                    DownloadProgress downloadProgress = new DownloadProgress(downloadId, task_status, download_so_far_size, file_total_size);
                    if (!TextUtils.isEmpty(fileName)) {
                        downloadProgress.setFileName(fileName);
                    }
                    if (!TextUtils.isEmpty(fileUri)) {
                        downloadProgress.setDownloadUri(fileUri);
                    }

                    Message message = new Message();
                    message.what = HANDLE_DOWNLOAD;
                    message.obj = downloadProgress;
                    downLoadHandler.sendMessage(message);
                    if (task_status == DownloadManager.STATUS_SUCCESSFUL || task_status == DownloadManager.STATUS_FAILED || task_status == DownloadManager.STATUS_PAUSED) {
                        downloadTaskMap.remove(downloadId);
                        return -1;
                    }
                }else {
                    QDLogger.i(downloadId+"下载文件的总大小"+file_total_size);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if(!isExists){
            downloadTaskMap.remove(downloadId);
            downloadHelper.unregisterReceiver(downloadId);

        }
        return !isExists?0:1;
    }

    /**
     * 构造方法
     *
     * @param context
     * @param handler
     */
    private DownloadChangeObserver(Context context, DownloadHandler handler) {
        super(handler);
        this.downLoadHandler = handler;
        this.downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
       /* try {
            executorService = Executors.newSingleThreadScheduledExecutor();
        } catch (Exception e) {
            QDLogger.e("QDdownload", "DownloadChangeObserver" + e.getMessage());
        }*/
    }

    /**
     * 当所监听的Uri发生改变时，就会回调此方法
     *
     * @param selfChange 此值意义不大, 一般情况下该回调值false
     */
    @Override
    public void onChange(boolean selfChange) {
       /* try {
            executorService.scheduleAtFixedRate(downloadProgressRunnable, 0, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            QDLogger.e("QDdownload", "onChange" + e.getMessage());
        }*/

        QDLogger.e("QDdownload", "onChange");
        downLoadHandler.removeCallbacks(downloadProgressRunnable);
        downLoadHandler.postDelayed(downloadProgressRunnable, 1000);
    }

    /**
     * 关闭
     */
    public void close() {
        /*if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }*/
        if (downLoadHandler != null) {
            // downLoadHandler.removeCallbacks(downloadProgressRunnable);
            downLoadHandler.removeCallbacksAndMessages(null);
        }
    }

    public static final int HANDLE_DOWNLOAD = 0x001;
    public Handler downLoadHandler;
    private Map<Long, DownloadTask> downloadTaskMap = new HashMap<>();

    public void putTask(DownloadTask downloadTask) {
        if (downloadTask != null) {
            downloadTaskMap.put(downloadTask.getDownloadId(), downloadTask);
        }
    }

    public static class DownloadHandler extends Handler {

        public DownloadHandler() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (HANDLE_DOWNLOAD == msg.what) {
                DownloadProgress downloadProgress = (DownloadProgress) msg.obj;
                if (downloadHelper.taskMap.containsKey(downloadProgress.getDownloadId())) {
                    DownloadTask downloadTask = downloadHelper.taskMap.get(downloadProgress.getDownloadId());
                    //被除数可以为0，除数必须大于0
                    if (downloadProgress != null) {
                        if (downloadProgress.getDownloadId() == -1) {
                            QDLogger.e("QDdownload", "downloadId = -1");
                        } else {
                            //  OnDownloadProgressListener listener = listenerMap.get(downloadTask.getDownloadId());
                            switch (downloadProgress.getStatus()) {
                                case DownloadManager.STATUS_PENDING:
                                    break;
                                case DownloadManager.STATUS_PAUSED:
                                    QDLogger.d("QDdownload", "暂停下载");
                                    if (downloadTask != null) {
                                        downloadTask.getOnProgressListener().onDownloadPaused();
                                        downloadHelper.unregisterReceiver(downloadTask.getDownloadId());
                                    }
                                    break;
                                case DownloadManager.STATUS_RUNNING:
                                    //QDLogger.d("下载中");
                                    if (downloadTask.getOnProgressListener() != null) {
                                        downloadTask.getOnProgressListener().onDownloadRunning(downloadProgress.getDownloadId(), downloadTask.getFileName(), downloadProgress.getHasLoadSize() / (float) downloadProgress.getTotalSize());
                                    }
                                    break;
                                case DownloadManager.STATUS_SUCCESSFUL:
                                    QDLogger.d("QDdownload", "下载成功");
                                    if (downloadTask.getOnProgressListener() != null) {
                                        downloadTask.getOnProgressListener().onDownloadRunning(downloadProgress.getDownloadId(), downloadTask.getFileName(), 1);
                                    }
                                    if (downloadTask != null) {
                                        downloadTask.setFileName(downloadProgress.getFileName());
                                        downloadTask.setDownUriStr(downloadProgress.getFileName());
                                        downloadTask.getOnProgressListener().onDownloadSuccess(downloadTask);
                                        downloadHelper.unregisterReceiver(downloadTask.getDownloadId());
                                    }
                                    break;
                                case DownloadManager.STATUS_FAILED:
                                    QDLogger.d("QDdownload", "下载失败");
                                    if (downloadTask != null) {
                                        downloadTask.getOnProgressListener().onDownloadFail();
                                        downloadHelper.unregisterReceiver(downloadTask.getDownloadId());
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}