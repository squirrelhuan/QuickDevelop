package cn.demomaster.huan.quickdevelop.activity.sample.component;

import android.os.Bundle;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingCircleBallView;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

@ActivityPager(name = "Loading",preViewClass = LoadingCircleBallView.class,resType = ResType.Custome)
public class LoadingActivity extends BaseActivityParent {

    private StateView sv_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getActionBarLayoutOld().setTitle("加载动画");

        sv_loading = findViewById(R.id.sv_loading);
        sv_loading.setStateType(StateView.StateType.LOADING);
    }
}
