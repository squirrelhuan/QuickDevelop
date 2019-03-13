package cn.demomaster.huan.quickdevelop.fragment.helper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "ErrorTestFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class ErrorTestFragment extends BaseFragment {
    //Components
    View mView;
    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_errortest, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";

        return (ViewGroup) mView;
    }

    @Override
    public void initView(View rootView, ActionBarLayout actionBarLayout) {
        actionBarLayout.setTitle("异常捕获");
        mView.findViewById(R.id.btn_error_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a =0;
                int b = 1;
                int c = b/a;
            }
        });
    }

    public void initActionBarLayout(ActionBarLayout actionBarLayout) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayout.setTitle("audio play");
        actionBarLayout.setHeaderBackgroundColor(Color.RED);

    }
}