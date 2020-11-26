package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.PermissionManager;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.model.QDFile;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;
import cn.demomaster.qdlogger_library.QDLogger;

import static androidx.core.content.ContextCompat.getExternalFilesDirs;
import static cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil.getFileCreatTime;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "FileManager", preViewClass = TextView.class, resType = ResType.Custome)
public class FileManagerFragment extends BaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

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
        View mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_filemanager, null);
        return (ViewGroup) mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().setTitle("文件管理");
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                thread.run();

            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                PermissionManager.getInstance().chekPermission(mContext, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionManager.PermissionListener() {

                    @Override
                    public void onPassed() {
                        Toast.makeText(mContext, "通过", Toast.LENGTH_LONG).show();
                        QDActionDialog qdActionDialog = new QDActionDialog.Builder(mContext).setContentView(new LoadingCircleView(mContext)).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setTopImage(R.mipmap.quickdevelop_ic_launcher).setMessage("删除中...").create();
                        qdActionDialog.show();
                        btn_delete.setEnabled(false);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
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
                        });
                        thread.run();
                    }

                    @Override
                    public void onRefused() {
                        Toast.makeText(mContext, "拒绝", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        List<String> sdcards = QDFileUtil.getExtSDCardPathList();
        if (sdcards != null) {
            for (String sdcard : sdcards) {
                QDLogger.d("sdcards:" + sdcard);
            }
        }

        File[] files;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            files = getExternalFilesDirs(mContext, Environment.MEDIA_MOUNTED);
            for (File file : files) {
                QDLogger.e("main", "得到的全部外存：" + file.getAbsolutePath());


//便历所有外部存储
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

        String path = Environment.getExternalStorageDirectory() + File.separator + "tsetfile.txt";
        QDLogger.d(path);
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        getFileCreatTime(mContext, file, new QDFileUtil.OnSearchListener() {
            @Override
            public void onResult(QDFile qdFile) {
                if (qdFile != null) {
                    //Date date1 = new Date(System.currentTimeMillis());
                    Date date = new Date(qdFile.getModifyTime());
                    QDLogger.e("-------------------------" + simpleDateFormat.format(date));
                }
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
                QdThreadHelper.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_current.setText("正在删除第" + count + "空文件夹:" + file.getPath());
                    }
                });
                QDLogger.e("删除第(" + count + ")空文件夹[" + (QDFileUtil.delete(file.getPath()) ? "成功" : "失败") + "]:" + file.getPath());

                return;
            }
            QDLogger.d("文件夹:" + file.getPath() + "，文件个数(" + file.listFiles().length + ")");
            for (File file1 : file.listFiles()) {
                deleteEmptyFiles(file1.getPath());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}