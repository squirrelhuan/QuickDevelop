package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarTip;
import cn.demomaster.huan.quickdeveloplibrary.util.FileUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "FileManagerFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class FileManagerFragment extends QDBaseFragment {

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
    View mView;
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_filemanager, null);
        }
        ButterKnife.bind(this,mView);
        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {
        actionBarLayout.setTitle("文件管理");
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String rootPath = Environment.getExternalStorageDirectory().getPath() ;
                        List<File> files = FileUtil.getEmptyFiles(rootPath);
                        for (File f: files){
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
                QDActionDialog  qdActionDialog = new QDActionDialog.Builder(mContext).setContentView(new LoadingCircleView(mContext)).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setTopImage(R.mipmap.quickdevelop_ic_launcher).setMessage("删除中...").create();
                qdActionDialog.show();
                btn_delete.setEnabled(false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String rootPath = Environment.getExternalStorageDirectory().getPath() ;
                        deleteEmptyFiles(rootPath);

                        btn_delete.setEnabled(true);
                        btn_delete.setText("删除文件");
                        qdActionDialog.dismiss();
                    }
                });
                thread.run();
            }
        });

    }

    private int count ;
    private void deleteEmptyFiles(String rootPath) {
        //String rootPath = Environment.getExternalStorageDirectory().getPath() ;
        File file = new File(rootPath);
        if (file.isDirectory()) {
            QDLogger.d("正在读取:"+file.getPath());
            if (file.listFiles().length == 0) {
                if(file.isDirectory()&&file.listFiles().length<1){
                    count+=1;
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_current.setText("正在删除第"+count+"空文件夹:"+file.getPath());
                        }
                    });

                    QDLogger.d("删除空文件夹:"+file.getPath());
                    FileUtil.delete(file.getPath());
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