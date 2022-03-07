package cn.demomaster.huan.quickdeveloplibrary.ui.error;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.ui.file.FileExplorerFragment;
import cn.demomaster.huan.quickdeveloplibrary.ui.file.TextDetailFragment;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;

import static cn.demomaster.huan.quickdeveloplibrary.util.CrashHandler.CRASH_ERROR_DATA;

/**
 * 异常详情页
 */
public class ErrorCrashActivity extends QuickActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_log);
        setTitle("Error");
        String error = getIntent().getStringExtra(CRASH_ERROR_DATA);
        System.out.println("CRASH_ERROR_DATA ="+error);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("finishWithActivity", true);
        //bundle.putString("DIR_PATH_KEY", CrashHandler.getInstance().getCrashCacheDir().getAbsolutePath());
        bundle.putString("FileType", "txt");
        bundle.putString("Data", error);
        intent.putExtras(bundle);
        bundle.putString("FILE_PATH_KEY", null);
        intent.putExtras(bundle);
        startFragment(new TextDetailFragment(), android.R.id.content, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
