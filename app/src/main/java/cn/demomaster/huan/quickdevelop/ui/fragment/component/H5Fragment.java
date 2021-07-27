package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdevelop.ui.fragment.designer.WebViewFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.qdrouter_library.view.ImageTextView;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "H5任意门", preViewClass = TextView.class, resType = ResType.Custome)
public class H5Fragment extends BaseFragment {

    @Override
    public int getHeadlayoutResID() {
        return R.layout.qd_applet_actionbar_common;
    }

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_layout_h5, null);
       /* View view = new FrameLayout(getContext());
        AjsLayoutInflater.parseXmlAssetsForLayout(getContext(), "config/layout_test.xml", view);*/
        return view;
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        ImageTextView imageTextView = findViewById(R.id.it_actionbar_title);
        if (imageTextView != null) {
            imageTextView.setText(getTitle());
        }
    }

    @Override
    public void initCreatView(View mView) {
        super.initCreatView(mView);
        EditText et_url = findViewById(R.id.et_url);
        Button btn_jump = findViewById(R.id.btn_jump);
        btn_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // WebViewFragment webViewFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("URL", et_url.getText().toString());
               // webViewFragment.setArguments(bundle);
               // ((QDActivity)v.getContext()).getFragmentHelper().startFragment(webViewFragment);
                getFragmentHelper().build(getActivity(),WebViewFragment.class.getName())
                        .putExtras(bundle)
                        .putExtra("password", 666666)
                        .putExtra("name", "小三")
                        .navigation();
            }
        });
        ImageTextView btn_title = findViewById(R.id.it_actionbar_title);
        if (btn_title != null) {
            btn_title.setText(getTitle());
        }
        ImageTextView btn_back = findViewById(R.id.it_actionbar_back);
        if (btn_back != null) {
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickBack();
                }
            });
        }
        ImageTextView btn_close = findViewById(R.id.it_actionbar_close);
        if (btn_close != null) {
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
        ViewGroup ll_menu_right = findViewById(R.id.ll_menu_right);
        View view_splitor = findViewById(R.id.view_splitor);
        if (isRootFragment()) {
            if (btn_back != null) {
                btn_back.setVisibility(View.GONE);
            }
            if (btn_close != null) {
                btn_close.setVisibility(View.VISIBLE);
            }
            if (ll_menu_right != null) {
                ll_menu_right.setBackgroundResource(R.drawable.applet_menu_bg);
            }
            if (view_splitor != null) {
                view_splitor.setVisibility(View.VISIBLE);
            }
        } else {
            if (btn_close != null) {
                btn_close.setVisibility(View.GONE);
            }
            if (btn_back != null) {
                btn_back.setVisibility(View.VISIBLE);
            }
            if (ll_menu_right != null) {
                ll_menu_right.setBackground(null);
            }
            if (view_splitor != null) {
                view_splitor.setVisibility(View.GONE);
            }
        }
    }

    public void initView(View rootView) {
        int i = (int) (Math.random() * 10 % 4);
        setTitle(titles[i]);
        getActionBarTool().setHeaderBackgroundColor(colors[i]);
        Button button = rootView.findViewById(R.id.btn_open_new_fragment);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startFragment();
                }
            });
        }
        Button btn_set_title = rootView.findViewById(R.id.btn_set_title);
        if (btn_set_title != null) {
            btn_set_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = (int) (Math.random() * 10 % 4);
                    getActionBarTool().setTitle(titles[i] + "");
                }
            });
        }
    }

    private final String[] titles = {"1", "2", "3", "4"};
    private final int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
    private void startFragment() {
        getFragmentHelper().startFragment(new H5Fragment());
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