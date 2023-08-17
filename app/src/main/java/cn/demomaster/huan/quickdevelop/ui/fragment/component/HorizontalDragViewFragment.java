package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

/**
 * 横向滑动
 */
@ActivityPager(name = "侧拉布局",preViewClass = TextView.class,resType = ResType.Resource,iconRes = R.mipmap.ic_database)
public class HorizontalDragViewFragment extends QuickFragment {

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_horizontal_drag_view, null);
        return view;
    }

    @Override
    public void initView(View rootView) {

    }
}