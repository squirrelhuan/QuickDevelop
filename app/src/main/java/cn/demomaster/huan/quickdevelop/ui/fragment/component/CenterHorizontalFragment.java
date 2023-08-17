package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.AppListAdapter;
import cn.demomaster.huan.quickdevelop.adapter.HorizontalAdapter;
import cn.demomaster.huan.quickdevelop.view.SlidingTabLayout;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.AutoCenterHorizontalScrollView;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;

@ActivityPager(name = "横向滚动居中",preViewClass = TextView.class,resType = ResType.Custome)
public class CenterHorizontalFragment extends QuickFragment {

    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_center_horizental, null);
        return mView;
    }

    @Override
    public String getTitle() {
        return "横向滚动居中";
    }

    @Override
    public void initView(View rootView) {
        initHorizontal();
    }

    //  SnapHelper snapHelperCenter = new LinearSnapHelper();
    private void initHorizontal() {
        AutoCenterHorizontalScrollView autoCenterHorizontalScrollView = findViewById(R.id.achs_test);
        //测试用的随机字符串集合
        List<String> names =new ArrayList<>();
        for(int i=0;i<50;i++){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(""+i);
            for(int j=0;j<i%4;j++){
                stringBuilder.append("A");
            }
            names.add(stringBuilder.toString());
        }
        //adapter去处理itemView
        HorizontalAdapter hadapter = new HorizontalAdapter(mContext,names);
        autoCenterHorizontalScrollView.setAdapter(hadapter);
        autoCenterHorizontalScrollView.setOnSelectChangeListener(position -> ((TextView) findViewById(R.id.tv_index)).setText("当前"+position));
        autoCenterHorizontalScrollView.setCurrentIndex(39);


        AutoCenterHorizontalScrollView achs_test2;
        achs_test2 = findViewById(R.id.achs_test2);
        //测试用的随机字符串集合
        List<String> names2 =new ArrayList<>();
        for(int i=0;i<50;i++){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(""+i);
            for(int j=0;j<i%4;j++){
                stringBuilder.append("A");
            }
            names2.add(stringBuilder.toString());
        }
        //adapter去处理itemView
        HorizontalAdapter hadapter2 = new HorizontalAdapter(mContext,names2);
        achs_test2.setAdapter(hadapter2);
        achs_test2.setOnSelectChangeListener(position -> ((TextView) findViewById(R.id.tv_index2)).setText("当前"+position));
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

    private void setUpRecyclerView() {
        //centerSnapRecyclerView = findViewById(R.id.centerSnapRecyclerView);
        LinearLayoutManager layoutManagerCenter
                = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        centerSnapRecyclerView.setLayoutManager(layoutManagerCenter);
        AppListAdapter appListCenterAdapter = new AppListAdapter(mContext);
        centerSnapRecyclerView.setAdapter(appListCenterAdapter);
        List<String> names =new ArrayList<>();
        for(int i=0;i<50;i++){
            names.add("第几"+i+"个");
        }
        appListCenterAdapter.updateList(names);
        SnapHelper snapHelperCenter = new LinearSnapHelper();
        snapHelperCenter.attachToRecyclerView(centerSnapRecyclerView);

    }
    ViewPager2 view_pager_tag;
    private void init() {
        //view_pager_tag = findViewById(R.id.view_pager_hor);
        view_pager_tag.setClipChildren(false); //VP的内容可以不在限制内绘制
        List<String> list = new ArrayList<>();
        for (int i=0;i<30;i++){
            list.add("第"+i+"个");
        }
        //getSupportFragmentManager()
        SampleFragmentAdapter adapter = new SampleFragmentAdapter(mContext, list);
        view_pager_tag.setAdapter(adapter);
        view_pager_tag.setOffscreenPageLimit(10); //缓存页面数
        view_pager_tag.setPadding(0,0,0,0);
        //view_pager_tag.setPageMargin(DisplayUtil.getScreenWidth(mContext) +DisplayUtil.dip2px(mContext,60)); //每页的间隔
        //view_pager_tag.setPadding(QMUIDisplayHelper.getScreenWidth(mContext)/2,0,0,0);
    }


    public static class SampleFragmentAdapter extends FragmentStateAdapter {
        private final List<String> data;

        public SampleFragmentAdapter(@NonNull FragmentActivity fragmentActivity,List<String> data) {
            super(fragmentActivity);
            this.data = data;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            fragment = new BlankFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
