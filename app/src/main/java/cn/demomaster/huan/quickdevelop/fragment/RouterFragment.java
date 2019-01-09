package cn.demomaster.huan.quickdevelop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.activity.sample.fragment.BaseFragmentActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class RouterFragment extends BaseFragment {
    //Components
    ViewGroup mView;


    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_router, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";
        Button button = mView.findViewById(R.id.btn_open_new_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opentFragment();
            }
        });
        return mView;
    }

    private void opentFragment() {
        ((BaseFragmentActivity)getActivity()).startFragment(new RouterFragment());
    }
}