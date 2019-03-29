package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.RecycleViewAdapter;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout2;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDActionDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.pushcardlayout.PushCardLayout;
import cn.demomaster.huan.quickdeveloplibrary.widget.pushcardlayout.Saleng;


/**
 * 音频播放view
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "PushCard",preViewClass = StateView.class,resType = ResType.Custome)
public class PushCardFragment extends QDBaseFragment {
    //Components
    ViewGroup mView;
    private RecyclerView recycler_body;
    private LinearLayoutManager linearLayoutManager;
    private RecycleViewAdapter adapter;
    private List<String> lists;

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_pushcard, null);
        }
        Bundle bundle = getArguments();
        String title = "空界面";
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayoutOld) {
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
        adapter = new RecycleViewAdapter(mContext, lists);
        //设置分割线使用的divider
        recycler_body.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recycler_body.setAdapter(adapter);

        //初始化
        final PushCardLayout pcl_layout = rootView.findViewById(R.id.pcl_layout);

        /*************************   自定义头部和底部布局   ************************************************/
        Saleng textView = new Saleng(mContext);
        textView.setBackgroundColor(Color.BLACK);
        textView.setPaddingTop(pcl_layout.getBottomLayoutHeight());

        Saleng textView2 = new Saleng(mContext);
        textView2.setBackgroundColor(Color.BLACK);

        //设置顶部布局view
        pcl_layout.setTopLayoutView(textView);
        //设置底部布局view
        pcl_layout.setBottomLayoutView(textView2);

        //禁用滑动 pcl_layout.setCanRefresh(false);

        /*************************   设置数据监听器，可触发网络请求  数据加载完成请手动恢复 pcl_layout.setCancel();  ********************************/
        pcl_layout.setDataListener(new PushCardLayout.PushCardDatalistener() {
            @Override
            public void onLoadMoreData() {
                final QDActionDialog qdActionDialog1 = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionDialog.StateType.LOADING).setMessage("加载中").setDelayMillis(-1).create();
                //qdActionDialog1.show();
                pcl_layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final QDActionDialog qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionDialog.StateType.COMPLETE).setMessage("加载失败").setDelayMillis(2000).create();
                        qdActionDialog.show();
                        pcl_layout.dismiss();
                        qdActionDialog1.dismiss();
                    }
                }, 3000);
                Toast.makeText(mContext, "加载更多。。。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRefreshData() {
                final QDActionDialog qdActionDialog1 = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionDialog.StateType.LOADING).setMessage("加载中").setDelayMillis(-1).create();
                //qdActionDialog1.show();
                pcl_layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //qdActionDialog.dismiss();
                        final QDActionDialog qdActionDialog = new QDActionDialog.Builder(mContext).setContentbackgroundColor(mContext.getResources().getColor(R.color.transparent_dark_cc)).setBackgroundRadius(50).setStateType(QDActionDialog.StateType.COMPLETE).setMessage("刷新成功").setDelayMillis(2000).create();
                        qdActionDialog.show();
                        pcl_layout.dismiss();
                        qdActionDialog1.dismiss();
                    }
                }, 3000);
                Toast.makeText(mContext, "刷新数据。。。", Toast.LENGTH_SHORT).show();
            }
        });

        /*************************   设置动画监听器，可自定义动画   ************************************************/
        pcl_layout.setAnimationListener(new PushCardLayout.PushCardAnimationListener() {
            @Override
            public void onStart(View targetView) {
                QDLogger.i("Animation", "Animation Start ...");
            }

            @Override
            public void onRuning(View targetView,boolean isUpper, final float value) {
                QDLogger.i("Animation", "Animation onRuning:" + value);
                ((Saleng) targetView).setPercent(value );
                //isUpper 可判断是头部动画还是底部动画
            }

            @Override
            public void onEnd(View targetView) {
                QDLogger.i("Animation", "Animation End ...");
                ((Saleng) targetView). refreshAnimation();
            }
        });
        //设置默认状态
        pcl_layout.setDefaultState(PushCardLayout.DefaultStateType.top);
    }

    private String[] titles = {"1", "2", "3", "4"};
    private int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};

    public void initActionBarLayout(ActionBarLayout2 actionBarLayoutOld) {
        int i = (int) (Math.random() * 10 % 4);
        actionBarLayoutOld.setTitle(titles[i]+"---------ASDFGGHHJ");
        actionBarLayoutOld.setHeaderBackgroundColor(colors[i]);
    }

}