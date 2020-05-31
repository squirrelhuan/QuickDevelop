package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.app.DownloadManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadTask;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.OnDownloadProgressListener;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_PAUSED;
import static android.app.DownloadManager.STATUS_PENDING;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;
import static android.content.Context.DOWNLOAD_SERVICE;


/**
 * Squirrel桓
 * 2018/8/25 QDTerminal
 */
@ActivityPager(name = "Downloader", preViewClass = TextView.class, resType = ResType.Custome)
public class DownloadFragment extends QDFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_download_01)
    QDButton btn_download_01;
    @BindView(R.id.btn_download_thread)
    QDButton btn_download_thread;

    @BindView(R.id.btn_upload_file)
    QDButton btn_upload_file;

    View mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_download, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("文件下载");
        btn_download_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadHelper.DownloadBuilder(mContext).setUrl("https://baekteori.s3.ap-northeast-2.amazonaws.com/charge/app/app-release-legu-20191220001.apk?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20191224T041244Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=AKIA2AOAUOJY3RLCJN6J%2F20191224%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=6a0a8f6e078b5c5f07f048aac0e43f600ca45d8d0ab526a770c66590de9936fc")
                        .setFileName("抖音.png")
                        .setDownloadType(DownloadTask.DownloadType.Okhttp)
                        .setOnProgressListener(
                                new OnDownloadProgressListener() {
                                    @Override
                                    public void onDownloadRunning(long downloadId, String name, float progress) {
                                        QDLogger.d("下载状态：" + downloadId + "," + name + "," + progress);
                                    }

                                    @Override
                                    public void onDownloadSuccess(DownloadTask downloadTask) {
                                        QDLogger.i("1下载完成" + downloadTask.getFileName() + "->" + downloadTask.getDownloadUri().getPath());
                                    }

                                    @Override
                                    public void onDownloadFail() {

                                    }

                                    @Override
                                    public void onDownloadPaused() {

                                    }
                                }).start();
               /* new DownloadHelper.DownloadBuilder(mContext).setUrl("http://wap.apk.anzhi.com/data5/apk/201907/19/941a15c7bdcc39a9ef33e22cb8dd7680_70536400.apk")
                        .setFileName("抖音.apk")
                        .setOnProgressListener(
                                new OnDownloadProgressListener() {
                                    @Override
                                    public void onDownloadRunning(long downloadId, String name, float progress) {
                                        QDLogger.d("下载状态：" + downloadId + "," + name + "," + progress);
                                    }

                                    @Override
                                    public void onDownloadSuccess(DownloadTask downloadTask) {
                                        QDLogger.i("1下载完成" + downloadTask.getFileName() + "->" + downloadTask.getDownloadUri().getPath());
                                    }

                                    @Override
                                    public void onDownloadFail() {

                                    }

                                    @Override
                                    public void onDownloadPaused() {

                                    }
                                }).start();

                new DownloadHelper.DownloadBuilder(mContext).setUrl("https://alissl.ucdl.pp.uc.cn/fs08/2019/07/05/6/2_b0cfb9e044477a4a1ea8ef8656fb69c9.apk?yingid=wdj_web&fname=%E5%BD%B1%E9%9F%B3%E5%85%88%E9%94%8B&pos=wdj_web%2Fdetail_normal_dl%2F0&appid=296927&packageid=800798582&apprd=296927&iconUrl=http%3A%2F%2Fandroid-artworks.25pp.com%2Ffs08%2F2019%2F07%2F08%2F4%2F2_41f75e4a7e71a2c47497e01a3c24ca43_con.png&pkg=com.xfplay.play&did=18c032fc0601f7543599f3bb2b4e65da&vcode=500730&md5=caa800e0c2c0575641d001377f12e3a1")
                        .setFileName("影音.apk")
                        .setOnProgressListener(
                                new OnDownloadProgressListener() {
                                    @Override
                                    public void onDownloadRunning(long downloadId, String name, float progress) {
                                        QDLogger.d("下载状态：" + downloadId + "," + name + "," + progress);
                                    }

                                    @Override
                                    public void onDownloadSuccess(DownloadTask downloadTask) {
                                        QDLogger.i("2下载完成" + downloadTask.getFileName() + "->" + downloadTask.getDownloadUri().getPath());
                                    }

                                    @Override
                                    public void onDownloadFail() {

                                    }

                                    @Override
                                    public void onDownloadPaused() {

                                    }
                                }).start();*/

            }
        });

        btn_download_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDownloading();
            }
        });

        btn_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory(), "ShareBattery/log/2019-11-26.txt");
                QDLogger.i("上传路径：" + file.getAbsolutePath());
                //uploadFile(file);
                //http://web.baekteori.com:18088/mange-web/log/upload
                sendFromDataPostRequest("http://web.baekteori.com:18088/mange-web/log/upload", file, "file");
            }
        });
    }

    private static final MediaType FROM_DATA = MediaType.parse("multipart/form-data");

    public void sendFromDataPostRequest(String url, File file, String typeName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                MultipartBody body = new MultipartBody.Builder()
                        .setType(FROM_DATA)
                        .addFormDataPart(typeName, "测试上传日志文件.txt", fileBody)
                        .build();
                Request request = new Request.Builder()
                        .post(body)
                        .url(url)
                        .build();
                OkHttpClient client = new OkHttpClient();
                try {
                    System.out.println(client.newCall(request).execute().body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private DownloadManager downloadManager;

    private void getDownloading() {
        this.downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();//.setFilterByStatus(DownloadManager.STATUS_RUNNING);//.setFilterById(downloadId);//
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            //遍历游标
            while (cursor != null && cursor.moveToNext()) {
                //下载文件的总大小
                int file_total_size = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //if (file_total_size > 0) {
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

                QDLogger.e(fileName + ",state=" + task_status);
                QDLogger.e(fileName + ",STATUS_PENDING=" + STATUS_PENDING);
                QDLogger.e(fileName + ",STATUS_FAILED=" + STATUS_FAILED);
                QDLogger.e(fileName + ",STATUS_PAUSED=" + STATUS_PAUSED);
                QDLogger.e(fileName + ",STATUS_RUNNING=" + STATUS_RUNNING);
                QDLogger.e(fileName + ",STATUS_SUCCESSFUL=" + STATUS_SUCCESSFUL);

                if (task_status == STATUS_FAILED) {
                    long column_id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    downloadManager.remove(column_id);
                }

                long column_id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                // QDLogger.i("下载编号：" + column_id+",总大小："+file_total_size+",已下载："+download_so_far_size+"，状态："+task_status);
                   /* DownloadProgress downloadProgress = new DownloadProgress(downloadId, task_status, download_so_far_size, file_total_size);
                    if (!TextUtils.isEmpty(fileName)) {
                        downloadProgress.setFileName(fileName);
                    }
                    if (!TextUtils.isEmpty(fileUri)) {
                        downloadProgress.setDownloadUri(fileUri);
                    }
                    if (task_status == DownloadManager.STATUS_SUCCESSFUL || task_status == DownloadManager.STATUS_FAILED || task_status == DownloadManager.STATUS_PAUSED) {
                        downloadTaskMap.remove(downloadId);
                    }
                    Message message = new Message();
                    message.what = HANDLE_DOWNLOAD;
                    message.obj = downloadProgress;
                    downLoadHandler.sendMessage(message);*/
                //}
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}