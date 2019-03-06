package cn.demomaster.huan.quickdevelop.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.service.MessageService;
import cn.demomaster.huan.quickdevelop.service.SimpleService;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.jni.BaseService;
import cn.demomaster.huan.quickdeveloplibrary.jni.JNITest;
import cn.demomaster.huan.quickdeveloplibrary.jni.ServiceHelper;
import cn.demomaster.huan.quickdeveloplibrary.jni.ServiceToken;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.util.QMUIDisplayHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;
import cn.demomaster.huan.quickdeveloplibrary.widget.scroll.QDNestedFixedView;
import cn.demomaster.huan.quickdeveloplibrary.widget.scroll.QDNestedScrollParent;

import static cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityRoot.TAG;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "NestedScrollView", preViewClass = StateView.class, resType = ResType.Custome)
public class NestedScrollViewFragment extends BaseFragment {


    //Components
    ViewGroup mView;
    int headHeight;

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

                Log.i(TAG, ",headHeight=" + headHeight);
                if (headHeight + dy <= qdNestedScrollParent.getFixedView().getMinHeight()) {
                    headHeight = qdNestedScrollParent.getFixedView().getMinHeight();
                } else if (headHeight + dy >= qdNestedScrollParent.getFixedView().getMaxHeight()) {
                    headHeight = qdNestedScrollParent.getFixedView().getMaxHeight();
                } else {
                    headHeight = headHeight + dy;
                }
                Log.i(TAG, "可视区域改变 " + dx + "," + dy + ",headHeight=" + headHeight);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iv_head.getLayoutParams();
                layoutParams.height = headHeight;
                iv_head.setLayoutParams(layoutParams);
                int l = (int) (QMUIDisplayHelper.getScreenWidth(mContext) / 2 * (qdNestedScrollParent.getFixedView().getProgress()) - (qdNestedScrollParent.getFixedView().getMaxHeight() / 2 - iv_head.getWidth() / 2) * (1 - qdNestedScrollParent.getFixedView().getProgress()));
                layoutParams.leftMargin = (int) (l - iv_head.getWidth() / 2);
              /*  if (layoutParams.leftMargin > -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2) {
                    Log.i(TAG, layoutParams.leftMargin+">>>>"+(-qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2));
                } else {
                    Log.i(TAG, layoutParams.leftMargin+"<<<<"+(-qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2));
                }*/
                layoutParams.leftMargin = layoutParams.leftMargin > -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2 ? layoutParams.leftMargin : -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight / 2;
                Log.i(TAG, "可视区域改变 " + dx + "," + dy + ",headHeight=" + headHeight + "layoutParams.leftMargin=" + layoutParams.leftMargin);


            }
        });
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarLayout actionBarLayout) {

    }

    @Override
    public void onStop() {
        super.onStop();
        //ServiceHelper.unbindFromService(mToken);
    }
}