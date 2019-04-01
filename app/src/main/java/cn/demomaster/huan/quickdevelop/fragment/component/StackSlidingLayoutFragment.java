package cn.demomaster.huan.quickdevelop.fragment.component;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarInterface;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;


/**
 * Squirrel桓
 * 2018/8/25
 */

@ActivityPager(name = "StackSliding",preViewClass = StateView.class,resType = ResType.Custome)
public class StackSlidingLayoutFragment extends QDBaseFragment {


    //Components
    ViewGroup mView;

    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public ViewGroup getContentView(LayoutInflater inflater) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.fragment_layout_stack_sliding, null);
        }
        return mView;
    }

    @Override
    public void initView(View rootView, ActionBarInterface actionBarLayout) {
        actionBarLayout.setActionBarType(ActionBarInterface.ACTIONBAR_TYPE.ACTION_TRANSPARENT);
        actionBarLayout.setHeaderBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}