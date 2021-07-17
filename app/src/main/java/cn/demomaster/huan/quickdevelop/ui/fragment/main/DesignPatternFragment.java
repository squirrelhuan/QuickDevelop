package cn.demomaster.huan.quickdevelop.ui.fragment.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.DesignListAdapter;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.util.QDFileUtil;
import cn.demomaster.huan.quickdeveloplibrary.view.decorator.GridDividerItemDecoration;


/**
 * Components视图
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(iconRes = R.mipmap.quickdevelop_ic_launcher)
public class DesignPatternFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private DesignListAdapter designListAdapter;

    @Override
    public boolean isUseActionBarLayout() {
        return false;//不带导航栏
    }

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_component, null);
        return view;
    }

    private String linksPath = "designHtmls.json";

    public void initView(View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        designListAdapter = new DesignListAdapter(getContext());
        String data_str = QDFileUtil.getFromAssets(getActivity(), linksPath);
        List<String> data = JSON.parseArray(data_str, String.class);
        designListAdapter.updateList(data);
        //设置Adapter
        recyclerView.setAdapter(designListAdapter);
        //设置分隔线
        //recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
        //设置行级分割线使用的divider
        //recyclerView.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getContext(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

        int spanCount = 3;
        //使用网格布局展示
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        //设置分隔线
        recyclerView.addItemDecoration(new GridDividerItemDecoration(getContext(), spanCount));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}