package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.ui.file.FileExplorerFragment;
import cn.demomaster.huan.quickdeveloplibrary.util.CrashHandler;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;

@ActivityPager(name = "异常捕获", preViewClass = TextView.class, resType = ResType.Resource,iconRes = R.mipmap.ic_crash)
public class CrashActivity extends QuickActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("finishWithActivity", true);
        bundle.putString("DIR_PATH_KEY", CrashHandler.getInstance().getCrashCacheDir().getAbsolutePath());
        intent.putExtras(bundle);
        startFragment(new FileExplorerFragment(), android.R.id.content, intent);
    }
}