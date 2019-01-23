package cn.demomaster.huan.quickdevelop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.MainActivity;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.activity.sample.CenterHorizontalActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.PickActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.PictureSelectActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.TabMenuActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.actionbar.ActionBarActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.actionbar.ActionBarTipActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.QDActionDialogActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.QDialogActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.RatingBarActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.LoadingActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.ToggleButtonActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.fragment.BaseFragmentActivity;
import cn.demomaster.huan.quickdevelop.adapter.ComponentAdapter;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(iconRes = R.mipmap.ic_launcher)
public class ComponentFragment extends Fragment {
    //Components
    View mView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_component, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";
        initView(mView);
        return mView;
    }

    private RecyclerView recyclerView;
    private ComponentAdapter componentAdapter;

    private void initView(View mView) {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        componentAdapter = new ComponentAdapter(getContext());
        List<Class> classList = new ArrayList<>();
        classList.add(ToggleButtonActivity.class);
        classList.add(CenterHorizontalActivity.class);
        classList.add(LoadingActivity.class);
        classList.add(PickActivity.class);
        classList.add(TabMenuActivity.class);
        classList.add(PictureSelectActivity.class);
        classList.add(ActionBarActivity.class);
        classList.add(RatingBarActivity.class);
        classList.add(ActionBarTipActivity.class);
        classList.add(QDialogActivity.class);
        classList.add(QDActionDialogActivity.class);
        classList.add(BaseFragmentActivity.class);
        classList.add(GuiderFragment.class);
        classList.add(AudioRecordFragment.class);




        classList.add(MainActivity.class);

        componentAdapter.updateList(classList);
        //设置Adapter
        recyclerView.setAdapter(componentAdapter);
        //设置分隔线
        //recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
        //设置行级分割线使用的divider
        //recyclerView.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getContext(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

        int spanCount = 3;
        //使用网格布局展示
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        //设置分隔线
        recyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), spanCount));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}