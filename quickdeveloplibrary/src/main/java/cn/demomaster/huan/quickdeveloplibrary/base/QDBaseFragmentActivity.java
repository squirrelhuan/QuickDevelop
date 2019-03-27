package cn.demomaster.huan.quickdeveloplibrary.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutView;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

public class QDBaseFragmentActivity extends AppCompatActivity {
    public AppCompatActivity mContext;
    public ActionBarLayoutView actionBarLayoutView;
    private int headlayoutResID = R.layout.activity_action_bar_test;
    public int getHeadlayoutResID() {
        return headlayoutResID;
    }
    public ActionBarLayoutView getActionBarLayout(View view) {
        if (actionBarLayoutView == null) {
            ActionBarLayoutView.Builder builder = new ActionBarLayoutView.Builder(mContext).setContentView(view).setHeaderResId(getHeadlayoutResID());
            actionBarLayoutView = builder.creat();
        }
        return actionBarLayoutView;
    }

    public ActionBarLayoutView getActionBarLayoutView() {
        return actionBarLayoutView;
    }

    @Override
    public void setContentView(int layoutResID) {
        initQDContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        initQDContentView(view);
    }

    private void initQDContentView(View view){
        this.mContext = this;
        if (isUseActionBarLayout()) {//是否使用自定义导航栏
            StatusBarUtil.transparencyBar(mContext);
            actionBarLayoutView = getActionBarLayout(view);
            super.setContentView(actionBarLayoutView);
            FragmentActivityHelper.getInstance().bindActivity(this);
        } else {
            super.setContentView(view);
        }
    }

    public LayoutInflater mInflater;
    private void initQDContentView(int layoutResID){
        mInflater = LayoutInflater.from(this);
       View view = mInflater.inflate(layoutResID,null);
       initQDContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentActivityHelper.getInstance().startFragment(activity, fragment);
    }

    public int getContentViewId() {
        return android.R.id.content;//R.id.qd_fragment_content_view;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        FragmentActivityHelper.getInstance().onBackPressed(this);

    }


    /*
 是否实用自定义导航
  */
    public boolean isUseActionBarLayout() {
        return true;
    }


}
