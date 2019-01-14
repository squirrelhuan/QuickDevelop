package cn.demomaster.huan.quickdevelop.fragment;

import android.graphics.Color;
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
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;


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
        Button btn_set_title = mView.findViewById(R.id.btn_set_title);
        btn_set_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) (Math.random() * 10 % 4);
                getActionBarLayout().setTitle(titles[i]+"");
            }
        });
        Button btn_guider = mView.findViewById(R.id.btn_guider);
        btn_guider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFragment();
            }
        });

        return mView;
    }

    private String[] titles = {"1", "2", "3", "4"};
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};

    public void initActionBarLayout(ActionBarLayout actionBarLayout) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayout.setTitle(titles[i]+"---------ASDFGGHHJ");
        actionBarLayout.setHeaderBackgroundColor(colors[i]);
    }


    private void opentFragment() {
        FragmentActivityHelper.getInstance().startFragment(new RouterFragment());
    }

    private void startFragment(){
        FragmentActivityHelper.getInstance().startFragment(new GuiderFragment());
    }
}