package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.qdrouter_library.base.activity.QuickActivity;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class RouterFragment extends BaseFragment {

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.fragment_layout_router, null);
        initView(mView);
        return mView;
    }

    public void initView(View rootView) {
        int i = (int) (Math.random() * 10 % 4);
        getActionBarTool().setTitle(titles[i]+"sss");
        getActionBarTool().setHeaderBackgroundColor(colors[i]);
        Button button = rootView.findViewById(R.id.btn_open_new_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opentFragment();
            }
        });
        Button btn_set_title = rootView.findViewById(R.id.btn_set_title);
        btn_set_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) (Math.random() * 10 % 4);
                // actionBarLayout.setTitle(titles[i]+"");
            }
        });
        Button btn_guider = rootView.findViewById(R.id.btn_guider);
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
         ((QuickActivity)getContext()).getFragmentHelper().startFragment(new RouterFragment());
    }

    private void startFragment() {
        ((QuickActivity)getContext()).getFragmentHelper().startFragment(new GuiderFragment());
        // ((QDActivity)getContext()).getFragmentHelper().addElement( new GuiderFragment());
    }

    //记录用户首次点击返回键的时间
    private long firstClickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isRootFragment()) {
            if (System.currentTimeMillis() - firstClickTime > 2000) {
                QdToast.show(mContext, "再点击退出 activity");
                firstClickTime = System.currentTimeMillis();
            } else {
                getActivity().finish();
            }
            return true;
        }
        // QdToast.show(mContext, "onKeyDown isRootFragment="+isRootFragment());
        return super.onKeyDown(keyCode, event);
    }

}