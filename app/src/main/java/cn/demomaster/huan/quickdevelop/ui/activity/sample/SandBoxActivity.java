package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.ui.file.FileExplorerFragment;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;

/**
 * 沙箱
 */
@ActivityPager(name = "沙箱浏览", preViewClass = TextView.class, resType = ResType.Resource,iconRes = R.mipmap.ic_sandbox)
public class SandBoxActivity extends QuickActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sand_box);

        ArrayList<String> paths = new ArrayList<>();
        paths.add(mContext.getFilesDir().getParentFile().getAbsolutePath());
        paths.add(mContext.getExternalCacheDir().getAbsolutePath());
        paths.add(mContext.getExternalFilesDir(null).getAbsolutePath());

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        //bundle.putString("DIR_PATH_KEY", mContext.getExternalFilesDir("").getParentFile().getAbsolutePath());
        bundle.putStringArrayList("DIR_PATHS_KEY", paths);
        bundle.putBoolean("finishWithActivity", true);
        intent.putExtras(bundle);
        startFragment(new FileExplorerFragment(), android.R.id.content, intent);
    }
}