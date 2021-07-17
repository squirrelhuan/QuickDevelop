package cn.demomaster.huan.quickdevelop;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.view.webview.QDWebView;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.quickpermission_library.PermissionHelper;

import static cn.demomaster.huan.quickdeveloplibrary.util.system.QDAppInfoUtil.getAppName;

/**
 * 測試 h5頁面 键盘遮挡问题
 */
public class QDMainFragmentActivity2 extends QDActivity {

    QDWebView qdWebView;
    @Override
    public boolean isUseActionBarLayout() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain2);
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        qdWebView = findViewById(R.id.webView);
        qdWebView.loadUrl(url);
       // qdWebView.loadUrl("http:\\/\\/ucipchatlib.astro.nxengine.com\\/webchat\\/chat.html?c=31&jId=79");//"http://192.168.199.118:8080/webchat/chat.html?c=1&jId=81");
        PermissionHelper.getInstance().requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},null);
        QDLogger.d("getAppName="+getAppName(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        qdWebView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);//屏蔽转入转出动画
    }

    //当指定了android:configChanges="orientation"后,方向改变时onConfigurationChanged被调用,并且activity不再销毁重建
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                Log.i(TAG,"竖屏");
                break;
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                Log.i(TAG,"横屏");
            default:
                break;
        }
    }

}
