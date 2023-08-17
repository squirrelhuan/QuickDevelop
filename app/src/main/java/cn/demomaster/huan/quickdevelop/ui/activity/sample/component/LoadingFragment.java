package cn.demomaster.huan.quickdevelop.ui.activity.sample.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleBallView;
import cn.demomaster.qdrouter_library.actionbar.LoadStateType;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.qdrouter_library.view.StateView;

@ActivityPager(name = "加载动画",preViewClass = LoadingCircleBallView.class,resType = ResType.Custome)
public class LoadingFragment extends QuickFragment {

    private StateView sv_loading;
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_loading, null);
        return view;
    }

    @Override
    public void initView(View rootView) {
        sv_loading = findViewById(R.id.sv_loading);
        sv_loading.setStateType(LoadStateType.LOADING);

//        ViewTreeObserver.OnDrawListener onParentViewDrawListener = () -> {
//            //QDLogger.println("viewParent重绘");
//            //invalidate();
//        };
//        //view重绘时回调
//        getWindow().getDecorView().getViewTreeObserver().addOnDrawListener(onParentViewDrawListener);
    }
}
