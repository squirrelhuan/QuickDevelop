package cn.demomaster.huan.quickdevelop.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import java.io.File;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;

public class QuickWebViewActivity extends QuickActivity {

    private static final String EXTERNAL_FILES = "external_files";
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_web_view);
        webview = findViewById(R.id.webview);
        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            //android10以上转换
            if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
                File file = new File(uri.getPath());
                webview.loadUrl(file.getAbsolutePath());
            } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                webview.loadUrl(uri.toString());
            }
        }
    }
}