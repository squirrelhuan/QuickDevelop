package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.getExternalFilesDirs;
import static cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil.getFileCreatTime;

/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "文件管理", preViewClass = TextView.class, resType = ResType.Custome)
public class FileManagerFragment extends QuickFragment {

    //Components
    @BindView(R.id.btn_search)
    QDButton btn_search;
    @BindView(R.id.btn_delete)
    QDButton btn_delete;
    @BindView(R.id.tv_current)
    TextView tv_current;
    @BindView(R.id.tv_count)
    TextView tv_count;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_filemanager, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("文件管理");
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btn_search.setOnClickListener(v -> {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String rootPath = Environment.getExternalStorageDirectory().getPath();
                    List<File> files = QDFileUtil.getEmptyFiles(rootPath);
                    for (File f : files) {
                        QDLogger.d(f);
                    }
                }
            });
            thread.start();
        });
        btn_delete.setOnClickListener(v -> {
            count = 0;
            PermissionHelper.requestPermission(mContext, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionHelper.PermissionListener() {
                @Override
                public void onPassed() {
                    QDActionDialog qdActionDialog = new QDActionDialog.Builder(mContext).setContentView(new LoadingCircleView(mContext)).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setTopImage(R.mipmap.quickdevelop_ic_launcher).setMessage("删除中...").create();
                    qdActionDialog.show();
                    btn_delete.setEnabled(false);
                            String rootPath = Environment.getExternalStorageDirectory().getPath();
                           /* File file = new File("/");
                            if(file!=null){
                                deleteEmptyFiles(file.getAbsolutePath());
                            }else {
                                deleteEmptyFiles(rootPath);
                            }*/

                            //deleteEmptyFiles(rootPath);
                            deleteEmptyFiles(QDFileUtil.getStoragePath(mContext, true));

                            btn_delete.setEnabled(true);
                            btn_delete.setText("删除文件");
                            qdActionDialog.dismiss();
                }

                @Override
                public void onRefused() {
                }
            });
        });

        List<String> sdcards = QDFileUtil.getExtSDCardPathList();
        if (sdcards != null) {
            for (String sdcard : sdcards) {
                QDLogger.println("sdcards:" + sdcard);
            }
        }

        File[] files;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            files = getExternalFilesDirs(mContext, Environment.MEDIA_MOUNTED);
            for (File file : files) {//便历所有外部存储
                QDLogger.e("main", "外部存储：" + file.getAbsolutePath());
            }
        }

        QDLogger.d("StoragePath:", android.os.Environment.getExternalStorageDirectory().getPath());
        QDLogger.d("StoragePath:", QDFileUtil.getStoragePath(mContext, true));
        QDLogger.d("getExternalStorageState:", Environment.getExternalStorageState());
        QDLogger.i("getExternalCacheDir=" + getContext().getExternalCacheDir().getAbsolutePath());
        QDLogger.i("getFilesDir=" + getContext().getFilesDir().getAbsolutePath());
        QDLogger.i("getCacheDir=" + getContext().getCacheDir().getAbsolutePath());
        QDLogger.i("getExternalStorageDirectory=" + android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
        // QDLogger.i("getExternalStoragePublicDirectory="+android.os.Environment.getExternalStoragePublicDirectory().getAbsolutePath());
        // QDLogger.i("getExternalFilesDir="+getContext().getExternalFilesDir().getAbsolutePath());
        QDLogger.i("getExternalStorageDirectory=" + getContext().getExternalCacheDir().getAbsolutePath());
        QDLogger.i("getExternalCacheDir=" + getContext().getExternalCacheDir().getAbsolutePath());
        QDLogger.i("getRootDirectory=" + android.os.Environment.getRootDirectory().getAbsolutePath());


        QDLogger.i("getExternalFilesDir=" + mContext.getExternalFilesDir("").getParentFile().getAbsolutePath());
        QDLogger.i("FilesDir().getParentFile()=" + mContext.getFilesDir().getParentFile().getAbsolutePath());

        String path = Environment.getExternalStorageDirectory() + File.separator + "tsetfile.txt";
        QDLogger.d(path);
        File file = new File(path);
        QDFileUtil.createFile(file);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        getFileCreatTime(file, qdFile -> {
            if (qdFile != null) {
                Date date = new Date(qdFile.getModifyTime());
                QDLogger.e("文件修改时间：" + simpleDateFormat.format(date));
            }
        });


    }

    private int count;
    private void deleteEmptyFiles(String rootPath) {
        //String rootPath = Environment.getExternalStorageDirectory().getPath() ;
        File file = new File(rootPath);
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            if (file.listFiles() == null) {
                QDLogger.e("文件不存在:" + file.getPath());
                return;
            }
            if (file.listFiles().length == 0) {
                count += 1;
                QdThreadHelper.runOnUiThread(() -> tv_current.setText("正在删除第" + count + "空文件夹:" + file.getPath()));
                QDLogger.e("删除第(" + count + ")空文件夹[" + (QDFileUtil.delete(file.getPath()) ? "成功" : "失败") + "]:" + file.getPath());
                return;
            }
            QDLogger.d("文件夹:" + file.getPath() + "，文件个数(" + file.listFiles().length + ")");
            for (File file1 : file.listFiles()) {
                deleteEmptyFiles(file1.getPath());
            }
        }
    }

    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        ((Activity)getContext()).startActivityForResult(Intent.createChooser(i, "上传文件"), 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (null != uploadFile) {
                    Uri result = data == null ? null
                            : data.getData();
                    uploadFile.onReceiveValue(result);
                    uploadFile = null;
                }
                if (null != uploadFiles) {
                    Uri result = data == null ? null
                            : data.getData();
                    uploadFiles.onReceiveValue(new Uri[]{result});
                    uploadFiles = null;
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (null != uploadFile) {
                    uploadFile.onReceiveValue(null);
                    uploadFile = null;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}