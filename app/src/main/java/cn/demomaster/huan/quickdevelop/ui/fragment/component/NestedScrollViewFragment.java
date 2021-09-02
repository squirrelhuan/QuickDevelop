package cn.demomaster.huan.quickdevelop.ui.fragment.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.ui.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.util.DisplayUtil;
import cn.demomaster.huan.quickdeveloplibrary.widget.scroll.QDNestedScrollParent;
import cn.demomaster.qdlogger_library.QDLogger;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "NestedScrollView", preViewClass = TextView.class, resType = ResType.Custome)
public class NestedScrollViewFragment extends BaseFragment {

    int headHeight;

    @Nullable
    @Override
    public View onGenerateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_layout_nestedscrollview, null);
        return mView;
    }

    @Override
    public void initView(View rootView) {

        final ImageView iv_head = rootView.findViewById(R.id.iv_head);

        iv_head.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_head.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                headHeight = iv_head.getMeasuredHeight();
            }
        });

        //BaseService.baseBinder.getService();
        final QDNestedScrollParent qdNestedScrollParent = rootView.findViewById(R.id.ns_p_01);
        //qdNestedScrollParent.setMinHeight(200);
        qdNestedScrollParent.getFixedView().setOnVisibleHeightChangeListener((dx, dy) -> {
            QDLogger.println(",headHeight=" + headHeight);
            if (headHeight + dy <= qdNestedScrollParent.getFixedView().getMinHeight()) {
                headHeight = qdNestedScrollParent.getFixedView().getMinHeight();
            } else {
                headHeight = Math.min(headHeight + dy, qdNestedScrollParent.getFixedView().getMaxHeight());
            }
            QDLogger.println("可视区域改变 " + dx + "," + dy + ",headHeight=" + headHeight);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iv_head.getLayoutParams();
            layoutParams.height = headHeight;
            iv_head.setLayoutParams(layoutParams);
            int l = (int) (DisplayUtil.getScreenWidth(mContext) / 2 * (qdNestedScrollParent.getFixedView().getProgress()) - (qdNestedScrollParent.getFixedView().getMaxHeight() / 2 - iv_head.getWidth() / 2) * (1 - qdNestedScrollParent.getFixedView().getProgress()));
            layoutParams.leftMargin = l - iv_head.getWidth() / 2;
          /*  if (layoutParams.leftMargin > -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2) {
                Log.i(TAG, layoutParams.leftMargin+">>>>"+(-qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2));
            } else {
                Log.i(TAG, layoutParams.leftMargin+"<<<<"+(-qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight/2));
            }*/
            layoutParams.leftMargin = layoutParams.leftMargin > -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight / 2 ? layoutParams.leftMargin : -qdNestedScrollParent.getFixedView().getMaxHeight() / 2 + headHeight / 2;
            QDLogger.println("可视区域改变 " + dx + "," + dy + ",headHeight=" + headHeight + "layoutParams.leftMargin=" + layoutParams.leftMargin);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        //ServiceHelper.unbindFromService(mToken);
    }
}