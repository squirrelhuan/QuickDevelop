package cn.demomaster.huan.quickdeveloplibrary.base;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.view.View;

import java.lang.ref.WeakReference;

import cn.demomaster.huan.quickdeveloplibrary.annotation.ActivityPager;
import cn.demomaster.huan.quickdeveloplibrary.annotation.ResType;
import cn.demomaster.huan.quickdeveloplibrary.base.fragment.FragmentActivityHelper;
import cn.demomaster.huan.quickdeveloplibrary.view.loading.StateView;

@ActivityPager(name = "BaseFragment",preViewClass = StateView.class,resType = ResType.Custome)
public class BaseFragmentActivity extends AppCompatActivity {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        FragmentActivityHelper.getInstance().bindActivity(new WeakReference<FragmentActivity>(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startFragment(AppCompatActivity activity,Fragment fragment){
        FragmentActivityHelper.getInstance().startFragment(activity,fragment);
    }
    public int getContentViewId(){
       return android.R.id.content;//R.id.qd_fragment_content_view;
    }

}
