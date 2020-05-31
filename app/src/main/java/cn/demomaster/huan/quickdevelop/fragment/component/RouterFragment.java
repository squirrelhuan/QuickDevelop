package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class RouterFragment extends QDFragment {
    //Components
    ViewGroup mView;

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_router, null);
        }

        return mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayout) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayout.setTitle(titles[i]+"sss");
        actionBarLayout.setHeaderBackgroundColor(colors[i]);
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
                actionBarLayout.setTitle(titles[i]+"");
            }
        });
        Button btn_guider = mView.findViewById(R.id.btn_guider);
        btn_guider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFragment();
            }
        });

    }

    private String[] titles = {"1", "2", "3", "4"};
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
    private void opentFragment() {
        ((QDActivity)getContext()).getFragmentHelper().startFragment(mContext,new RouterFragment());
    }

    private void startFragment(){
        ((QDActivity)getContext()).getFragmentHelper().startFragment(mContext,new GuiderFragment());
    }


    //记录用户首次点击返回键的时间
    private long firstClickTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isRootFragment()){
            if (System.currentTimeMillis() - firstClickTime > 2000) {
                QdToast.show(mContext, "再点击退出 activity");
                firstClickTime = System.currentTimeMillis();
            }else {
                getActivity().finish();
            }
            return true;
        }
       // QdToast.show(mContext, "onKeyDown isRootFragment="+isRootFragment());
        return super.onKeyDown(keyCode,event);
    }

}