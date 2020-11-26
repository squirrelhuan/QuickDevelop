package cn.demomaster.huan.quickdevelop.activity.sample.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import cn.demomaster.huan.quickdevelop.fragment.component.RouterFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ACTIONBAR_TYPE;
import cn.demomaster.qdlogger_library.QDLogger;

@ActivityPager(name = "BaseFragment",preViewClass = TextView.class,resType = ResType.Custome)
public class BaseFragmentActivity extends QDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*View view = new FrameLayout(this);
        view.setId(getContentViewId());*/
        setContentView(null);
        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        QDLogger.i("BaseFragmentActivity onCreate");
        RouterFragment f1 = new RouterFragment();
        startFragment(f1,getContentViewId());
        //开启事务，fragment的控制是由事务来实现的
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
        if(f1 == null){
            f1 = new RouterFragment();
            transaction.add(getContentViewId(), f1);
        }
        //隐藏所有fragment
        //hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f1);*/
        //第二种方式(replace)，初始化fragment
//        if(f1 == null){
//            f1 = new MyFragment("消息");
//        }
//        transaction.replace(R.id.main_frame_layout, f1);

        /*//提交事务
        transaction.commit();*/

    }
/*
    public int getContentViewId(){
       return R.id.qd_fragment_content_view;
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        //FragmentActivityHelper.getInstance().unBindActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.i("BaseFragmentActivity onKeyDown"+keyCode);
        return super.onKeyDown(keyCode, event);
    }
}
