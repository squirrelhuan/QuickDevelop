package cn.demomaster.huan.quickdevelop.fragment.main;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.DesignListAdapter;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.util.FileUtil;
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
    public ViewGroup getContentView(LayoutInflater inflater) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_layout_component, null);
    }

    private String linksPath = "designHtmls.json";

    @Override
    public void initView(View rootView, ActionBarLayout actionBarLayout) {
        actionBarLayout.setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        actionBarLayout.getLeftView().setVisibility(View.GONE);
        actionBarLayout.setBackGroundColor(Color.RED);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        designListAdapter = new DesignListAdapter(getContext());
        String data_str = FileUtil.getFromAssets(mContext, linksPath);
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