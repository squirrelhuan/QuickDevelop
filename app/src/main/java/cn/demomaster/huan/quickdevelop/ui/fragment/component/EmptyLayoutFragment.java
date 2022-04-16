package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.widget.dialog.QDMulSheetDialog;
import cn.demomaster.huan.quickdeveloplibrary.widget.layout.EmptyLayout;
import cn.demomaster.qdrouter_library.base.fragment.QuickFragment;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "加载布局", preViewClass = TextView.class, resType = ResType.Custome)
public class EmptyLayoutFragment extends QuickFragment {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //QDLogger.d("拦截Activity:"+getClass().getName() + "返回事件");
        return super.onKeyDown(keyCode, event);
    }

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_emptylayout, null);
        return mView;
    }

    EmptyLayout emptylayout;

    public void initView(View rootView) {
        ButterKnife.bind(this, rootView);
        getActionBarTool().getRightView().setOnClickListener(v -> showMulMenuDialog());
        emptylayout = rootView.findViewById(R.id.emptylayout);
        emptylayout.hideAll();
        emptylayout.showTitle("标题");
        emptylayout.showMessage("单行提示");
    }

    private void showMulMenuDialog() {
        String[] menus = {"单行提示", "标题+描述", "单行文字+重试按钮", "标题+描述+重试按钮", "加载动画", "加载动画+加载提示", "加载动画+加载提示+加载描述",};
        new QDMulSheetDialog.MenuBuilder(mContext).setData(menus).setOnDialogActionListener((dialog, position, data) -> {
            dialog.dismiss();
            //PopToastUtil.ShowToast(mContext,data.get(position));
            switch (position) {
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
        }).create().show();
    }
}