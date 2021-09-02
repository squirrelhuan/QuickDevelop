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
        return mView;
    }

    public void initView(View rootView) {
        int i = (int) (Math.random() * 10 % 4);
        getActionBarTool().setTitle(titles[i]+"sss");
        getActionBarTool().getActionBarLayout().getActionBarView().setBackgroundColor(colors[i]);
        Button button = rootView.findViewById(R.id.btn_open_new_fragment);
        button.setOnClickListener(view -> startFragment(new RouterFragment(),R.id.container1,null));
        Button btn_set_title = rootView.findViewById(R.id.btn_set_title);
        btn_set_title.setOnClickListener(view -> {
            int i1 = (int) (Math.random() * 10 % 4);
            setTitle(titles[i1]+"");
        });
        Button btn_guider = rootView.findViewById(R.id.btn_guider);
        btn_guider.setOnClickListener(view -> startFragment(new GuiderFragment(),R.id.container1,null));
    }

    private String[] titles = {"1", "2", "3", "4"};
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
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