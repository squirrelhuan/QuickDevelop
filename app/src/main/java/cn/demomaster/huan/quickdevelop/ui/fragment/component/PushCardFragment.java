package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.view.adapter.SimpleRecycleViewAdapter;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.LoadingTengxuntvView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionStateType;
import cn.demomaster.huan.quickdeveloplibrary.widget.pushcardlayout.PushCardLayout;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * 音频播放view
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "PushCard", preViewClass = TextView.class, resType = ResType.Custome)
public class PushCardFragment extends QuickFragment {
    private RecyclerView recycler_body;
    private LinearLayoutManager linearLayoutManager;
    private SimpleRecycleViewAdapter adapter;
    private List<String> lists;

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_pushcard, null);
        return mView;
    }

    public void initView(View rootView) {
        recycler_body = rootView.findViewById(R.id.recycler_body);
        //模拟一些数据加载
        lists = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            lists.add(i + "item");
        }
        //这里使用线性布局像listview那样展示列表,第二个参数可以改为 HORIZONTAL实现水平展示
        linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        //使用网格布局展示
        recycler_body.setLayoutManager(new GridLayoutManager(mContext, 1));
        //recy_drag.setLayoutManager(linearLayoutManager);
        adapter = new SimpleRecycleViewAdapter(mContext, lists);
        //设置分割线使用的divider
        recycler_body.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recycler_body.setAdapter(adapter);

        //初始化
        final PushCardLayout pcl_layout = rootView.findViewById(R.id.pcl_layout);

        /*************************   自定义头部和底部布局   ************************************************/
        /*final Saleng textView = new Saleng(mContext);
        textView.setPaddingTop(pcl_layout.getfooterHeight());
        textView.setBackgroundColor(Color.BLACK);

        Saleng textView2 = new Saleng(mContext);
        textView2.setBackgroundResource(R.drawable.umeng_socialize_qzone);
        textView2.setBackgroundColor(Color.BLACK);

        //设置顶部布局view
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams1.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        pcl_layout.addHeaderView(textView,layoutParams1);*/

        //设置底部布局view

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        //pcl_layout.addFooterView(textView2,layoutParams);


        int w = (int) getResources().getDimension(R.dimen.dp_50);
        pcl_layout.addHeaderView(new LoadingTengxuntvView(mContext), layoutParams);
        pcl_layout.addFooterView(new LoadingTengxuntvView(mContext), layoutParams);

        //禁用滑动 pcl_layout.setCanRefresh(false);
        //设置数据监听器，可触发网络请求  数据加载完成请手动恢复 pcl_layout.setCancel();
        pcl_layout.setDataListener(new PushCardLayout.PushCardDatalistener() {
            @Override
            public void onLoadMoreData() {
               /* pcl_layout.removeCallbacks(downRunnable);
                pcl_layout.removeCallbacks(upRunnable);
                pcl_layout.postDelayed(upRunnable, 3000);*/
                //Toast.makeText(mContext, "加载更多。。。", Toast.LENGTH_SHORT).show();
                QdToast.show(mContext, "加载更多。。。");
            }

            @Override
            public void onRefreshData() {
                /*pcl_layout.removeCallbacks(downRunnable);
                pcl_layout.removeCallbacks(upRunnable);
                pcl_layout.postDelayed(downRunnable, 3000);*/
                //Toast.makeText(mContext, "刷新数据。。。", Toast.LENGTH_SHORT).show();
                QdToast.show(mContext, "刷新数据。。。");
            }
        });

        //设置动画监听器，可自定义动画
        pcl_layout.setAnimationListener((isUpper, value) -> {
            //QDLogger.i("Animation", "Animation onRuning:" + value);
            //(isUpper?textView:textView2). refreshAnimation();
            //isUpper 可判断是头部动画还是底部动画
        });
        //设置默认状态
        pcl_layout.setDefaultState(PushCardLayout.DefaultStateType.top);
    }

   /* public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle(titles[i]+"---------ASDFGGHHJ");
        actionBarLayoutOld.setHeaderBackgroundColor(colors[i]);
    }*/

    Runnable upRunnable = () -> {
        //qdActionDialog.dismiss();
        QDActionDialog qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionStateType.COMPLETE).setMessage("刷新成功").setDelayMillis(2000).create();
        qdActionDialog.show();
        //pcl_layout.dismiss();
    };

    Runnable downRunnable = () -> {
        final QDActionDialog qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionStateType.COMPLETE).setMessage("加载失败").setDelayMillis(2000).create();
        qdActionDialog.show();
    };

}