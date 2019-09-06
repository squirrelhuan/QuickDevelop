package cn.demomaster.huan.quickdeveloplibrary.helper.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

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
            Handler handler = new Handler();
            instance = new DownloadChangeObserver(context, new DownloadHandler());
            instance.downloadHelper = downloadHelper;
        }
        return instance;
    }

    public DownloadChangeObserver(Handler handler, DownloadManager downloadManager) {
        super(handler);
        this.downloadManager = downloadManager;
    }

    private DownloadManager downloadManager;
    private ScheduledExecutorService executorService;
    private Runnable downloadProgressRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param
     * @return
     */
    private void updateProgress() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_RUNNING);
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            //遍历游标
            while (cursor != null && cursor.moveToNext()) {
                //下载文件的总大小
                int file_total_size = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                if (file_total_size > 0) {
                    //已经下载文件大小
                    int download_so_far_size = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    //下载状态
                    int task_status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    // String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    // String fileUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                    long column_id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    // QDLogger.i("下载编号：" + column_id+",总大小："+file_total_size+",已下载："+download_so_far_size+"，状态："+task_status);
                    DownloadProgress downloadProgress = new DownloadProgress(column_id, task_status, download_so_far_size, file_total_size);
                    Message message = new Message();
                    message.what = HANDLE_DOWNLOAD;
                    message.obj = downloadProgress;
                    downLoadHandler.sendMessage(message);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 构造方法
     *
     * @param context
     * @param handler
     */
    public DownloadChangeObserver(Context context, DownloadHandler handler) {
        super(handler);
        this.downLoadHandler = handler;
        this.downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        try {
            executorService = Executors.newSingleThreadScheduledExecutor();
        } catch (Exception e) {
            QDLogger.e(e.getMessage());
        }
    }

    /**
     * 当所监听的Uri发生改变时，就会回调此方法
     *
     * @param selfChange 此值意义不大, 一般情况下该回调值false
     */
    @Override
    public void onChange(boolean selfChange) {
        try {
            executorService.scheduleAtFixedRate(downloadProgressRunnable, 0, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            QDLogger.e(e.getMessage());
        }
    }

    public void close() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        if (downLoadHandler != null) {
            downLoadHandler.removeCallbacks(downloadProgressRunnable);
            downLoadHandler.removeCallbacksAndMessages(null);
        }
    }

    public static final int HANDLE_DOWNLOAD = 0x001;
    public Handler downLoadHandler;

    public void putTask(DownloadTask downloadTask) {
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
                            QDLogger.e("downloadId = -1");
                        } else {
                            //  OnDownloadProgressListener listener = listenerMap.get(downloadTask.getDownloadId());
                            switch (downloadProgress.getStatus()) {
                                case DownloadManager.STATUS_PENDING:
                                    break;
                                case DownloadManager.STATUS_PAUSED:
                                    QDLogger.d("暂停下载");
                                    break;
                                case DownloadManager.STATUS_RUNNING:
                                    //QDLogger.d("下载中");
                                    if (downloadTask.getOnProgressListener() != null) {
                                        downloadTask.getOnProgressListener().onProgress(downloadProgress.getDownloadId(), downloadTask.getFileName(), downloadProgress.getHasLoadSize() / (float) downloadProgress.getTotalSize());
                                    }
                                    break;
                                case DownloadManager.STATUS_SUCCESSFUL:
                                    QDLogger.d("下载成功");
                                    if (downloadTask != null) {
                                        //  downloadTask.onDownloadSuccess();
                                    }
                                    break;
                                case DownloadManager.STATUS_FAILED:
                                    QDLogger.d("failed");
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}