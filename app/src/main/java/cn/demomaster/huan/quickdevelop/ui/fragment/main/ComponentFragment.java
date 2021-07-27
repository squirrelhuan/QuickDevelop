package cn.demomaster.huan.quickdevelop.ui.fragment.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.ComponentAdapter;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.ActionBarActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.AdsActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.CenterHorizontalActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.PickActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.PictureSelectActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.TabMenuActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.actionbar.ActionBarTipActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.component.LoadingActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.component.QDActionDialogActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.component.QDialogActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.component.SlidingPanelLayoutActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.component.ToggleButtonActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.fragment.AppletsFragmentActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.fragment.BaseFragmentActivity;
import cn.demomaster.huan.quickdevelop.ui.activity.sample.fragment.BasePaperActivity;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.AnimitionFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.AudioRecordFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.BlurFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.ColorPickerFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.EmptyLayoutFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.FramelayoutFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.GuiderFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.H5Fragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.NdkTestFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.NestedScrollViewFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.PopUpFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.PushCardFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.QDTipPopupFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.QdButtonFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.RatingBarFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.SoundFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.StackSlidingLayoutFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.TextSpanFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.TimeDomainPlotFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.WheelImageFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.component.lifecycleTimerFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.helper.CompressLayoutFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;


/**
 * Components视图
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(iconRes = R.mipmap.quickdevelop_ic_launcher)
public class ComponentFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ComponentAdapter componentAdapter;

    @Override
    public boolean isUseActionBarLayout() {
        return false;//不带导航栏
    }

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_component, null);
        return view;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        componentAdapter = new ComponentAdapter(getContext());
        List<Class> classList = new ArrayList<>();

        classList.add(AdsActivity.class);
        classList.add(QdButtonFragment.class);
        classList.add(TextSpanFragment.class);

        classList.add(EmptyLayoutFragment.class);
        classList.add(SlidingPanelLayoutActivity.class);
        classList.add(FramelayoutFragment.class);
        classList.add(CompressLayoutFragment.class);
        classList.add(WheelImageFragment.class);
        classList.add(ToggleButtonActivity.class);
        classList.add(TimeDomainPlotFragment.class);
        classList.add(lifecycleTimerFragment.class);
        classList.add(AnimitionFragment.class);

        classList.add(CenterHorizontalActivity.class);
        classList.add(LoadingActivity.class);
        classList.add(PickActivity.class);
        classList.add(TabMenuActivity.class);
        classList.add(PictureSelectActivity.class);
        classList.add(ActionBarActivity.class);
        classList.add(RatingBarFragment.class);
        classList.add(ActionBarTipActivity.class);
        classList.add(QDialogActivity.class);
        classList.add(QDActionDialogActivity.class);
        classList.add(PopUpFragment.class);

        classList.add(H5Fragment.class);
        classList.add(AppletsFragmentActivity.class);
        classList.add(BaseFragmentActivity.class);

        classList.add(BasePaperActivity.class);
        classList.add(GuiderFragment.class);
        classList.add(AudioRecordFragment.class);
        classList.add(NdkTestFragment.class);
        classList.add(NestedScrollViewFragment.class);
        classList.add(SoundFragment.class);
        classList.add(ColorPickerFragment.class);
        classList.add(BlurFragment.class);
        classList.add(PushCardFragment.class);
        classList.add(QDTipPopupFragment.class);
        classList.add(StackSlidingLayoutFragment.class);

        componentAdapter.updateList(classList);
        //设置Adapter
        recyclerView.setAdapter(componentAdapter);
        //设置分隔线
        //recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
        //设置行级分割线使用的divider
        //recyclerView.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getContext(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

        int spanCount = 3;
        //使用网格布局展示NanumSquareRoundr.ttf
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        //设置分隔线
        recyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), spanCount));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setFont(rootView);
    }

    void setFont(View view) {
        Typeface typeFaceHold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumSquareRoundEB.ttf");
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View view1 = viewGroup.getChildAt(i);
                setFont(view1);
            }
        } else {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeFaceHold);
            }
        }
    }

}