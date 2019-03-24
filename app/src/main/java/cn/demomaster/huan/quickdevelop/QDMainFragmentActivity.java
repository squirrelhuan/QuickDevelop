package cn.demomaster.huan.quickdevelop;

import android.graphics.Color;
import android.os.Bundle;

import cn.demomaster.huan.quickdevelop.fragment.main.MainFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseActivityParent;
import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.BaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.QDBaseFragment;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;

/**
 *
 */
public class QDMainFragmentActivity extends QDBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdmain);
        if (savedInstanceState == null) {
            QDBaseFragment fragment = new MainFragment();
            FragmentActivityHelper.getInstance().startFragment( mContext,fragment);
        }
        actionBarLayoutView.setActionbarType(ActionBarLayoutView.ACTIONBAR_TYPE.NORMAL);
        actionBarLayoutView.setHeaderBackgroundColor(Color.GRAY);
        //actionBarLayoutView.setActionbarType(ActionBarLayoutView.ACTIONBAR_TYPE.NORMAL);
        //getActionBarLayout().getLeftView().setVisibility(View.GONE);
    }


}
