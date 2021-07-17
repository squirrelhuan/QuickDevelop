package cn.demomaster.huan.quickdevelop.ui.activity.sample.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import cn.demomaster.huan.quickdevelop.ui.fragment.component.AppletsFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.activity.QDActivity;
import cn.demomaster.qdlogger_library.QDLogger;
import cn.demomaster.qdrouter_library.actionbar.ACTIONBAR_TYPE;

@ActivityPager(name = "小程序",preViewClass = TextView.class,resType = ResType.Custome)
public class AppletsFragmentActivity extends QDActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = new FrameLayout(this);
        view.setId(View.generateViewId());
        setContentView(view);
        getActionBarTool().setActionBarType(ACTIONBAR_TYPE.NO_ACTION_BAR_NO_STATUS);
        QDLogger.i("AppletsFragmentActivity onCreate id="+view.getId()+",FragmentHelper="+getFragmentHelper().hashCode());
        /*AppletsFragment f1 = new AppletsFragment();
        startFragment(this,f1,getContentViewId());*/
       // startFragment(new AppletsFragment(),getContentViewId());

        getFragmentHelper().build(this,AppletsFragment.class.getName())
                .setContainerViewId(view.getId()).putExtras(new Bundle())
                .putExtra("password", 666666)
                .putExtra("name", "小三").navigation();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        QDLogger.println("keyCode="+keyCode);
        return super.onKeyDown(keyCode, event);
    }
}
