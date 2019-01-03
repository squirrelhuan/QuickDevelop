package cn.demomaster.huan.quickdevelop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.ComponentAdapter;
import cn.demomaster.huan.quickdeveloplibrary.helper.simplepicture.SimplePictureAdapter;


/**
 * Squirrel桓
 * 2018/8/25
 */
public class ComponentFragment extends Fragment {
    //Components
    View mView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_layout_component, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";
        initView(mView);
        return mView;
    }

    private RecyclerView recyclerView;
    private ComponentAdapter componentAdapter;

    private void initView(View mView) {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//设置布局管理器
        //使用网格布局展示
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        componentAdapter = new ComponentAdapter(getContext());
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            names.add("第几" + i + "个");
        }
        componentAdapter.updateList(names);
//设置Adapter
        recyclerView.setAdapter(componentAdapter);
        //设置分隔线
        //recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
        //设置分割线使用的divider
        recyclerView.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getContext(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

//设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}