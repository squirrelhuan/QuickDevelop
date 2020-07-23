package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBar;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.widget.scroll.QDNestedFixedView;
import cn.demomaster.huan.quickdeveloplibrary.widget.scroll.QDNestedScrollParent;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "NestedScrollView", preViewClass = TextView.class, resType = ResType.Custome)
public class NestedScrollViewFragment extends QDFragment {

    //Components
    ViewGroup mView;
    int headHeight;
    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_nestedscrollview, null);
        }

        Bundle bundle = getArguments();
        String title = "空界面";
        // Example of a call to a native method

        final ImageView iv_head = mView.findViewById(R.id.iv_head);

        iv_head.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_head.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                headHeight = iv_head.getMeasuredHeight();
            }
        });

        //BaseService.baseBinder.getService();
        final QDNestedScrollParent qdNestedScrollParent = mView.findViewById(R.id.ns_p_01);
        //qdNestedScrollParent.setMinHeight(200);
        qdNestedScrollParent.getFixedView().setOnVisibleHeightChangeListener(new QDNestedFixedView.OnVisibleHeightChangeListener() {
            @Override
            public void onChange(final int dx, final int dy) {

                QDLogger.i( ",headHeight=" + headHeight);
                if (headHeight + dy <= qdNestedScrollParent.getFixedView().getMinHeight()) {
                    headHeight = qdNestedScrollParent.getFixedView().getMinHeight();
                } else if (headHeight + dy >= qdNestedScrollParent.getFixedView().getMaxHeight()) {
                    headHeight = qdNestedScrollParent.getFixedView().getMaxHeight();
                } else {
                    headHeight = headHeight + dy;
                }
                QDLogger.i( "可视区域改变 " + dx + "," + dy + ",headHeight=" + headHeight);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iv_head.getLayoutParams();
                layoutParams.height = headHeight;
                iv_head.setLayoutParams(layoutParams);
                int l = (int) (DisplayUtil.getScreenWidth(mContext) / 2 * (qdNestedScrollParent.getFixedView().getProgress()) - (qdNestedScrollParent.getFixedView().getMaxHeight() / 2 - iv_head.getWidth() / 2) * (1 - qdNestedScrollParent.getFixedView().getProgress()));
                layoutParams.leftMargin = (int) (l - iv_head.getWidth() / 2);
              /*  if (layoutParams.leftMargin > -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2) {
                    Log.i(TAG, layoutParams.leftMargin+">>>>"+(-qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2));
                } else {
                    Log.i(TAG, layoutParams.leftMargin+"<<<<"+(-qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2));
                }*/
                layoutParams.leftMargin = layoutParams.leftMargin > -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2 ? layoutParams.leftMargin : -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight / 2;
                QDLogger.i( "可视区域改变 " + dx + "," + dy + ",headHeight=" + headHeight + "layoutParams.leftMargin=" + layoutParams.leftMargin);


            }
        });
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBar actionBarLayout) {

    }

    @Override
    public void onStop() {
        super.onStop();
        //ServiceHelper.unbindFromService(mToken);
    }
}