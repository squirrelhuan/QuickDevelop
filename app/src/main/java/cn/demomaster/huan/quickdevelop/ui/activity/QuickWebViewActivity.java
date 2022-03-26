package cn.demomaster.huan.quickdevelop.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.qdlogger_library.QDLogger;
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