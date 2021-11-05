package cn.demomaster.huan.quickdevelop.ui.fragment.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.net.RetrofitInterface;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences;
import cn.demomaster.huan.quickdeveloplibrary.helper.install.InstallHelper;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.http.HttpUtils;
import cn.demomaster.huan.quickdeveloplibrary.http.URLConstant;
import cn.demomaster.huan.quickdeveloplibrary.model.Version;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QDButton;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDDialog;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickpermission_library.PermissionHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "App更新", iconRes = R.drawable.ic_baseline_system_update_24, resType = ResType.Resource)
public class UpdateAppFragment extends BaseFragment {

    @BindView(R.id.btn_update_app)
    QDButton btn_update_app;
    @BindView(R.id.btn_install_access)
    QDButton btn_install_access;

    @BindView(R.id.btn_install_silence)
    QDButton btn_install_silence;

    @BindView(R.id.btn_install_silence2)
    QDButton btn_install_silence2;
    @BindView(R.id.btn_check_update)
    QDButton btn_check_update;
    View mView;

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_update_app, null);
        }
        ButterKnife.bind(this, mView);
        return mView;
    }

    int type = 0;

    public void initView(View rootView) {
        getActionBarTool().setTitle("文件下载");
        btn_update_app.setOnClickListener(v -> {
            type = 0;
            updateApp(mContext);
        });
        btn_install_access.setOnClickListener(v -> {
            type = 1;
            updateApp(mContext);
        });

        btn_install_silence.setOnClickListener(v -> {
            type = 2;
            updateApp(mContext);
        });

        btn_install_silence2.setOnClickListener(v -> {
            type = 3;
            updateApp(mContext);
        });
        btn_check_update.setOnClickListener(this);
    }

    //app更新
    public void updateApp(final Activity context) {
        showDialog();
        //存储权限
        PermissionHelper.requestPermission(context, PERMISSIONS_STORAGE, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                checkVersionCode(context);
            }

            @Override
            public void onRefused() {
            }
        });

    }

    /**
     * alert 消息提示框显示
     *
     * @param context  上下文
     * @param title    标题
     * @param message  消息
     * @param listener 监听器
     */
    public void showAlert(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", listener);
        builder.setCancelable(false);
        builder.setIcon(R.mipmap.ic_launcher);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void checkVersionCode(Activity context) {
        Toast.makeText(context, "正在检查更新请稍后...", Toast.LENGTH_SHORT).show();
        //Retrofit
        RetrofitInterface retrofitInterface = HttpUtils.getInstance().getRetrofit(RetrofitInterface.class, "http://www.demomaster.cn/");
        retrofitInterface.getVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(@NonNull Object response) {
                        QDLogger.i("onNext: " + JSON.toJSONString(response));
                        try {
                            //JSONObject jsonObject = JSON.parseObject(response.getData().toString());
                            //List doctors1 = JSON.parseArray(response.getData().toString(), DoctorModelApi.class);
                            //String token = jsonObject.get("token").toString();
                            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(response));
                            jsonObject = jsonObject.getJSONObject("data");
                            Version version = JSON.parseObject(JSON.toJSONString(jsonObject), Version.class);
                            int ver_code = version.getVersionCode();
                            String ver_name = version.getVersionName();
                            if (version != null && ver_code > QDAppInfoUtil.getVersionCode(context)) {
                                showUpdateAppDialog(context, version);
                                //Toast.makeText(context, "当前版本：" + getVersionName(context) + "最新版本：" + ver_name, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(context, "当前版本已是最新，无需更新！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onStart() {
                        QDLogger.i("onStart: ");
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        QDLogger.i(throwable);
                    }

                    @Override
                    public void onComplete() {
                        QDLogger.i("onComplete: ");
                    }
                });
    }

    public void showDialog() {
        QDSharedPreferences.init(getContext());
        String conf = QDFileUtil.getFromAssets(mContext, "config/update.his");
        if (conf != null) {
            List<Version> versions = JSON.parseArray(conf, Version.class);
            final Version version = versions.get(versions.size() - 1);
            QDSharedPreferences.getInstance().putBoolean(version.getVersionCode() + "", false);
        }
    }

    //app更新弹窗
    private void showUpdateAppDialog(final Context context, final Version version) {
        version.setDownloadUrl("https://b6.market.xiaomi.com/download/AppStore/084df452cadba44cb1b73603138d7fbe8aef2b76d/com.kuaiduizuoye.scan.apk");
        version.setFileName("新闻");
        new QDDialog.Builder(getContext()).setTitle("更新提示")
                .setMessage("确定要更新吗？")
                .setBackgroundRadius(30)
                .addAction("更新", (dialog, view, tag) -> {
                    dialog.dismiss();
                    //Toast.makeText(context,"正在下载更新包",Toast.LENGTH_LONG).show();
                    //InstallHelper.downloadAndInstall(mContext, version.getFileName(), version.getDownloadUrl());
                    if (type == 0) {
                        InstallHelper.downloadAndInstall(mContext, version.getFileName(), version.getDownloadUrl());
                    } else if (type == 1) {
                        InstallHelper.runInstall(mContext, new File(Environment.getExternalStorageDirectory(), "xiao.apk"));
                    } else if (type == 2) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                InstallHelper.silenceInstall(mContext, true, new File(Environment.getExternalStorageDirectory(), "xiao.apk"), new PackageInstaller.SessionCallback() {
                                    @Override
                                    public void onCreated(int sessionId) {

                                    }

                                    @Override
                                    public void onBadgingChanged(int sessionId) {

                                    }

                                    @Override
                                    public void onActiveChanged(int sessionId, boolean active) {

                                    }

                                    @Override
                                    public void onProgressChanged(int sessionId, float progress) {

                                    }

                                    @Override
                                    public void onFinished(int sessionId, boolean success) {
                                        InstallHelper.launchAPK(mContext, "");
                                    }
                                });
                            } else {
                                QDLogger.e("5.0以上用这种静默安装");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (type == 3) {
                        try {
                            InstallHelper.downloadAndSilenceInstall(getActivity(), true, version.getFileName() + ".apk", version.getDownloadUrl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).addAction("取消").setGravity_foot(Gravity.CENTER).create().show();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //下载文件
    public static void downloadFile(final Activity context, String urls, final String name) {
        final String url = urls;
        PermissionHelper.requestPermission(context, PERMISSIONS_STORAGE, new PermissionHelper.PermissionListener() {
            @Override
            public void onPassed() {
                /*DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                // 存储的目录
                request.setDestinationInExternalPublicDir(FilePath.DOWNLOAD_APP_FOLDER_NAME, FilePath.DOWNLOAD_APP_FILE_NAME);
                // 设置允许使用的网络类型，这里是移动网络和wifi都可以
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                // 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
                request.setShowRunningNotification(true);
                request.setVisibleInDownloadsUi(true);
                request.setTitle(context.getString(R.string.app_name) + "正在下载" + name);
                // 不显示下载界面
                request.setVisibleInDownloadsUi(true);
                *//*
                 * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
                 * 在/mnt/sdcard
                 * /Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错
                 * ，不设置，下载后的文件在/cache这个 目录下面
                 *//*
                //request.setDestinationInExternalFilesDir(this, null, "tar.apk");
                // 下载完成后保留 下载的notification
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                long id = downloadManager.enqueue(request);
                // TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面*/
            }

            @Override
            public void onRefused() {
                Toast.makeText(context, "请打开相关权限！", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void checkVersion() {
        // https://www.pgyer.com/apiv2/app/check
        try {
            String appKey = "2206123c28a3b818ab7abbed33b37632";
            //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), map_str2);
            //RetrofitInterface retrofitInterface = HttpUtils.getInstance().createRetrofit(RetrofitInterface.class, URLConstant.URL_BASE);
            String _api_key = "2736f790002c32b8eae7ad8e73843197";
            RetrofitInterface retrofitInterface = HttpUtils.getInstance().getRetrofit(RetrofitInterface.class, URLConstant.URL_BASE);
            retrofitInterface.checkAppVersion(_api_key, appKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<JSONObject>() {
                        @Override
                        public void onNext(@NonNull JSONObject response) {
                            QDLogger.i("查询结果: " + response);
                            if (response != null) {
                                if (response.containsKey("code") && response.getIntValue("code") == 0) {
                                    if (response.containsKey("data") && response.get("data") != null) {
                                        JSONObject dataObj = response.getJSONObject("data");
                                        if (dataObj != null && dataObj.containsKey("buildVersionNo")) {
                                            int v = Integer.parseInt(dataObj.getString("buildVersionNo"));
                                            if (v > QDAppInfoUtil.getVersionCode(getContext())) {
                                                Version version = new Version();
                                                version.setDownloadUrl(dataObj.getString("downloadURL"));
                                                showUpdateAppDialog2(getContext(), version);
                                            }else {
                                                QdToast.show("云端最新版本："+v+",当前版本："+QDAppInfoUtil.getVersionCode(getContext()));
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        protected void onStart() {

                        }

                        @Override
                        public void onError(@NonNull Throwable throwable) {
                            QDLogger.i("onError: " + throwable.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //app更新弹窗
    private void showUpdateAppDialog2(final Context context, final Version version) {
        new QDDialog.Builder(getContext()).setTitle("更新提示")
                .setMessage("检测到新版本是否前往更新？")
                .setBackgroundRadius(30)
                .addAction("前往更新", (dialog, view, tag) -> {
                    dialog.dismiss();
                    //Toast.makeText(context,"正在下载更新包",Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse("https://www.pgyer.com/UYx0");
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(uri);
                    startActivity(intent);

                }).addAction("暂不")
                .setGravity_foot(Gravity.CENTER)
                .create().show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.btn_check_update){
            checkVersion();
        }
    }
}