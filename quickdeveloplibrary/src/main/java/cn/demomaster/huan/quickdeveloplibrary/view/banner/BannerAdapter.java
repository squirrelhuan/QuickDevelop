package cn.demomaster.huan.quickdeveloplibrary.view.banner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.demomaster.qdlogger_library.QDLogger;

public class BannerAdapter extends FragmentStateAdapter {

    final OnPlayingListener onloadingListener;
    final List<AdsResource> mAdsResourceList;
    ViewPager2 viewPager2;

    public BannerAdapter(@NonNull FragmentActivity fragmentActivity, List<AdsResource> adsResourceList, ViewPager2 viewPager2, OnPlayingListener onloadingListener) {
        super(fragmentActivity);
        this.mAdsResourceList = adsResourceList;
        this.onloadingListener = onloadingListener;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // return mFragments.get(position);
        int position1 = (position - basePosition) % getItemCount2();
        if (position1 < 0) {
            position1 = getItemCount2() + position1;
        }
        int fragmentCode = position1 + basePosition;
        BannerFragment fragment = new BannerFragment(mAdsResourceList.get(position1), onloadingListener, this, fragmentCode);

       /* Class<? extends Fragment> clazz = FragmentFactory.loadFragmentClass(
                viewPager2.getContext().getClassLoader(), BannerFragment.class.getName());
        Fragment fragment = null;
        try {
            fragment = clazz.getConstructor().newInstance();
            Bundle args = new Bundle();
            args.putInt("");
            if (args != null) {
                args.setClassLoader(fragment.getClass().getClassLoader());
                fragment.setArguments(args);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/

        registerFragment(fragment);
        return fragment;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        //QDLogger.e("onBindViewHolder");
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        //System.out.println("onDetachedFromRecyclerView");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // System.out.println("onViewDetachedFromWindow");
    }

    @Override
    public int getItemCount() {
        return (mAdsResourceList == null || mAdsResourceList.size() == 0) ? 0 : Integer.MAX_VALUE;
    }

    Map<Integer, BannerFragmentInterface> adsFragmentMap = new HashMap<>();

    public void setCurrentItem(int currentItem) {

    }

    public final static int basePosition = Integer.MAX_VALUE / 2;

    public static int getRealPosition(int position) {
        return position - basePosition;
    }
    
    /**
     * 真实资源数量
     *
     * @return
     */
    public int getItemCount2() {
        return (mAdsResourceList == null || mAdsResourceList.size() == 0) ? 0 : mAdsResourceList.size();
    }

    public void setData(List<AdsResource> list) {
        mAdsResourceList.clear();
        mAdsResourceList.addAll(list);
    }

    public int getCurrentItem() {
        return viewPager2.getCurrentItem();
    }

    /**
     * fragment注册激活状态监听
     *
     * @param adsFragment
     */
    public void registerFragment(BannerFragment adsFragment) {
        adsFragmentMap.put(adsFragment.hashCode(), adsFragment);
        //System.out.println("新增页面:" + fragmentCode);
    }

    int mCurrentItemHashCode;

    public void onFragmentActived(int hashCode) {
        QDLogger.println("onFragmentActived:" + hashCode);
        mCurrentItemHashCode = hashCode;
        for (Map.Entry entry : adsFragmentMap.entrySet()) {
            BannerFragmentInterface anInterface = (BannerFragmentInterface) entry.getValue();
            anInterface.onActiveChanged(mCurrentItemHashCode);
        }
    }

    /**
     * 释放移除的fragment
     *
     * @param fragmentCode
     */
    public void unregisterFragment(int fragmentCode) {
        try {
            adsFragmentMap.remove(fragmentCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnPlayingListener {
        void onVideoComplete();//視頻播放完成
    }
}
