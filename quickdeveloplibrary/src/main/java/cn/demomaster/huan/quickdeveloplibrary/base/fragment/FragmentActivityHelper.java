package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.BaseFragmentActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.QDBaseFragmentActivity;
import cn.demomaster.huan.quickdeveloplibrary.base.tool.actionbar.ActionBarLayoutInterface;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * @author squirrel桓
 * @date 2019/1/10.
 * description：
 */
public class FragmentActivityHelper {
    private static FragmentActivityHelper instance;

    public static FragmentActivityHelper getInstance() {
        if (instance == null) {
            instance = new FragmentActivityHelper();
        }
        return instance;
    }

    public FragmentActivityHelper() {
    }

    private void refreshFragment(AppCompatActivity activity) {
        List<Fragment> fragmentActivitys = getFragments(activity);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> fragmentAll = fragmentManager.getFragments();
        for (Fragment fragment : fragmentActivitys) {
            if (!fragmentAll.contains(fragment)) {
                removeFragmentFromMap(activity, fragment);
                refreshFragment(activity);
                return;
            }
        }
    }

    private boolean onBackFragment(AppCompatActivity activity) {
        if (activity != null && getFragments(activity) != null && getFragments(activity).size() > 0) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                QDLogger.d("FragmentHelper", "size=" + getFragments(activity).size());
                QDLogger.d("FragmentHelper", "BackStackEntryCount=" + fragmentManager.getBackStackEntryCount());

                refreshFragment(activity);
                Fragment fragment = getFragments(activity).get(getFragments(activity).size() - 1);
                if (fragment instanceof BaseFragmentActivityInterface) {
                    boolean ret = ((BaseFragmentActivityInterface) fragment).onBackPressed();
                    if (!ret) {
                        removeFragment(activity, fragment);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void removeFragmentFromMap(AppCompatActivity activity, Fragment fragment) {
        List<Fragment> fragmentList = getFragments(activity);
        fragmentList.remove(fragment);
        fragmentMap.put(activity, fragmentList);
    }
/*
    public void add(Fragment fragment) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = activities.get(activities.size() - 1).getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, fragment);
        transaction.show(fragment);
        //提交事务
        //transaction.commit();
        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
        transaction.commitAllowingStateLoss();
    }*/

    public void replace(AppCompatActivity activity, Fragment fragment) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.show(fragment);
        addFragmentToMap(activity, fragment);
        //提交事务
        //transaction.commit();
        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
        transaction.commitAllowingStateLoss();
    }

    public void bindActivity(WeakReference<FragmentActivity> activity) {
        if (getContentView(activity.get()) == null) {
            //view.setId(getContentView(activity));
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

    private void removeFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    private Fragment currentFragment;

    public void hideFragment(AppCompatActivity activity, Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(currentFragment);
        transaction.commit();
    }

    public void startFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(getListener(activity, fragment));
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            transaction.setCustomAnimations(R.anim.translate_from_right_to_left_enter, R.anim.translate_from_right_to_left_out, R.anim.translate_from_left_to_right_enter, R.anim.translate_from_left_to_right_out);
        }
        addFragmentToMap(activity, fragment);
        transaction.add(R.id.qd_fragment_content_view, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();

        if (currentFragment != null && currentFragment.isHidden()) {
            hideFragment(activity,currentFragment);
        }

        currentFragment = fragment;
    }

    private void addFragmentToMap(AppCompatActivity activity, Fragment fragment) {
        QDLogger.d("FragmentHelper", "添加fragment");
        if (getFragments(activity) == null) {
            List<Fragment> fragmentList = new ArrayList<>();
            fragmentList.add(fragment);
            fragmentMap.put(activity, fragmentList);
        } else {
            List<Fragment> fragmentList = getFragments(activity);
            fragmentList.add(fragment);
            fragmentMap.put(activity, fragmentList);
        }
    }

    private Map<AppCompatActivity, List<Fragment>> fragmentMap = new HashMap<>();

    public List<Fragment> getFragments(AppCompatActivity appCompatActivity) {
        return fragmentMap.get(appCompatActivity);
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

    /*public int getContentViewId() {
        return R.id.qd_fragment_content_view;
    }*/
    public static View getContentView(Activity ac) {
        ViewGroup view = (ViewGroup) ac.getWindow().getDecorView();
        FrameLayout content = (FrameLayout) view.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }

    public boolean onBackPressed(AppCompatActivity mContext) {
        return onBackFragment(mContext);
    }
}
