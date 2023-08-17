package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifeCycleClassInfo;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifeCycleEvent;
import cn.demomaster.huan.quickdeveloplibrary.lifecycle.LifecycleView;
import cn.demomaster.huan.quickdeveloplibrary.util.lifecycle.LifecycleManager;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.QuickToggleButton;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.quicksticker_annotations.BindView;
import cn.demomaster.quicksticker_annotations.QuickStickerBinder;


/**
 * 时域图
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "生命周期查看器", preViewClass = TextView.class, resType = ResType.Custome)
public class lifecycleTimerFragment extends QuickFragment {

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_lifecycle_timer, null);
        return mView;
    }
    @BindView(R.id.toggle_enable)
    QuickToggleButton toggle_enable;
    public void initView(View rootView) {
        QuickStickerBinder.getInstance().bind(this,rootView);
        LifecycleView lifecycleView = rootView.findViewById(R.id.timeDomainPlotView);
        LinkedHashMap<LifeCycleClassInfo, List<LifeCycleEvent>> listLinkedHashMap = LifecycleManager.getInstance(mContext).getLifecycleTimerData().getLinePointsMap();
        lifecycleView.setLinePoints(listLinkedHashMap);
        toggle_enable.setChecked(LifecycleManager.getInstance(mContext).isEnable());
        toggle_enable.setOnToggleChanged((view, on) -> LifecycleManager.getInstance(mContext).setEnable(on));

        RadioGroup rg_transitionType = rootView.findViewById(R.id.rg_transitionType);
        rg_transitionType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_transitionType_01:
                    lifecycleView.setTransitionType(LifecycleView.TransitionType.horizontal);
                    break;
                case R.id.rb_transitionType_02:
                    lifecycleView.setTransitionType(LifecycleView.TransitionType.vertical);
                    break;
                case R.id.rb_transitionType_03:
                    lifecycleView.setTransitionType(LifecycleView.TransitionType.transitionXY);
                    break;
                case R.id.rb_transitionType_04:
                    lifecycleView.setTransitionType(LifecycleView.TransitionType.none);
                    break;
            }
        });
        RadioGroup rg_scaleType = rootView.findViewById(R.id.rg_scaleType);
        rg_scaleType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rg_scaleType_01:
                    lifecycleView.setScaleType(LifecycleView.ScaleType.scaleX);
                    break;
                case R.id.rg_scaleType_02:
                    lifecycleView.setScaleType(LifecycleView.ScaleType.scaleY);
                    break;
                case R.id.rg_scaleType_03:
                    lifecycleView.setScaleType(LifecycleView.ScaleType.scaleXY);
                    break;
                case R.id.rg_scaleType_04:
                    lifecycleView.setScaleType(LifecycleView.ScaleType.none);
                    break;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuickStickerBinder.getInstance().unBind(this);
    }
}