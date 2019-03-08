package cn.demomaster.huan.quickdevelop.activity.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.AppListAdapter;
import cn.demomaster.huan.quickdevelop.adapter.HorizontalAdapter;
import cn.demomaster.huan.quickdevelop.fragment.BlankFragment;
import cn.demomaster.huan.quickdevelop.view.SlidingTabLayout;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.AutoCenterHorizontalScrollView;
import cn.demomaster.huan.quickdeveloplibrary.widget.button.ToggleButton;

@ActivityPager(name = "CenterHorizontal",preViewClass = ToggleButton.class,resType = ResType.Custome)
public class CenterHorizontalActivity extends BaseActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_horizental);

        //init();
        initHorizontal();
        //setUpRecyclerView();
        //initSlidingView();
    }

    //  SnapHelper snapHelperCenter = new LinearSnapHelper();

    private void initHorizontal() {
        AutoCenterHorizontalScrollView autoCenterHorizontalScrollView;
        autoCenterHorizontalScrollView = findViewById(R.id.achs_test);
        //测试用的随机字符串集合
        List<String> names =new ArrayList<>();
        for(int i=0;i<50;i++){
            String a = ""+i;
            for(int j=0;j<i%4;j++){
                a=a+"A";
            }
            names.add(a);
        }
        //adapter去处理itemView
        HorizontalAdapter hadapter = new HorizontalAdapter(mContext,names);
        autoCenterHorizontalScrollView.setAdapter(hadapter);
        autoCenterHorizontalScrollView.setOnSelectChangeListener(new AutoCenterHorizontalScrollView.OnSelectChangeListener() {
            @Override
            public void onSelectChange(int position) {
                ((TextView) findViewById(R.id.tv_index)).setText("当前"+position);
            }
        });
        autoCenterHorizontalScrollView.setCurrentIndex(39);


        AutoCenterHorizontalScrollView achs_test2;
        achs_test2 = findViewById(R.id.achs_test2);
        //测试用的随机字符串集合
        List<String> names2 =new ArrayList<>();
        for(int i=0;i<50;i++){
            String a = ""+i;
            for(int j=0;j<i%4;j++){
                a=a+"A";
            }
            names2.add(a);
        }
        //adapter去处理itemView
        HorizontalAdapter hadapter2 = new HorizontalAdapter(mContext,names2);
        achs_test2.setAdapter(hadapter2);
        achs_test2.setOnSelectChangeListener(new AutoCenterHorizontalScrollView.OnSelectChangeListener() {
            @Override
            public void onSelectChange(int position) {
                ((TextView) findViewById(R.id.tv_index2)).setText("当前"+position);
            }
        });
        achs_test2.setCurrentIndex(16);
    }

    private SlidingTabLayout id_sliding_view;
    private void initSlidingView() {
        //id_sliding_view =  findViewById(R.id.id_sliding_view);
        //id_sliding_view.setViewPager(null);
      /*  SlidingTabView slidingTabView = new SlidingTabView(mContext);
        for(int i=0;i<50;i++){
           TextView textView = new TextView(mContext);
            textView.setText("h"+i);
            slidingTabView.addView(textView);
        }
        id_sliding_view.addView(slidingTabView);*/
    }

    public RecyclerView centerSnapRecyclerView;
    private AppListAdapter appListCenterAdapter;
    private void setUpRecyclerView() {

        //centerSnapRecyclerView = findViewById(R.id.centerSnapRecyclerView);

        LinearLayoutManager layoutManagerCenter
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        centerSnapRecyclerView.setLayoutManager(layoutManagerCenter);
        appListCenterAdapter = new AppListAdapter(this);
        centerSnapRecyclerView.setAdapter(appListCenterAdapter);
        List<String> names =new ArrayList<>();
        for(int i=0;i<50;i++){
            names.add("第几"+i+"个");
        }
        appListCenterAdapter.updateList(names);
        SnapHelper snapHelperCenter = new LinearSnapHelper();
        snapHelperCenter.attachToRecyclerView(centerSnapRecyclerView);

    }
    ViewPager view_pager_tag;
    private SampleFragmentAdapter adapter;
    private void init() {
        //view_pager_tag = findViewById(R.id.view_pager_hor);
        view_pager_tag.setClipChildren(false); //VP的内容可以不在限制内绘制
        List<String> list = new ArrayList<>();
        for (int i=0;i<30;i++){
            list.add("第"+i+"个");
        }
        adapter = new SampleFragmentAdapter(getSupportFragmentManager(),list);
        view_pager_tag.setAdapter(adapter);
        view_pager_tag.setOffscreenPageLimit(10); //缓存页面数
        view_pager_tag.setPageMargin(-QMUIDisplayHelper.getScreenWidth(mContext) +DisplayUtil.dip2px(mContext,60)); //每页的间隔
        //view_pager_tag.setPadding(QMUIDisplayHelper.getScreenWidth(mContext)/2,0,0,0);
    }

    private class SampleFragmentAdapter extends FragmentPagerAdapter {
        private List<String> data;
        public SampleFragmentAdapter(FragmentManager fm) {
            super(fm);
        }
        public SampleFragmentAdapter(FragmentManager fm,List<String> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            fragment = new BlankFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return data.size();//
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
