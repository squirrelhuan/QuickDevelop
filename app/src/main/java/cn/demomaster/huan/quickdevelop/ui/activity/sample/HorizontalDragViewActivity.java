package cn.demomaster.huan.quickdevelop.ui.activity.sample;

import android.os.Bundle;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;

/**
 * 横向滑动
 */
@ActivityPager(name = "横向抽屉",preViewClass = TextView.class,resType = ResType.Resource,iconRes = R.mipmap.ic_database)
public class HorizontalDragViewActivity extends QuickActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_drag_view);
    }
}