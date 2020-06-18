package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.helper.toast.QdToast;
import cn.demomaster.huan.quickdeveloplibrary.util.QDLogger;

/**
 * @author squirrel桓
 * @date 2019/1/10.
 * description：
 */
public class FragmentHelper implements FragmentManager.OnBackStackChangedListener {

    public FragmentHelper(AppCompatActivity activity) {
        fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
    }

    public FragmentManager fragmentManager;

    public List<Fragment> getFragments() {
        return getFragmentManager().getFragments();
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public boolean onKeyDown(Context context, int keyCode, KeyEvent event) {
        QDLogger.d("FragmentHelper", "size=" + getFragments().size() + ",BackStackEntryCount=" + getFragmentManager().getBackStackEntryCount());
       // if (getFragmentManager().getBackStackEntryCount() > 0) {
            if (getFragments().size() >0) {
                Fragment fragment = getFragments().get(getFragments().size() - 1);
                if(fragment==null||fragment.getContext()!=context){
                    return false;
                }
                if (fragment instanceof QDFragmentInterface) {
                    boolean ret = ((QDFragmentInterface) fragment).onKeyDown(keyCode, event);
                    return ret;
                }
            }
        //}
        return false;
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
        //提交事务
        //transaction.commit();
        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
        transaction.commitAllowingStateLoss();
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

    private WeakReference<Fragment> currentFragment;
    public void hideFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(currentFragment.get());
        transaction.commit();
    }

    public void startFragment(AppCompatActivity activity, QDFragment fragment) {
        QDLogger.d(fragment.getClass()+" startFragment fragment");
        if(fragmentManager.getBackStackEntryCount()==0){
            fragment.setRootFragment(true);
            QDLogger.d(fragment.getClass()+" is root fragment");
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.add(R.id.qd_fragment_content_view, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
        if (currentFragment != null && currentFragment.get() != null && currentFragment.get().isHidden()) {
            hideFragment(activity, currentFragment.get());
        }
        currentFragment = new WeakReference(fragment);
    }
//PopBackStackImmediate


    /*public int getContentViewId() {
        return R.id.qd_fragment_content_view;
    }*/
    public static View getContentView(Activity ac) {
        ViewGroup view = (ViewGroup) ac.getWindow().getDecorView();
        FrameLayout content = (FrameLayout) view.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }

    public void onDestroy() {
        if (currentFragment != null) {
            currentFragment = null;
        }
        if (fragmentManager != null) {
            fragmentManager.removeOnBackStackChangedListener(this);
            fragmentManager = null;
        }
    }

    @Override
    public void onBackStackChanged() {
        /*FragmentManager manager = activity.getSupportFragmentManager();
        // android.app.FragmentManager manager1 = activity.getFragmentManager();
        if (manager != null) {
            Fragment currFrag = fragment;
            //currFrag = (Fragment) manager.findFragmentByTag(R.id.fl_home);
            currFrag.onResume();
        }*/
    }
}
