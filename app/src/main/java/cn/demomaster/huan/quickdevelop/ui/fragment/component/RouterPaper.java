package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.qdrouter_library.paper.Paper;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class RouterPaper extends Paper {
    //Components

    public void initView(View view) {
        int i = (int) (Math.random() * 10 % 4);
        view.setBackgroundColor(colors[i]);
        //actionBarLayout.setTitle(titles[i]+"sss");
        //actionBarLayout.setHeaderBackgroundColor(colors[i]);
        Button button = view.findViewById(R.id.btn_open_new_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opentFragment();
            }
        });
        Button btn_set_title = view.findViewById(R.id.btn_set_title);
        btn_set_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) (Math.random() * 10 % 4);
                // actionBarLayout.setTitle(titles[i]+"");
            }
        });
        Button btn_guider = view.findViewById(R.id.btn_guider);
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
        getMyPayerManager().addElement(new RouterPaper());
        //((QDActivity)getContext()).getFragmentHelper().startFragment(mContext,new RouterPaper());
    }

    private void startFragment() {
        //((QDActivity)getContext()).getFragmentHelper().startFragment(mContext,new GuiderFragment());
    }


    //记录用户首次点击返回键的时间
    private long firstClickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if(isRootFragment()){
            if (System.currentTimeMillis() - firstClickTime > 2000) {
                QdToast.show(mContext, "再点击退出 activity");
                firstClickTime = System.currentTimeMillis();
            }else {
                getActivity().finish();
            }
            return true;
        }*/
        // QdToast.show(mContext, "onKeyDown isRootFragment="+isRootFragment());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public View onCreatView(LayoutInflater inflater, ViewGroup container) {
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_layout_router, null);
        initView(view);
        return view;
    }
}