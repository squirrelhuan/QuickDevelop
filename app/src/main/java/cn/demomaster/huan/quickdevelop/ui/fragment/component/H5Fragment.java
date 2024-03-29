package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.QDSharedPreferences;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.ui.fragment.WebViewFragment;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;
import cn.demomaster.qdrouter_library.view.ImageTextView;


/**
 * Squirrel桓
 * 2018/8/25
 */
@ActivityPager(name = "H5任意门", iconRes = R.drawable.ic_h5, resType = ResType.Resource)
public class H5Fragment extends QuickFragment {

    @Override
    public View getHeaderlayout() {
        return inflateView(R.layout.qd_applet_actionbar_common);
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
    String[] data =new String[]{};
    ArrayAdapter<String> adapter;
    ListView list_view;
    @Override
    public void initCreatView(View mView) {
        super.initCreatView(mView);
        list_view = findViewById(R.id.list_view);
        list_view.setOnItemClickListener((parent, view, position, id) -> jumpUrl(data[position]));

        initListData();
        EditText et_url = findViewById(R.id.et_url);
        Button btn_jump = findViewById(R.id.btn_jump);
        btn_jump.setOnClickListener(v -> {
           // WebViewFragment webViewFragment = new WebViewFragment();
            String url = et_url.getText().toString();
            String str = QDSharedPreferences.getInstance().getString("urls",null);
            Map<String,String> urlMap;

            Gson gson = new Gson();
            if(!TextUtils.isEmpty(str)){
                urlMap = gson.fromJson(str, Map.class);
            }else {
                urlMap = new LinkedHashMap<>();
            }
            urlMap.put(url,url);
            QDSharedPreferences.getInstance().putString("urls",gson.toJson(urlMap));
            initListData();
            jumpUrl(url);
        });

        Button btn_jump2 = findViewById(R.id.btn_jump2);
        btn_jump2.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent. setData( Uri. parse( "baekteori_maxida_android://ISPSuccess/https://testpg.payletter.com/PGSVC/MobileCard/ISPPay.asp?data=baekteori1,ed43e56572664adf80cbf1c46e138fe7,320,baekteori120210811180059,,20210811&vpresult=00 "));
            startActivity(intent);
        });
        ImageTextView btn_back = findViewById(R.id.it_actionbar_back);
        if (btn_back != null) {
            btn_back.setOnClickListener(v -> onClickBack());
        }
        ImageTextView btn_close = findViewById(R.id.it_actionbar_close);
        if (btn_close != null) {
            btn_close.setOnClickListener(v -> getActivity().finish());
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

    private void jumpUrl(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        // webViewFragment.setArguments(bundle);
        // ((QDActivity)v.getContext()).getFragmentHelper().startFragment(webViewFragment);
        getFragmentHelper().build(getActivity(), WebViewFragment.class.getName())
                .putExtras(bundle)
                .putExtra("password", 666666)
                .navigation();
    }

    private void initListData() {
        String str = QDSharedPreferences.getInstance().getString("urls",null);
        if(!TextUtils.isEmpty(str)){
            Gson gson = new Gson();
            Map<String,String> urlMap = gson.fromJson(str, Map.class);
            if(urlMap!=null){
                List<String> stringList = new ArrayList<>();
                for(Map.Entry entry:urlMap.entrySet()){
                    stringList.add((String) entry.getKey());
                }
                data = stringList.toArray(new String[0]);
            }
        }

        adapter = new ArrayAdapter<>(
                mContext, android.R.layout.simple_list_item_1, data);
        list_view.setAdapter(adapter);
    }

    public void initView(View rootView) {

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