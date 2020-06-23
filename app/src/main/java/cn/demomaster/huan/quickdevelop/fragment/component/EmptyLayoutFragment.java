package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.adapter.VisiableLayoutAdapter;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.PopToastUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDMulSheetDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.layout.EmptyLayout;
import cn.demomaster.huan.quickdeveloplibrary.widget.layout.VisibleLayout;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "EmptyLayout", preViewClass = TextView.class, resType = ResType.Custome)
public class EmptyLayoutFragment extends QDFragment {

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //QDLogger.d("拦截Activity:"+getClass().getName() + "返回事件");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        ViewGroup mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_emptylayout, null);
        return mView;
    }

    EmptyLayout emptylayout;
    @Override
    public void initView(View rootView, ActionBar actionBarLayout) {
        ButterKnife.bind(this, rootView);

        getActionBarLayout().getRightView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMulMenuDialog();
            }
        });

        emptylayout = rootView.findViewById(R.id.emptylayout);
    }

    private void showMulMenuDialog() {
        String[] menus ={"单行提示","标题+描述","单行文字+重试按钮","标题+描述+重试按钮","加载动画","加载动画+加载提示","加载动画+加载提示+加载描述",};
        new QDMulSheetDialog.MenuBuilder(mContext).setData(menus).setOnDialogActionListener(new QDMulSheetDialog.OnDialogActionListener() {
            @Override
            public void onItemClick(QDMulSheetDialog dialog, int position, List<String> data) {
                dialog.dismiss();
                //PopToastUtil.ShowToast(mContext,data.get(position));
                switch (position){
                    case 0://单行提示
                        emptylayout.hideAll();
                        emptylayout.showTitle("单行提示");
                        break;
                    case 1:
                        emptylayout.hideAll();
                        emptylayout.showTitle("标题");
                        emptylayout.showMessage("单行提示");
                        break;
                    case 2:
                        emptylayout.hideAll();
                        emptylayout.showMessage("单行提示");
                        emptylayout.showRetry();
                        break;
                    case 3:
                        emptylayout.hideAll();
                        emptylayout.showTitle("标题");
                        emptylayout.showMessage("单行提示");
                        emptylayout.showRetry();
                        break;
                    case 4:
                        emptylayout.hideAll();
                        emptylayout.showLodding();
                        break;
                    case 5:
                        emptylayout.hideAll();
                        emptylayout.showLodding();
                        emptylayout.showTitle("加载中");
                        break;
                    case 6:
                        emptylayout.hideAll();
                        emptylayout.showLodding();
                        emptylayout.showTitle("加载中");
                        emptylayout.showMessage("请耐心等待");
                        emptylayout.showRetry();
                        break;
                }
            }
        }).create().show();
    }

}