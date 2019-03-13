package cn.demomaster.huan.quickdevelop.activity.sample.fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import cn.demomaster.huan.quickdevelop.R;
import cn.demomaster.huan.quickdevelop.fragment.component.RouterFragment;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

@ActivityPager(name = "BaseFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class BaseFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_base_fragment);
        View view = new FrameLayout(this);
        view.setId(getContentViewId());
        setContentView(view);
        FragmentActivityHelper.getInstance().bindActivity(this);
        RouterFragment f1 = null;
        f1 = new RouterFragment();
        FragmentActivityHelper.getInstance().replace(this,f1);
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

    public int getContentViewId(){
       return R.id.qd_fragment_content_view;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //FragmentActivityHelper.getInstance().unBindActivity(this);
    }
}
