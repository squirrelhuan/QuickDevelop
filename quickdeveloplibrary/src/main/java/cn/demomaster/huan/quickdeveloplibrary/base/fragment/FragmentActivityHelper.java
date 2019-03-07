package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseFragmentActivity;
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

    private List<FragmentActivity> activities = new ArrayList<>();

    public void add(Fragment fragment) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = activities.get(activities.size() - 1).getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, fragment);
        transaction.show(fragment);
        //提交事务
        //transaction.commit();
        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
        transaction.commitAllowingStateLoss();
    }

    public void replace(Fragment fragment) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = activities.get(activities.size() - 1).getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.show(fragment);
        //提交事务
        //transaction.commit();
        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
        transaction.commitAllowingStateLoss();
    }

    FragmentActivity activity;

    public void bindActivity(FragmentActivity activity) {
        this.activity = activity;
        if (getContentView(activity) == null) {
            View view = new FrameLayout(activity);
            activity.setContentView(view);

            //view.setId(getContentView(activity));
        }

        activities.add(activity);
    }

    public void unBindActivity(FragmentActivity activity) {
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
            if (fragmentManager.getBackStackEntryCount() <= 1) {
                FragmentActivity activity = activities.get(activities.size() - 1);
                activity.finish();
                unBindActivity(activity);
            } else {
                fragmentManager.popBackStack();
            }
        }
    }
  /*  public void startFragment(Fragment fragment){
        if (activities != null && activities.size() > 0) {
            FragmentManager fragmentManager = activities.get(activities.size() - 1).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.translate_from_right_to_left_enter, R.anim.translate_from_right_to_left_out, R.anim.translate_from_left_to_right_enter, R.anim.translate_from_left_to_right_out)
                    .replace(R.id.qd_fragment_content_view, fragment)//android.R.id.content
                    .addToBackStack("A")
                   .commitAllowingStateLoss();//.commit();会报错，commitAllowingStateLoss不会报错，activity状态可能会丢失
        }
    }*/

    public void startFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(getListener(activity, fragment));
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            transaction.setCustomAnimations(R.anim.translate_from_right_to_left_enter, R.anim.translate_from_right_to_left_out, R.anim.translate_from_left_to_right_enter, R.anim.translate_from_left_to_right_out);
        }
        transaction.add(R.id.qd_fragment_content_view, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    private FragmentManager.OnBackStackChangedListener getListener(final AppCompatActivity activity, final Fragment fragment) {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = activity.getSupportFragmentManager();
                if (manager != null) {
                    Fragment currFrag = fragment;
                    //currFrag = (Fragment) manager.findFragmentByTag(R.id.fl_home);
                    currFrag.onResume();
                }
            }
        };
        return result;
    }

    public ActionBarLayoutInterface getActionBarLayoutInterface() {
        return actionBarLayoutInterface;
    }

    /*public int getContentViewId() {
        return R.id.qd_fragment_content_view;
    }*/
    public static View getContentView(Activity ac) {
        ViewGroup view = (ViewGroup) ac.getWindow().getDecorView();
        FrameLayout content = (FrameLayout) view.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }

    public void onBackPressed(BaseFragmentActivity baseFragmentActivity) {
        //contains
        if (activities.contains(baseFragmentActivity)) {
            remove();
            //FragmentManager fragmentManager = activities.get(activities.size() - 1).getSupportFragmentManager();
            //fragmentManager.get
            //baseFragmentActivity.onBackPressed();
        }
    }
}
