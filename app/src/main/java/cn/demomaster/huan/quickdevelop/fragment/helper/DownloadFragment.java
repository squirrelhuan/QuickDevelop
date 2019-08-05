package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.DownloadTask;
import cn.demomaster.huan.quickdeveloplibrary.helper.download.OnDownloadProgressListener;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Downloader", preViewClass = StateView.class, resType = ResType.Custome)
public class DownloadFragment extends QDBaseFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    //Components
    @BindView(R.id.btn_download_01)
    QDButton btn_download_01;
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
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
        actionBarLayoutOld.setTitle("文件下载");
        btn_download_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadHelper.DownloadBuilder(mContext).setUrl("http://wap.apk.anzhi.com/data5/apk/201907/19/941a15c7bdcc39a9ef33e22cb8dd7680_70536400.apk")
                        .setFileName("抖音.apk")
                        .setOnProgressListener(
                                new OnDownloadProgressListener() {
                                    @Override
                                    public void onComplete(DownloadTask downloadTask) {
                                        QDLogger.i("1下载完成" + downloadTask.getFileName() + "->" + downloadTask.getDownIdUri().getPath());
                                    }

                                    @Override
                                    public void onProgress(long downloadId, String name, float progress) {
                                        QDLogger.d("下载状态：" + downloadId + "," + name + "," + progress);
                                    }
                                }).start();

                new DownloadHelper.DownloadBuilder(mContext).setUrl("https://alissl.ucdl.pp.uc.cn/fs08/2019/07/05/6/2_b0cfb9e044477a4a1ea8ef8656fb69c9.apk?yingid=wdj_web&fname=%E5%BD%B1%E9%9F%B3%E5%85%88%E9%94%8B&pos=wdj_web%2Fdetail_normal_dl%2F0&appid=296927&packageid=800798582&apprd=296927&iconUrl=http%3A%2F%2Fandroid-artworks.25pp.com%2Ffs08%2F2019%2F07%2F08%2F4%2F2_41f75e4a7e71a2c47497e01a3c24ca43_con.png&pkg=com.xfplay.play&did=18c032fc0601f7543599f3bb2b4e65da&vcode=500730&md5=caa800e0c2c0575641d001377f12e3a1")
                        .setFileName("影音.apk")
                        .setOnProgressListener(
                                new OnDownloadProgressListener() {
                                    @Override
                                    public void onComplete(DownloadTask downloadTask) {
                                        QDLogger.i("2下载完成" + downloadTask.getFileName() + "->" + downloadTask.getDownIdUri().getPath());
                                    }

                                    @Override
                                    public void onProgress(long downloadId, String name, float progress) {
                                        QDLogger.d("下载状态：" + downloadId + "," + name + "," + progress);
                                    }
                                }).start();;
            }
        });
    }

}