package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutInterface;

/**
 * @author squirrel桓
 * @date 2019/1/10.
 * description：
 */
public class FragmentActivityHelper {
    private static FragmentActivityHelper instance;
    private ActionBarLayoutInterface actionBarLayoutInterface;

    public static FragmentActivityHelper getInstance() {
        if (instance == null) {
            instance = new FragmentActivityHelper();
        }
        return instance;
    }

    public FragmentActivityHelper() {
        actionBarLayoutInterface = new ActionBarLayoutInterface() {
            @Override
            public void onBack() {
                remove();
            }
        };

    }

    private List<AppCompatActivity> activities = new ArrayList<>();

    public void add(Fragment fragment) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = activities.get(activities.size() - 1).getSupportFragmentManager().beginTransaction();
        transaction.add(getContentViewId(), fragment);
        transaction.show(fragment);
        //提交事务
        transaction.commit();
    }

    public void replace(Fragment fragment) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = activities.get(activities.size() - 1).getSupportFragmentManager().beginTransaction();
        transaction.replace(getContentViewId(), fragment);
        transaction.show(fragment);
        //提交事务
        transaction.commit();
    }

    public void bindActivity(AppCompatActivity activity) {
        View view = new FrameLayout(activity);
        view.setId(getContentViewId());
        activity.setContentView(view);
        activities.add(activity);
    }

    public void unBindActivity(AppCompatActivity activity) {
        if (activities == null) {
            return;
        }
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public void remove() {
        if (activities != null && activities.size() > 0) {
            FragmentManager fragmentManager = activities.get(activities.size() - 1).getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() == 0) {
                AppCompatActivity activity = activities.get(activities.size() - 1);
                activity.finish();
                unBindActivity(activity);
            }else {
                fragmentManager.popBackStack();
            }
        }
    }
    public void startFragment(Fragment fragment){
        if (activities != null && activities.size() > 0) {
            FragmentManager fragmentManager = activities.get(activities.size() - 1).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.translate_from_right_to_left_enter, R.anim.translate_from_right_to_left_out, R.anim.translate_from_left_to_right_enter, R.anim.translate_from_left_to_right_out)
                    .replace(getContentViewId(), fragment)
                    .addToBackStack("A")
                    .commit();
        }
    }

    public ActionBarLayoutInterface getActionBarLayoutInterface() {
        return actionBarLayoutInterface;
    }

    public int getContentViewId() {
        return R.id.qd_fragment_content_view;
    }
}
