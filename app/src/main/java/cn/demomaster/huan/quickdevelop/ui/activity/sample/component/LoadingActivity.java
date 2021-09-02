package cn.demomaster.huan.quickdevelop.ui.activity.sample.component;

import android.os.Bundle;
import android.view.ViewTreeObserver;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.activity.BaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadStateType;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleBallView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.qdlogger_library.QDLogger;

@ActivityPager(name = "加载动画",preViewClass = LoadingCircleBallView.class,resType = ResType.Custome)
public class LoadingActivity extends BaseActivity {

    private StateView sv_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        sv_loading = findViewById(R.id.sv_loading);
        sv_loading.setStateType(LoadStateType.LOADING);

        ViewTreeObserver.OnDrawListener onParentViewDrawListener = () -> {
            //QDLogger.println("viewParent重绘");
            //invalidate();
        };
        //view重绘时回调
        getWindow().getDecorView().getViewTreeObserver().addOnDrawListener(onParentViewDrawListener);
    }
}
