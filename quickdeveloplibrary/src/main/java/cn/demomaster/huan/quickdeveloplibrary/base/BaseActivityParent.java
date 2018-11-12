package cn.demomaster.huan.quickdeveloplibrary.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.demomaster.huan.quickdeveloplibrary.ApplicationParent;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarHelper;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayout;
import cn.demomaster.huan.quickdeveloplibrary.util.StatusBarUtil;

public class BaseActivityParent extends AppCompatActivity {

    public Activity mContext;
    public Bundle mBundle = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        mBundle = getIntent().getExtras();
        ((ApplicationParent) getApplicationContext()).addActivity(this);
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(mContext);
    }

    //private LayoutInflater inflater;
    public ActionBarLayout activityLayout;
    @Override
    public void setContentView(int layoutResID) {
        mContext = this;
        activityLayout = ActionBarHelper.init(this,layoutResID);
        View view = activityLayout.getFinalView();
        super.setContentView(view);
    }

    public void onCreateView(@Nullable Bundle savedInstanceState){

    }

    public View getContentView() {
        return this.findViewById(android.R.id.content);
    }

    /**
     * 设置导航栏透明
     */
    public void transparentBar() {
        StatusBarUtil.transparencyBar(this);
    }

    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) intent.putExtras(bundle);
        startActivity(intent);
    }


}
