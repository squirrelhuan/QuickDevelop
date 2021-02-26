package cn.demomaster.huan.quickdeveloplibrary.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import cn.demomaster.huan.quickdeveloplibrary.R;
import cn.demomaster.huan.quickdeveloplibrary.base.OnReleaseListener;
import cn.demomaster.qdlogger_library.QDLogger;

/**
 * @author squirrel桓
 * @date 2019/1/10.
 * description：
 */
public class FragmentHelper implements FragmentManager.OnBackStackChangedListener, OnReleaseListener, NavigationInterface {
    AppCompatActivity activity;
    public FragmentManager fragmentManager;

    public FragmentHelper(AppCompatActivity activity) {
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        fragments = new ArrayList<>();
    }

    /**
     * 获取fragment列表，注意过滤掉fragment中嵌套的fragment。
     * @return
     */
    public List<Fragment> getFragments() {
        List<Fragment> fragmentList =new ArrayList<>();
        List<Fragment> fragmentList2 = getFragmentManager().getFragments();
        for (Fragment fragment:fragmentList2){
            if(fragment instanceof QDFragment){
                fragmentList.add(fragment);
            }
        }
        return fragmentList;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public boolean onKeyDown(Context context, int keyCode, KeyEvent event) {
        //QDLogger.d("FragmentHelper", "size=" + getFragments().size() + ",BackStackEntryCount=" + getFragmentManager().getBackStackEntryCount());
        if (getFragments().size() > 0) {
            Fragment fragment = getFragments().get(getFragments().size() - 1);
            if (fragment == null) {
                return false;
            }
            if (fragment instanceof ViewLifecycle) {
                boolean ret = ((ViewLifecycle) fragment).onKeyDown(keyCode, event);
                return ret;
            }
        }
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

    /**
     * 获取当前激活状态的fragment，即最顶层的fragment活动页面
     * @return
     */
    public Fragment getCurrentFragment() {
        List<Fragment> fragmentList = getFragments();
        if (fragmentList == null || fragmentList.size() < 1) {
            return null;
        } else {
            return fragmentList.get(fragmentList.size() - 1);
        }
    }

    public void hideFragment(Fragment fragment) {
        QDLogger.d("hideFragment =" + fragment);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    int containerViewId;

    public void startFragment(QDFragment fragment) {
        startFragment(fragment, containerViewId);
    }

    /* int animation1 = R.anim.slide_in_right;
     int animation2 = R.anim.slide_out_left;
     int animation3 = R.anim.slide_in_left;
     int animation4 = R.anim.slide_out_right;*/
    int animation1 = R.anim.h5_slide_in_right;
    int animation2 = R.anim.h5_slide_out_left;
    int animation3 = R.anim.h5_slide_in_left;
    int animation4 = R.anim.h5_slide_out_right;
    List<Fragment> fragments;

    public void startFragment(QDFragment fragment, int containerViewId) {
        fragment.setFragmentHelper(this);
        if (fragmentManager.getBackStackEntryCount() == 0) {
            fragment.setRootFragment(true);
            QDLogger.d("fragment",fragment.getClass().getSimpleName() + " is root fragment");
        }

        this.containerViewId = containerViewId;
        addFragment(fragment, containerViewId);
    }

    public void addFragment(QDFragment fragment, int containerViewId) {
        QDFragment currentFragment = (QDFragment) getCurrentFragment();
        QDLogger.d("fragment"," addFragment " + fragment.getClass().getSimpleName() + ",containerViewId=" + containerViewId + ",getFragments()=" + getFragments().size());

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            transaction.setCustomAnimations(animation1, animation2,
                    animation3, animation4);
        }
        transaction.add(containerViewId, fragment, getFragmentTag(fragment))//R.id.qd_fragment_content_view
                .addToBackStack(getFragmentTag(fragment));
        if (currentFragment != null) {//replace模式会有动画，add模式要通过hide方法才能有动画显示
            transaction.hide(currentFragment);
        }
        transaction.commitAllowingStateLoss();//.commit();会报错，commitAllowingStateLoss不会报错，activity状态可能会丢失
        if (currentFragment != null) {
            currentFragment.doPause();
        }
    }

    /**
     * 拼接fragment的Tag
     * @param fragment
     * @return
     */
    private String getFragmentTag(QDFragment fragment) {
        return fragment.getClass().getSimpleName() + "-" + fragment.hashCode();
    }

    public void replaceFragment(QDFragment fragment, int containerViewId) {
        QDLogger.println("fragment"," replaceFragment:" + fragment.getClass().getSimpleName() + ",containerViewId=" + containerViewId + ",getFragments()=" + getFragments().size());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            transaction.setCustomAnimations(animation1, animation2,
                    animation3, animation4);
        }
        transaction.replace(containerViewId, fragment, getFragmentTag(fragment))//R.id.qd_fragment_content_view
                .addToBackStack(getFragmentTag(fragment))
                .commit();
    }
    
 /*   public static View getContentView(Activity activity) {
        ViewGroup view = (ViewGroup) activity.getWindow().getDecorView();
        FrameLayout content = view.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }*/

    public void onDestroy() {
        if (fragments != null) {
            fragments = null;
        }
        if (fragmentManager != null) {
            fragmentManager.removeOnBackStackChangedListener(this);
            fragmentManager = null;
        }
    }

    @Override
    public void onBackStackChanged() {
        String str = "";
        for (Fragment fragment : getFragments()) {
            str += fragment.getClass().getSimpleName() + ",";
        }
        QDLogger.println("fragment","onBackStackChanged " + str);
    }

    @Override
    public void navigate(FragmentActivity context, QDFragment fragment, int containerViewId, Intent intent) {
        ((ViewLifecycle) fragment).setIntent(intent);
        startFragment(fragment, containerViewId);
    }

    @Override
    public void navigateForResult(FragmentActivity context, ViewLifecycle qdFragmentInterface, QDFragment fragment, int containerViewId, Intent intent, int requestCode) {
        ((ViewLifecycle) fragment).setIntent(intent);
        ((ViewLifecycle) fragment).setRequestCode(qdFragmentInterface, requestCode);
        startFragment(fragment, containerViewId);
    }

    Builder builder;

    public Builder build(Context context, String classPath) {
        if (builder == null) {
            builder = new Builder(context, classPath, this);
        } else {
            builder.setClassPath(classPath);
        }
        return builder;
    }

    public void OnBackPressed() {
        //QDLogger.println("OnBackPressed1 " + getCurrentFragment());
        if (!fragmentManager.isStateSaved() && fragmentManager.popBackStackImmediate()) {
            QDFragment currentFragment = (QDFragment) getCurrentFragment();
            QDLogger.d("fragment", "OnBackPressed 强制 resume=>" + currentFragment);
            if (currentFragment != null) {
                currentFragment.doResume();
            }
            return;
        }
        /*
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = count - 1; i >= 0; i--) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
            QDLogger.println(backStackEntry);
            if (backStackEntry.getName().equals(fragmentClass.getSimpleName())) {
                return;
            } else {
                QDLogger.println("移除fragment " + backStackEntry);
                fragmentManager.popBackStack(backStackEntry.getId(), 1);
            }
            //backStackEntry.
        }*/
    }

    /**
     * 回退到某个fragment
     *
     * @param fragmentClass
     */
    public void backTo(Class fragmentClass) {
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = count - 1; i >= 0; i--) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
            QDLogger.d(backStackEntry.getName());
            if (backStackEntry.getName().startsWith(fragmentClass.getSimpleName() + "-")) {
                QDFragment currentFragment = (QDFragment)fragmentManager.findFragmentByTag(backStackEntry.getName());
                QDLogger.d("fragment", "backTo 强制 resume=>" + currentFragment);
                if (currentFragment != null) {
                    currentFragment.doResume();
                }
                return;
            } else {
                //QDLogger.println("移除fragment " + backStackEntry);
                fragmentManager.popBackStack(backStackEntry.getId(), 1);
            }
        }
    }

    /*释放*/
    @Override
    public void onRelease() {

    }

    /**
     * 弹出其他栈，仅保留某class集合内的activity
     *
     * @param classList 要保留的 activity 类
     */
    public void popOtherFragmentExceptList(List<Class> classList) {
        if (classList == null) {
            return;
        }
        String classStr = "";
        for (int i = 0; i < classList.size(); i++) {
            classStr += classList.get(i).getName() + ",";
        }
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
            QDLogger.d("fragment", "backStackEntry name=" + backStackEntry.getName());
            if (!classStr.contains("." + backStackEntry.getName())) {
                QDLogger.d("fragment", "移除fragment " + backStackEntry);
                fragmentManager.popBackStack(backStackEntry.getId(), 1);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
/*
    public void onActivityResult(QDFragmentInterface fragment,int mRequestCode, int mResultCode, Intent mResultData) {
        //onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        fragment.onActivityResult();
    }*/

    public static class Builder {
        FragmentActivity context;
        int containerViewId = android.R.id.content;
        Bundle bundle;
        String classPath;
        FragmentHelper fragmentHelper;

        public Builder(Context context, String classPath, FragmentHelper fragmentHelper) {
            this.fragmentHelper = fragmentHelper;
            this.context = (FragmentActivity) context;
            this.classPath = classPath;
        }

        public Builder setClassPath(String classPath) {
            this.classPath = classPath;
            return this;
        }

        public Builder putExtras(Bundle extras) {
            this.bundle = extras;
            return this;
        }

        public Builder putExtra(String key, Object objValue) {
            return this;
        }

        public Builder setContainerViewId(int containerViewId) {
            this.containerViewId = containerViewId;
            return this;
        }

        public void navigation() {
            navigation(null, false, 0);
        }

        public void navigation(ViewLifecycle viewLifecycle, int requestCode) {
            navigation(viewLifecycle, true, requestCode);
        }

        public void navigation(ViewLifecycle viewLifecycle, boolean isForResult, int requestCode) {
            try {
                Class fragmentClass = Class.forName(classPath);
                QDFragment fragment = (QDFragment) fragmentClass.newInstance();
                Intent intent = new Intent();
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                if (isForResult) {
                    fragmentHelper.navigateForResult(context, viewLifecycle, fragment, containerViewId, intent, requestCode);
                } else {
                    fragmentHelper.navigate(context, fragment, containerViewId, intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
