package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.VisiableLayoutAdapter;
import cn.demomaster.huan.quickdevelop.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.view.timeline.TimeLineView;
import cn.demomaster.huan.quickdeveloplibrary.widget.layout.VisibleLayout;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "Framelayout", preViewClass = TextView.class, resType = ResType.Custome)
public class FramelayoutFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_show)
    TextView btn_show;

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @NonNull
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_framelayout, null);
        return mView;
    }

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = rootView.findViewById(R.id.tv_content);
                if (view.getVisibility() == View.VISIBLE) {
                    rootView.findViewById(R.id.tv_content).setVisibility(View.GONE);
                    btn_show.setText("显示");
                } else {
                    rootView.findViewById(R.id.tv_content).setVisibility(View.VISIBLE);
                    btn_show.setText("隐藏");
                }
            }
        });

        VisibleLayout vl_layout = rootView.findViewById(R.id.vl_layout);

        RadioGroup rg_positon = rootView.findViewById(R.id.rg_positon);
        rg_positon.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_left:
                        vl_layout.setGravity(Gravity.LEFT);
                        break;
                    case R.id.btn_right:
                        vl_layout.setGravity(Gravity.RIGHT);
                        break;
                    case R.id.btn_top:
                        vl_layout.setGravity(Gravity.TOP);
                        break;
                    case R.id.btn_bottom:
                        vl_layout.setGravity(Gravity.BOTTOM);
                        break;
                }
            }
        });

        List data = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18");
        VisiableLayoutAdapter recycleAdapter = new VisiableLayoutAdapter(getContext(), data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        //添加动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分隔线
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //添加分割线
        //mRecyclerView.addItemDecoration(new RefreshItemDecoration(getContext(), RefreshItemDecoration.VERTICAL_LIST));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ((TimeLineView) rootView.findViewById(R.id.time_line)).setTargetViewById(recyclerView, R.id.tv_message);
    }

}