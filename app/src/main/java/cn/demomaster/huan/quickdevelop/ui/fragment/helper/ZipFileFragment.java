package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

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
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.QdThreadHelper;
import cn.demomaster.huan.quickdeveloplibrary.model.QDFile;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;
import cn.demomaster.qdlogger_library.QDLogger;

import static cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil.getFileCreatTime;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "文件压缩", preViewClass = StateView.class, resType = ResType.Custome)
public class ZipFileFragment extends BaseFragment {

    @BindView(R.id.btn_search)
    QDButton btn_search;
    @BindView(R.id.btn_delete)
    QDButton btn_delete;
    @BindView(R.id.tv_current)
    TextView tv_current;
    @BindView(R.id.tv_count)
    TextView tv_count;
    View mView;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_filemanager, null);
        }
        ButterKnife.bind(this, mView);
        return (ViewGroup) mView;
    }

    public void initView(View rootView) {
        getActionBarTool().setTitle("文件管理");
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QdThreadHelper.runOnSubThread(new Runnable() {
                    @Override
                    public void run() {
                        String rootPath = Environment.getExternalStorageDirectory().getPath();
                        List<File> files = QDFileUtil.getEmptyFiles(rootPath);
                        for (File f : files) {
                            QDLogger.d(f);
                        }
                    }
                });
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QDActionDialog qdActionDialog = new QDActionDialog.Builder(mContext).setContentView(new LoadingCircleView(mContext)).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setTopImage(R.mipmap.quickdevelop_ic_launcher).setMessage("删除中...").create();
                qdActionDialog.show();
                btn_delete.setEnabled(false);
                QdThreadHelper.runOnSubThread(new Runnable() {
                    @Override
                    public void run() {
                        String rootPath = Environment.getExternalStorageDirectory().getPath();
                        deleteEmptyFiles(rootPath);

                        btn_delete.setEnabled(true);
                        btn_delete.setText("删除文件");
                        qdActionDialog.dismiss();
                    }
                });
            }
        });

        QDLogger.i("getExternalCacheDir=" + getContext().getExternalCacheDir().getAbsolutePath());
        QDLogger.i("getFilesDir=" + getContext().getFilesDir().getAbsolutePath());
        QDLogger.i("getCacheDir=" + getContext().getCacheDir().getAbsolutePath());
        QDLogger.i("getExternalStorageDirectory=" + Environment.getExternalStorageDirectory().getAbsolutePath());
        // QDLogger.i("getExternalStoragePublicDirectory="+android.os.Environment.getExternalStoragePublicDirectory().getAbsolutePath());
        // QDLogger.i("getExternalFilesDir="+getContext().getExternalFilesDir().getAbsolutePath());
        QDLogger.i("getExternalStorageDirectory=" + getContext().getExternalCacheDir().getAbsolutePath());
        QDLogger.i("getExternalCacheDir=" + getContext().getExternalCacheDir().getAbsolutePath());
        QDLogger.i("getRootDirectory=" + Environment.getRootDirectory().getAbsolutePath());

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
        if (file.isDirectory()) {
            QDLogger.d("正在读取:" + file.getPath());
            if (file.listFiles().length == 0) {
                if (file.isDirectory() && file.listFiles().length < 1) {
                    count += 1;
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_current.setText("正在删除第" + count + "空文件夹:" + file.getPath());
                        }
                    });

                    QDLogger.d("删除空文件夹:" + file.getPath());
                    QDFileUtil.delete(file.getPath());
                }
            } else {
                for (File file1 : file.listFiles()) {
                    deleteEmptyFiles(file1.getPath());
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