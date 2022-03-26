package cn.demomaster.huan.quickdeveloplibrary.ui.file;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;


import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.ui.file.FileExplorerFragment;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;

/**
 * 文件内浏览
 */
public class FileExplorerActivity extends QuickActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_log);
        Bundle bundle1 = getIntent().getExtras();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("finishWithActivity", true);
        //bundle.putString("DIR_PATH_KEY", CrashHandler.getInstance().getCrashCacheDir().getAbsolutePath());
        bundle.putString("DIR_PATH_KEY", bundle1.getString("DIR_PATH_KEY"));
        intent.putExtras(bundle);
        startFragment(new FileExplorerFragment(), android.R.id.content, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}