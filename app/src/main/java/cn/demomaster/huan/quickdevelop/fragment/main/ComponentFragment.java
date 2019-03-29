package cn.demomaster.huan.quickdevelop.fragment.main;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import cn.demomaster.huan.quickdevelop.MainActivity;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.activity.sample.CenterHorizontalActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.PickActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.PictureSelectActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.TabMenuActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.TestActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.actionbar.ActionBarTipActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.LoadingActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.QDActionDialogActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.QDialogActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.RatingBarActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.component.ToggleButtonActivity;
import cn.demomaster.huan.quickdevelop.activity.sample.fragment.BaseFragmentActivity;
import cn.demomaster.huan.quickdevelop.adapter.ComponentAdapter;
import cn.demomaster.huan.quickdevelop.fragment.component.AudioRecordFragment;
import cn.demomaster.huan.quickdevelop.fragment.component.GuiderFragment;
import cn.demomaster.huan.quickdevelop.fragment.component.NdkTestFragment;
import cn.demomaster.huan.quickdevelop.fragment.component.NestedScrollViewFragment;
import cn.demomaster.huan.quickdevelop.fragment.component.PushCardFragment;
import cn.demomaster.huan.quickdevelop.fragment.component.QDTipPopupFragment;
import cn.demomaster.huan.quickdevelop.fragment.component.SoundFragment;
import cn.demomaster.huan.quickdevelop.fragment.component.UMengShareFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;


/**
 * Components视图
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(iconRes = R.mipmap.quickdevelop_ic_launcher)
public class ComponentFragment extends QDBaseFragment {

    private RecyclerView recyclerView;
    private ComponentAdapter componentAdapter;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_layout_component, null);
    }

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public boolean isUseActionBarLayout() {
        return false;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {
        /*actionBarLayout.setActionBarType(ActionBarInterface.ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
//        actionBarLayout.getLeftView().setVisibility(View.GONE);
        actionBarLayout.setBackGroundColor(Color.RED);*/

        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        componentAdapter = new ComponentAdapter(getContext());
        List<Class> classList = new ArrayList<>();
        classList.add(ToggleButtonActivity.class);
        classList.add(CenterHorizontalActivity.class);
        classList.add(LoadingActivity.class);
        classList.add(PickActivity.class);
        classList.add(TabMenuActivity.class);
        classList.add(PictureSelectActivity.class);
        classList.add(TestActivity.class);
        classList.add(RatingBarActivity.class);
        classList.add(ActionBarTipActivity.class);
        classList.add(QDialogActivity.class);
        classList.add(QDActionDialogActivity.class);
        classList.add(BaseFragmentActivity.class);
        classList.add(GuiderFragment.class);
        classList.add(AudioRecordFragment.class);
        classList.add(NdkTestFragment.class);
        classList.add(NestedScrollViewFragment.class);
        classList.add(SoundFragment.class);
        classList.add(UMengShareFragment.class);
        classList.add(PushCardFragment.class);
        classList.add(QDTipPopupFragment.class);
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