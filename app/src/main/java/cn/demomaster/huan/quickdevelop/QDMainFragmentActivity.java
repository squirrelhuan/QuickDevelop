package cn.demomaster.huan.quickdevelop;

import android.os.Bundle;

import cn.demomaster.huan.quickdevelop.fragment.main.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;

/**
 *
 */
public class QDMainFragmentActivity extends BaseActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);
        //setContentView(R.layout.activity_empty);
        //setContentView(-1);
        if (savedInstanceState == null) {
            BaseFragment fragment = new MainFragment();
            FragmentActivityHelper.getInstance().startFragment( mContext,fragment);
        }
        //getActionBarLayout().setHeaderBackgroundColor(Color.GRAY);
        getActionBarLayout().setActionBarModel(ActionBarLayout.ACTIONBAR_TYPE.NO_ACTION_BAR);
        //getActionBarLayout().getLeftView().setVisibility(View.GONE);
    }


}
